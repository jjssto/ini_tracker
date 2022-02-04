package models;

import com.google.inject.ImplementedBy;

import java.time.LocalDateTime;
import java.util.concurrent.CompletionStage;
import java.util.List;

@ImplementedBy( DB_JPADiceRepository.class )
public interface DB_DiceRepository {

    CompletionStage<SR4_DiceRoll> insert( SR4_DiceRoll diceRoll );

    CompletionStage<SR4_DiceRoll> getDiceRoll( int combatId, int record_id );
    CompletionStage<SR4_DiceRoll> getLastDiceRoll( int combatId );

    CompletionStage<List<SR4_DiceRoll>> getLastNDiceRolls( int combatId, LocalDateTime zeit );
    CompletionStage<List<SR4_DiceRoll>> getLastNDiceRolls( int combatId, int record_id, int n );
}
