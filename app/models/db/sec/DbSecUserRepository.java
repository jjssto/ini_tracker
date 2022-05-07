package models.db.sec;

import com.google.inject.ImplementedBy;
import models.sec.SecUser;
import models.sec.SecUserPermission;
import play.mvc.Http;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy( DbSecUserRepositoryImpl.class )
public interface DbSecUserRepository {
    CompletionStage<SecUser> findByToken( String token );

    CompletionStage<SecUser> findByRequest( Http.RequestHeader requestHeader );

    CompletionStage<SecUser> get( int userId );

    CompletionStage<SecUser> findByUserName( String userName );

    CompletionStage<Boolean> userExists( String userName );


    CompletionStage<String> userNameFromToken( String token);

    CompletionStage<List<SecUser>> getAll();

    void remove( int userId );

    CompletionStage<Integer> persist( SecUser user );

    CompletionStage<Integer> merge( SecUser user );


    void removeRole( int userId, int roleId ) throws DbSecException;

    void addRole( int userId, int roleId) throws DbSecException;

    CompletionStage<Integer> flush();


    /* Permission Repo */
    CompletionStage<SecUserPermission> getUserPermission( String permission );
    CompletionStage<Boolean> permissionExists( String permission );
    CompletionStage<Integer> persist( SecUserPermission permission );
    CompletionStage<SecUserPermission> merge( SecUserPermission permission );

}
