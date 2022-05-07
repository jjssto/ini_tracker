package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.typesafe.config.Config;
import models.db.sec.DbSecException;
import models.db.sec.DbSecTokenRepository;
import models.db.sec.DbSecUserRepository;
import models.db.sec.DbSecSecurityRoleRepository;
import models.sec.SecSecurityRole;
import models.sec.SecToken;
import models.sec.SecUser;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

/**
 * Contains methods to display pages
 */
public class PageController extends Controller {

    private final DbSecUserRepository userRepo;
    private final DbSecTokenRepository sessionTokenRepo;
    private final DbSecSecurityRoleRepository roleRepo;
    private final FormFactory formFactory;
    private final HttpExecutionContext ec;
    private final Config config;

    @Inject
    public PageController(
        DbSecUserRepository userRepo,
        DbSecTokenRepository sessionTokenRepo,
        DbSecSecurityRoleRepository roleRepo,
        FormFactory formFactory,
        HttpExecutionContext ec,
        Config config

    ) {
        this.userRepo = userRepo;
        this.sessionTokenRepo = sessionTokenRepo;
        this.roleRepo = roleRepo;
        this.formFactory = formFactory;
        this.ec = ec;
        this.config = config;
    }

/** Displays page that contains a list of available combats */
    public Result index( Http.Request request ) {
        return ok( views.html.index.render() );
    }

    public Result loginPage( Http.RequestHeader requestHeader ) {
        return ok( views.html.login.render( requestHeader) );
    }

    public Result login( Http.Request request ) {
        DynamicForm form = formFactory.form().bindFromRequest( request );
        String userName = form.get( "userName" );
        String password = form.get( "password" );
        SecUser user;
        createAdmin();
        try {
            user = userRepo.findByUserName( userName ).toCompletableFuture().get();
        } catch ( EntityNotFoundException | ExecutionException | InterruptedException e ) {
            return badRequest();
        }
        if ( user != null && user.checkPassword( password ) ) {
            String token;
            SecToken sessionToken;
            sessionToken = new SecToken( user );
            sessionTokenRepo.persist( sessionToken );
            return ok( views.html.index.render() ).addingToSession( request, "LOGIN_TOKEN", sessionToken.getToken() );
        } else {
           return loginPage( request );
        }
    }

    private void createAdmin() {
        String defaultPW = config.getString( "default_admin_pw" );
        CompletionStage<SecSecurityRole> role = roleRepo.roleExists( "admin" ).thenApplyAsync(
            exists -> {
                if ( !exists ) {
                    SecSecurityRole sr = new SecSecurityRole( "admin" );
                    roleRepo.persist( sr );
                }
                return null;
            },
            ec.current()
        );
        userRepo.userExists( "admin" ).thenApplyAsync(
            exists -> {
                if ( !exists ) {
                    SecUser admin = new SecUser( "admin", defaultPW );
                    try {
                        SecSecurityRole adminRole = roleRepo.getSecurityRole( "admin" ).toCompletableFuture().get();
                        admin.addRole( adminRole );
                        userRepo.merge( admin );
                    } catch ( ExecutionException | InterruptedException e ) {
                        /* TODO */
                    }
                }
                return null;
            },
            ec.current()
        );
    }
    public Result logout( Http.Request request ) {
        String token = request.session().get("LOGIN_TOKEN").toString();
        sessionTokenRepo.deleteToken( token );
        return ok( request.uri() ).removingFromSession( request, "LOGIN_TOKEN" );
    }

    @Restrict( @Group("admin"))
    public Result createUserPage( Http.RequestHeader requestHeader ) {
        return ok( views.html.adm.adm_createUser.render( requestHeader ));
    }

    @Restrict( @Group("admin"))
    public CompletionStage<Result> userList( Http.RequestHeader requestHeader ) {
       return userRepo.getAll().thenApplyAsync(
            list -> ok( views.html.adm.adm_user.render( list )),
            ec.current()
        );
    }

