package controllers;

import models.DB_TokenRepository;
import models.SEC_User;
import models.SEC_Token;
import models.DB_UserRepository;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
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
           return forbidden();
        }
    }

    public Result createUserPage( Http.RequestHeader requestHeader ) {
        return ok( views.html.adm_createUser.render( requestHeader ));
    }

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

}
