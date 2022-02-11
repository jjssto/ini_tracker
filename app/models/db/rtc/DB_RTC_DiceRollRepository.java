package models.db.rtc;

import com.google.inject.ImplementedBy;
import models.rtc.RTC_Combat;
import models.rtc.RTC_DiceRolls;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy( DB_RTC_DiceRollRepositoryImpl.class )
public interface DB_RTC_DiceRollRepository {
    CompletionStage<Integer> persist( RTC_DiceRolls diceRoll );

    CompletionStage<Integer> persist( RTC_Combat combat );

    CompletionStage<List<RTC_DiceRolls>> getRolls( int combat_id );

    CompletionStage<List<RTC_Combat>> getCombats();
}