    @Restrict( @Group("admin"))
    public Result newPassword( Http.Request request ) {
        DynamicForm form = formFactory.form().bindFromRequest( request );
        int userId = Integer.parseInt( form.get( "userId") );
        String password = form.get( "password" );
        newPassword( userId, password );
        return ok();
    }

    @Restrict( @Group("admin"))
    public Result passwordPage( ) {
        return ok( views.html.adm.adm_password.render() );
    }
    public CompletionStage<Result> changePassword( Http.Request request ) {
        DynamicForm form = formFactory.form().bindFromRequest( request );
        String oldPW = form.get( "oldPW" );
        String newPW = form.get( "newPW" );
        return userRepo.findByToken( getLoginToken( request ) ).thenApplyAsync(
            user -> {
                if ( user.checkPassword( oldPW ) ) {
                    newPassword( user.getId(), newPW );
                    return ok( "Passwort geändert.");
                } else {
                    return badRequest( "Passwort falsch");
                }
            },
            ec.current()
        );
    }

    @Restrict( @Group("admin"))
    private void newPassword( int userId, String password ) {
        userRepo.get( userId ).thenApplyAsync(
            user -> {
                user.changePassword( password );
                userRepo.merge( user );
                return 1;
            },
            ec.current()
        );
    }

    @Restrict( @Group("admin"))
    public Result removeUser( Http.Request request ) {
        DynamicForm form = getForm( request );
        int userId = Integer.parseInt( form.get( "userId") );
        userRepo.remove( userId );
        return ok();
    }


    @Restrict( @Group("admin"))
    public Result createUser( Http.Request request ) {
        DynamicForm form = getForm( request );
        String userName = form.get( "userName" );
        String password = form.get( "password" );
        SecUser newUser = new SecUser( userName, password );
        if ( userRepo.persist( newUser ).toCompletableFuture().join() == 1 ) {
            return index( request );
        } else {
            return badRequest( "Fehler beim Speichern.");
        }
    }

    public CompletionStage<Result> getUser( Http.Request request ) {
        return userRepo.findByToken( getLoginToken( request ) ).thenApplyAsync(
            user -> {
                if ( user != null ) {
                    return ok( user.getUserName() );
                } else {
                    return ok( "" );
                }

            },
            ec.current()
        );
    }

    @Restrict( @Group("admin"))
    public CompletionStage<Result> editUser( Http.Request request ) {
        DynamicForm form = getForm( request );
        int userId = parseInt( "userId", form );
        return roleRepo.roles( userId );
    }


    @Restrict( @Group("admin"))
    public Result addRole( Http.Request request ) {
        DynamicForm form = getForm( request );
        int roleId = parseInt( "role", form );
        int userId = parseInt( "user", form );
        try {
            userRepo.addRole( userId, roleId );
            return ok("Role added");
        } catch ( DbSecException e ) {
            return badRequest( e.getMessage() );
        }
    }

    @Restrict( @Group("admin"))
    public Result removeRole( Http.Request request ) {
        DynamicForm form = getForm( request );
        int roleId = parseInt( "role", form );
        int userId = parseInt( "user", form );
        try {
            userRepo.removeRole( userId, roleId );
            return ok("Role added");
        } catch ( DbSecException e ) {
            return badRequest( e.getMessage() );
        }
    }

    private String getLoginToken( Http.RequestHeader requestHeader ) {
        try {
            String loginToken = requestHeader.session().get( "LOGIN_TOKEN" ).map( token -> { return token; } ).get();
            return loginToken;
        } catch ( Exception e ) {
            return "";
        }
    }

    private int parseInt(
        String key,
        DynamicForm form
    ) {
        try {
            return Integer.parseInt( form.get( key ) );
        } catch ( Exception e ) {
            return 0;
        }
    }

    private DynamicForm getForm(
        Http.Request request
    ) {
        return formFactory.form().bindFromRequest( request );
    }

}