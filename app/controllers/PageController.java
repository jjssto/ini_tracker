package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import models.DB_TokenRepository;
import models.DB_UserRepository;
import models.SEC_Token;
import models.SEC_User;
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

    private final DB_UserRepository userRepo;
    private final DB_TokenRepository sessionTokenRepo;
    private final FormFactory formFactory;
    private final HttpExecutionContext ec;

    @Inject
    public PageController(
        DB_UserRepository userRepo,
        DB_TokenRepository sessionTokenRepo,
        FormFactory formFactory,
        HttpExecutionContext ec

    ) {
        this.userRepo = userRepo;
        this.sessionTokenRepo = sessionTokenRepo;
        this.formFactory = formFactory;
        this.ec = ec;
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
        SEC_User user;
        try {
            user = userRepo.findByUserName( userName ).toCompletableFuture().get();
        } catch ( EntityNotFoundException | ExecutionException | InterruptedException e ) {
            return badRequest();
        }

        if ( user.checkPassword( password ) ) {
            String token;
            SEC_Token sessionToken;
            sessionToken = new SEC_Token( user );
            sessionTokenRepo.persist( sessionToken );
            return ok( views.html.index.render() ).addingToSession( request, "LOGIN_TOKEN", sessionToken.getToken() );
        } else {
           return loginPage( request );
        }
    }
    public Result logout( Http.Request request ) {
        String token = request.session().get("LOGIN_TOKEN").toString();
        sessionTokenRepo.deleteToken( token );
        return ok( request.uri() ).removingFromSession( request, "LOGIN_TOKEN" );
    }

    public Result createUserPage( Http.RequestHeader requestHeader ) {
        return ok( views.html.adm_createUser.render( requestHeader ));
    }

    @SubjectPresent
    public CompletionStage<Result> userList( Http.RequestHeader requestHeader ) {
       return userRepo.getAll().thenApplyAsync(
            list -> ok( views.html.adm_user.render( list )),
            ec.current()
        );
    }

    @SubjectPresent
    public Result newPassword( Http.Request request ) {
        DynamicForm form = formFactory.form().bindFromRequest( request );
        int userId = Integer.parseInt( form.get( "userId") );
        String password = form.get( "password" );
        newPassword( userId, password );
        return ok();
    }

    @SubjectPresent
    public Result passwordPage( ) {
        return ok( views.html.adm_password.render() );
    }
    public CompletionStage<Result> changePassword( Http.Request request ) {
        DynamicForm form = formFactory.form().bindFromRequest( request );
        String oldPW = form.get( "oldPW" );
        String newPW = form.get( "newPW" );
        String loginToken;
        try {
            loginToken = request.session().get( "LOGIN_TOKEN" ).map( token -> { return token; } ).get();
        } catch ( Exception e ) {
            loginToken = "";
        }
        return userRepo.findByToken( loginToken ).thenApplyAsync(
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

    @SubjectPresent
    public Result removeUser( Http.Request request ) {
        DynamicForm form = formFactory.form().bindFromRequest( request );
        int userId = Integer.parseInt( form.get( "userId") );
        userRepo.remove( userId );
        return ok();
    }


    @SubjectPresent
    public Result createUser( Http.Request request ) {
        DynamicForm form = formFactory.form().bindFromRequest( request );
        String userName = form.get( "userName" );
        String password = form.get( "password" );
        SEC_User newUser = new SEC_User( userName, password );
        if ( userRepo.persist( newUser ).toCompletableFuture().join() == 1 ) {
            return index( request );
        } else {
            return badRequest( "Fehler beim Speichern.");
        }
    }

    public CompletionStage<Result> getUser( Http.Request request ) {
        String loginToken;
        try {
            loginToken = request.session().get( "LOGIN_TOKEN" ).map( token -> { return token; } ).get();
        } catch ( Exception e ) {
            loginToken = "";
        }
        return userRepo.findByToken( loginToken ).thenApplyAsync(
            user -> {
                return ok(user.getUserName());
            },
            ec.current()
        );
    }
}
