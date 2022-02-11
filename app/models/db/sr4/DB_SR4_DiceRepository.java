package models.db.sr4;

import com.google.inject.ImplementedBy;
import models.sr4.SR4_DiceRoll;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.concurrent.CompletionStage;
import java.util.List;

@ImplementedBy( DB_SR4_DiceRepositoryImpl.class )
public interface DB_SR4_DiceRepository {

    CompletionStage<SR4_DiceRoll> insert( SR4_DiceRoll diceRoll );

    CompletionStage<SR4_DiceRoll> merge( SR4_DiceRoll diceRoll );

    CompletionStage<List<SR4_DiceRoll>> getLastDiceRolls( int combatId, LocalDateTime zeit );

    List<SR4_DiceRoll> getDiceRollsSince( int combatId, LocalDateTime zeit );

    CompletionStage<Integer> getLastDiceRollsNbr( int combatId, LocalDateTime zeit );

    void flush() throws PersistenceException;

    void clear();
}
