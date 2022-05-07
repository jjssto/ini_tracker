package models.db.sec;

import com.google.inject.ImplementedBy;
import models.sec.SecToken;
import models.sec.SecUser;

import java.util.concurrent.CompletionStage;

@ImplementedBy( DbSecTokenRepositoryImpl.class )
public interface DbSecTokenRepository {

    CompletionStage<Integer> persist( SecToken token );

    CompletionStage<SecUser> getUser( String token );

    void deleteToken( String token );
}
