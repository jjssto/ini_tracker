package models.db.sec;

import com.google.inject.ImplementedBy;
import models.sec.SEC_SecurityRole;
import models.sec.SEC_User;
import models.sec.SEC_UserPermission;
import play.mvc.Http;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy( DB_SEC_UserRepositoryImpl.class )
public interface DB_SEC_UserRepository {
    CompletionStage<SEC_User> findByToken( String token );

    CompletionStage<SEC_User> findByRequest( Http.RequestHeader requestHeader );

    CompletionStage<SEC_User> get( int userId );

    CompletionStage<SEC_User> findByUserName( String userName );

    CompletionStage<Boolean> userExists( String userName );

    CompletionStage<Boolean> roleExists( String role );

    CompletionStage<SEC_SecurityRole> getSecurityRole( String role );
    CompletionStage<SEC_UserPermission> getUserPermission( String permission );

    CompletionStage<Boolean> permissionExists( String permission );

    CompletionStage<String> userNameFromToken( String token);

    CompletionStage<List<SEC_User>> getAll();

    void remove( int userId );

    CompletionStage<Integer> persist( SEC_User user );

    CompletionStage<Integer> merge( SEC_User user );


    CompletionStage<Integer> persist( SEC_SecurityRole role );

    CompletionStage<SEC_SecurityRole> merge( SEC_SecurityRole role );

    CompletionStage<Integer> persist( SEC_UserPermission permission );

    CompletionStage<SEC_UserPermission> merge( SEC_UserPermission permission );




    CompletionStage<Integer> flush();
}
