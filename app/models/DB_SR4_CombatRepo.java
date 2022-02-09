package models;

import com.google.inject.ImplementedBy;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy( DB_SR4_CombatRepoImpl.class )
public interface DB_SR4_CombatRepo {

    CompletionStage<SR4_Combat> getIniList(
        int combatId,
        LocalDateTime timestamp
    );

    CompletionStage<SR4_Combat> getCombat( int combatId );

    CompletionStage<List<SR4_CombatShort>> getAllCombats();

    void removeChar( int charRecordId, int combatId );

    void persist( SR4_Combat combat ) throws EntityExistsException, IllegalArgumentException;

    abstract SR4_Combat merge( SR4_Combat combat ) throws EntityExistsException, IllegalArgumentException;

    void flush() throws PersistenceException;

    void clear();
}
