package models;

import com.google.inject.ImplementedBy;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import java.util.concurrent.CompletionStage;

@ImplementedBy( DB_SR4_CharRecordRepoImpl.class )
public interface DB_SR4_CharRecordRepo {
    CompletionStage<SR4_CharRecord> getCharRecord( int recordId );

    CompletionStage<SR4_CharRecord> getCharRecord( int charId, int combatId );

    void remove( int recordId );

    void persist( SR4_CharRecord record ) throws EntityExistsException, IllegalArgumentException;

    void merge( SR4_CharRecord record ) throws EntityExistsException, IllegalArgumentException;

    void flush() throws PersistenceException;

    void clear();
}
