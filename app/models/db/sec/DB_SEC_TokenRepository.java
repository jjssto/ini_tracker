package models.db.sec;

import com.google.inject.ImplementedBy;
import models.sec.SEC_Token;
import models.sec.SEC_User;

import java.util.concurrent.CompletionStage;

@ImplementedBy( DB_SEC_TokenRepositoryImpl.class )
public interface DB_SEC_TokenRepository {

    CompletionStage<Integer> persist( SEC_Token token );

    CompletionStage<SEC_User> getUser( String token );

    void deleteToken( String token );
}
