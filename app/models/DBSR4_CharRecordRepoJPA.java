package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DBSR4_CharRecordRepoJPA implements DBSR4_CharRecordRepo {

    private JPAApi jpaApi;
    private DB_DatabaseExecutionContext ec;

    @Inject
    public DBSR4_CharRecordRepoJPA(
        JPAApi jpaApi,
        DB_DatabaseExecutionContext ec
    ) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    @Override
    public CompletionStage<SR4_CharRecord> getCharRecord( int recordId ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        return entityManager.find( SR4_CharRecord.class, recordId );
                    } );
            },
            ec
        );
    }

    @Override
    public CompletionStage<SR4_CharRecord> getCharRecord( int charId, int combatId ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        List<SR4_CharRecord> list = entityManager
                            .createQuery(
                                "from SR4_CharRecord r " +
                                    "join r.chara ch join r.combat co " +
                                    "where ch.id = :charId and co.id = :combatId",
                                SR4_CharRecord.class )
                            .setParameter( "charId", charId )
                            .setParameter( "combatId", combatId )
                            .getResultList();
                        if ( list.size() > 0 ) {
                            return list.get( 0 );
                        } else {
                            return null;
                        }
                    } );
            },
            ec
        );
    }

    @Override
    public void persist( SR4_CharRecord record ) throws EntityExistsException, IllegalArgumentException {
        jpaApi.withTransaction(
            em -> {
                em.persist( record );
            }
        );
    }
    @Override
    public void merge( SR4_CharRecord record ) throws EntityExistsException, IllegalArgumentException {
        jpaApi.withTransaction(
            em -> {
                em.merge( record );
            }
        );
    }

    @Override
    public void flush() throws PersistenceException {
       jpaApi.withTransaction( EntityManager::flush );
    }

    @Override
    public void clear() {
        jpaApi.withTransaction( EntityManager::clear );
    }

}
