package models;

import com.google.inject.ImplementedBy;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.concurrent.CompletionStage;
import java.util.List;

@ImplementedBy( DBSR4_DiceRepositoryJPA.class )
public interface DBSR4_DiceRepository {

    CompletionStage<SR4_DiceRoll> insert( SR4_DiceRoll diceRoll );

    CompletionStage<SR4_DiceRoll> merge( SR4_DiceRoll diceRoll );

    CompletionStage<List<SR4_DiceRoll>> getLastDiceRolls( int combatId, LocalDateTime zeit );

    List<SR4_DiceRoll> getDiceRollsSince( int combatId, LocalDateTime zeit );

    CompletionStage<Integer> getLastDiceRollsNbr( int combatId, LocalDateTime zeit );

    void flush() throws PersistenceException;

    void clear();
}
