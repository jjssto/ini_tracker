package models;

import com.google.inject.ImplementedBy;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import java.util.concurrent.CompletionStage;

@ImplementedBy( DBSR4_CharRecordRepoJPA.class )
public interface DBSR4_CharRecordRepo {
    CompletionStage<SR4_CharRecord> getCharRecord( int recordId );

    CompletionStage<SR4_CharRecord> getCharRecord( int charId, int combatId );

    void persist( SR4_CharRecord record ) throws EntityExistsException, IllegalArgumentException;

    void merge( SR4_CharRecord record ) throws EntityExistsException, IllegalArgumentException;

    void flush() throws PersistenceException;

    void clear();
}
