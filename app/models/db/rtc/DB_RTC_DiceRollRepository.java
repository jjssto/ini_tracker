package models.db.rtc;

import com.google.inject.ImplementedBy;
import models.rtc.RtcCombat;
import models.rtc.RtcDiceRolls;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy( DB_RTC_DiceRollRepositoryImpl.class )
public interface DB_RTC_DiceRollRepository {
    CompletionStage<Integer> persist( RtcDiceRolls diceRoll );

    CompletionStage<Integer> persist( RtcCombat combat );

    CompletionStage<List<RtcDiceRolls>> getRolls( int combat_id );

    CompletionStage<List<RtcCombat>> getCombats();
}
