package models.db.gen;

import com.google.inject.ImplementedBy;
import models.gen.GEN_Combat;
import models.gen.GEN_DiceRolls;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy( DB_GEN_DiceRollRepositoryImpl.class )
public interface DB_GEN_DiceRollRepository {

    CompletionStage<Integer> persist( GEN_DiceRolls diceRoll );

    CompletionStage<Integer> persist( GEN_Combat combat );

    CompletionStage<List<GEN_DiceRolls>> getRolls( int combat_id );

    CompletionStage<List<GEN_Combat>> getCombats();
}
