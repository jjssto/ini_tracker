package models.db.rtc;

import models.db.DB_DatabaseExecutionContext;
import models.rtc.RtcCombat;
import models.rtc.RtcDiceRolls;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class DB_RTC_DiceRollRepositoryImpl implements DB_RTC_DiceRollRepository {

    private final JPAApi jpaApi;
    private final DB_DatabaseExecutionContext ec;

    @Inject
    public DB_RTC_DiceRollRepositoryImpl(
        JPAApi jpaApi,
        DB_DatabaseExecutionContext ec
    ) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Integer> persist( RtcDiceRolls diceRoll ) {
        return null;
    }

    @Override
    public CompletionStage<Integer> persist( RtcCombat combat ) {
        return null;
    }

    @Override
    public CompletionStage<List<RtcDiceRolls>> getRolls( int combat_id ) {
        return null;
    }

    @Override
    public CompletionStage<List<RtcCombat>> getCombats() {
        return null;
    }

}
