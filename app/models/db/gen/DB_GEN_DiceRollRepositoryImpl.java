package models.db.gen;

import models.db.DB_DatabaseExecutionContext;
import models.gen.GEN_Combat;
import models.gen.GEN_DiceRolls;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class DB_GEN_DiceRollRepositoryImpl implements DB_GEN_DiceRollRepository {

    private final JPAApi jpaApi;
    private final DB_DatabaseExecutionContext ec;

    @Inject
    public DB_GEN_DiceRollRepositoryImpl(
        JPAApi jpaApi,
        DB_DatabaseExecutionContext ec
    ) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Integer> persist( GEN_DiceRolls diceRoll ) {
        return null;
    }

    @Override
    public CompletionStage<Integer> persist( GEN_Combat combat ) {
        return null;
    }

    @Override
    public CompletionStage<List<GEN_DiceRolls>> getRolls( int combat_id ) {
        return null;
    }

    @Override
    public CompletionStage<List<GEN_Combat>> getCombats() {
        return null;
    }

}
