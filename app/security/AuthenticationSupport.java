package security;

import models.db.sec.DB_SEC_UserRepository;
import play.mvc.Http;
import play.mvc.Security;

import javax.inject.Inject;
import java.util.Optional;

public class AuthenticationSupport extends Security.Authenticator {

    private final DB_SEC_UserRepository userRepo;

    @Inject
    public AuthenticationSupport( final DB_SEC_UserRepository userRepository) {
        this.userRepo = userRepository;
    }

    @Override
    public Optional<String> getUsername(Http.Request request) {
        if ( request.getCookie( "user-login" ).isPresent() ) {
            String token = request.getCookie("user-login" ).get().value();
            return Optional.of( userRepo.userNameFromToken( token ).toString());
        } else {
            return Optional.empty();
        }
    }

    public Optional<String> getUsername(Http.RequestHeader request) {
        if ( request.getCookie( "user-login" ).isPresent() ) {
            String token = request.getCookie("user-login" ).get().value();
            return Optional.of( userRepo.userNameFromToken( token ).toString());
        } else {
            return Optional.empty();
        }
    }
}
