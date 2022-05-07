package models.db.rtc;

import com.google.inject.ImplementedBy;
import models.rtc.RtcCombat;
import models.sec.SecSecurityRole;
import models.sec.SecUser;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy( DbRtcCombatRepoImpl.class )
public interface DbRtcCombatRepo {

    CompletionStage<RtcCombat> getById( int id );

    CompletionStage<List<RtcCombat>> getAllByAccess( SecSecurityRole securityRole );

    CompletionStage<List<SecSecurityRole>> getAccessRoles();

    CompletionStage<List<RtcCombat>> getByUser( SecUser user );
}
