package models.db.sec;

import com.google.inject.ImplementedBy;
import models.sec.SecSecurityRole;
import models.sec.SecUser;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

@ImplementedBy( DbSecSecurityRoleRepositoryImpl.class )
public interface DbSecSecurityRoleRepository {

    CompletionStage<Result> roles( SecUser user );

    CompletionStage<Result> roles( int userId );

    CompletionStage<Integer> persist( SecSecurityRole role );

    CompletionStage<SecSecurityRole> merge( SecSecurityRole role );

    CompletionStage<Boolean> roleExists( String role );

    CompletionStage<SecSecurityRole> getSecurityRole( String role );
}
