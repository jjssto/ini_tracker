package models.db.rtc;

import com.google.inject.ImplementedBy;
import models.rtc.RtcCombat;
import models.sec.SEC_SecurityRole;
import models.sec.SEC_User;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy( DbRtcCombatRepoImpl.class )
public interface DbRtcCombatRepo {

    CompletionStage<RtcCombat> getById( int id );

    CompletionStage<List<RtcCombat>> getAllByAccess( SEC_SecurityRole securityRole );

    CompletionStage<List<SEC_SecurityRole>> getAccessRoles();

    CompletionStage<List<RtcCombat>> getByUser( SEC_User user );
}
