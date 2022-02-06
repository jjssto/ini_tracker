package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DBSR4_JPACombatRepo implements DBSR4_CombatRepo {

    private JPAApi jpaApi;
    private DB_DatabaseExecutionContext ec;

    @Inject
    public DBSR4_JPACombatRepo (
        JPAApi jpaApi,
        DB_DatabaseExecutionContext ec
    ) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    @Override
    public CompletionStage<SR4_Combat> getIniList(
        int combatId,
        LocalDateTime timestamp
    ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        List<SR4_Combat> list = entityManager.createQuery(
                            "from SR4_Combat c " +
                                "where c.id = :combatId and c.lastChanged > :timestamp",
                            SR4_Combat.class
                        ).setParameter( "combatId", combatId )
                            .setParameter( "timestamp", timestamp )
                            .getResultList();
                        if ( list.size() > 0 ) {
                            return list.get(0);
                        } else {
                            return null;
                        }
                 });
            },
            ec
        );
    }

    @Override
    public CompletionStage<SR4_Combat> getCombat( int combatId ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        return entityManager.find( SR4_Combat.class, combatId );
                    } );
            },
            ec
        );
    }

    @Override
    public CompletionStage<List<SR4_CombatShort>> getAllCombats() {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        return entityManager.createQuery(
                            "from SR4_CombatShort",
                            SR4_CombatShort.class
                        ).getResultList();
                    } );
            },
            ec
        );
    }


    @Override
    public void persist( SR4_Combat combat ) throws EntityExistsException, IllegalArgumentException {
        jpaApi.withTransaction(
            em -> {
                em.persist( combat );
            }
        );
    }


    @Override
    public SR4_Combat merge( SR4_Combat combat ) throws EntityExistsException, IllegalArgumentException {
        return jpaApi.withTransaction(
            em -> {
                return em.merge( combat );
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
