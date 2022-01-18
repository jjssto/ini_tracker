package models;

import com.google.inject.ImplementedBy;
import java.util.concurrent.CompletionStage;
import java.util.List;

@ImplementedBy( JPADiceRepository.class )
public interface DiceRepository {

    CompletionStage<DiceRoll> insert(DiceRoll diceRoll );

    CompletionStage<DiceRoll> getDiceRoll( int combatId, int record_id );
    CompletionStage<DiceRoll> getLastDiceRoll( int combatId );

    CompletionStage<List<DiceRoll>> getLastNDiceRolls( int combatId, long n );
    CompletionStage<List<DiceRoll>> getLastNDiceRolls( int combatId, int record_id, int n );
}
