package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;

@ImplementedBy( DB_JPATokenRepository.class )
public interface DB_TokenRepository {

    CompletionStage<Integer> persist( SEC_Token token );

    CompletionStage<SEC_User> getUser( String token );
}
