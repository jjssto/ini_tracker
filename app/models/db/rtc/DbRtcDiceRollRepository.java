package models.db.rtc;

import com.google.inject.ImplementedBy;
import models.rtc.RtcCombat;
import models.rtc.RtcDiceRoll;
import models.rtc.RtcDiceRolls;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy( DbRtcDiceRollRepositoryImpl.class )
public interface DbRtcDiceRollRepository {
    CompletionStage<Integer> persist( RtcDiceRoll diceRoll );

    CompletionStage<Integer> persist( RtcDiceRolls diceRoll );

    CompletionStage<Integer> persist( RtcCombat combat );

    CompletionStage<RtcDiceRolls> merge( RtcDiceRolls diceRoll );

    CompletionStage<RtcCombat> merge( RtcCombat combat );

    CompletionStage<List<RtcDiceRolls>> getRolls( int combat_id );

    CompletionStage<List<RtcDiceRolls>> getRolls(
        int combatId,
        LocalDateTime timestamp
    );

    CompletionStage<String> getRollsJson(
        int combatId,
        LocalDateTime timestamp
    );

    CompletionStage<List<RtcCombat>> getCombats();
}
