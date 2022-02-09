package security;

import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.models.Subject;
import models.DB_TokenRepository;
import models.DB_UserRepository;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class MyDeadboltHandler extends AbstractDeadboltHandler {

    private final AuthenticationSupport authenticator;
    private final DB_UserRepository userRepo;
    private final DB_TokenRepository sessionTokenRepo;

    @Inject
    public MyDeadboltHandler(
        final AuthenticationSupport authenticator,
        final DB_UserRepository userRepo,
        final DB_TokenRepository sessionTokenRepo
    ) {
        this.authenticator = authenticator;
        this.userRepo = userRepo;
        this.sessionTokenRepo = sessionTokenRepo;
    }

    @Override
    public CompletionStage<Optional<Result>> beforeAuthCheck(Http.RequestHeader requestHeader, Optional<String> content) {
            return CompletableFuture.supplyAsync (
                () -> Optional.of( Results.ok( views.html.login.render( requestHeader ) ))
            );
    }

    @Override
    public CompletionStage<Optional<? extends Subject>> getSubject(Http.RequestHeader requestHeader) {

        String loginToken;
        try {
            loginToken = requestHeader.session().get( "LOGIN_TOKEN" ).map( token -> { return token; } ).get();
        } catch ( Exception e ) {
            loginToken = "";
        }
        String finalToken = loginToken;
        return CompletableFuture.supplyAsync(
            () -> {
                return Optional.ofNullable( sessionTokenRepo.getUser(
                    finalToken
                ).toCompletableFuture().join() );
            } );
    }

    @Override
    public CompletionStage<Result> onAuthFailure(Http.RequestHeader requestHeader, Optional<String> content) {
        return CompletableFuture.supplyAsync(
            () -> ok( views.html.login.render( requestHeader ) )
        );
    }

    //@Override
    //public CompletionStage<Optional<DynamicResourceHandler>> getDynamicResourceHandler(Http.RequestHeader requestHeader) {
    //    return null;
    //}
}
