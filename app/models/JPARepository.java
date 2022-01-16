package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;


public class JPARepository implements Repository {

    private final JPAApi api;
    private final DatabaseExecutionContext ec;

    @Inject
    public JPARepository(
        JPAApi api,
        DatabaseExecutionContext ec
    ) {
        this.api = api;
        this.ec = ec;
    }

    /** add new instance of class `SR4Char` to DB */
    @Override
    public CompletionStage<SR4Char> add( SR4Char chara ) {
        return CompletableFuture.supplyAsync(
            () -> wrap(em -> insert(em, chara)), ec);
    }

    /** add new instance of class `CharRecord` to DB */
    @Override
    public CompletionStage<CharRecord> add( CharRecord record ) {
        return CompletableFuture.supplyAsync(
            () -> wrap(em -> insert(em, record)), ec);
    }

    /** add new instance of class `Combat` to DB */
    @Override
    public CompletionStage<Combat> add( Combat combat ) {
        return CompletableFuture.supplyAsync(
            () -> wrap(em -> insert(em, combat)), ec);
    }


    /* update */
    /** update an instance of class `SR4Char` in the DB */
    @Override
    public CompletionStage<SR4Char> update( SR4Char chara ) {
        return CompletableFuture.supplyAsync(
            () -> wrap(em -> update(em, chara)), ec);
    }

    /** update an instance of class `CharRecord` in the DB */
    @Override
    public CompletionStage<CharRecord> update( CharRecord record ) {
        return CompletableFuture.supplyAsync(
            () -> wrap(em -> update(em, record)), ec);
    }

    /** update an instance of class `Combat` in the DB */
    @Override
    public CompletionStage<Combat> update( Combat combat ) {
        return CompletableFuture.supplyAsync(
            () -> wrap(em -> update(em, combat)), ec);
    }


    /** Get instance of class `SR4Char` using its ID  */
    @Override
    public CompletionStage<SR4Char> getChar( Integer charId ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return api.withTransaction(
                    em -> {
                        return em.find( SR4Char.class, charId );
                    });
            },
            ec);
    }

    /** Get instance of class `CharRecord` using its ID  */
    @Override
    public CompletionStage<CharRecord> getRecord( Integer recordId ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return api.withTransaction(
                    em -> {
                        return em.find( CharRecord.class, recordId );
                    });
            },
            ec);
    }

    /** Get instance of class `Combat` using its ID  */
    @Override
    public CompletionStage<Combat> getCombat( Integer combatId ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return api.withTransaction(
                    em -> {
                        return em.find( Combat.class, combatId );
                    });
            },
            ec);
    }

    /* Listen */
    /** Stream of all instances of class `SR4Char` in the DB */
    @Override
    public CompletionStage<List<SR4Char>> allChars() {
        return CompletableFuture.supplyAsync(
            () -> {
                return api.withTransaction(
                    em -> {
                        return listAllChars( em);
                    });
            },
            ec);
    }


    /** Stream of the ids and description of all combats in the  DB */
    @Override
    public CompletionStage<List<CombatShort>> allCombats() {
        return CompletableFuture.supplyAsync(
            () -> {
                return api.withTransaction(
                    em -> {
                        return listAllCombats( em );
                    });
            },
            ec);
    }
    @Override
    public CompletionStage<List<CharRecord>> iniList( Integer combatId ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return api.withTransaction(
                    em -> {
                        return iniList( em, combatId );
                    });
            },
            ec);
    }



    /* Private helper functions */

    private <T> T wrap(Function<EntityManager, T> function) {
        return api.withTransaction(function);
    }

    private SR4Char insert(EntityManager em, SR4Char chara) {
        em.persist(chara);
        return chara;
    }
    private CharRecord insert(EntityManager em, CharRecord record) {
        em.persist(record);
        return record;
    }
    private Combat insert(EntityManager em, Combat combat) {
        em.persist(combat);
        return combat;
    }

    private SR4Char update(EntityManager em, SR4Char chara) {
        em.merge(chara);
        return chara;
    }
    private CharRecord update(EntityManager em, CharRecord record) {
        em.merge(record);
        return record;
    }
    private Combat update(EntityManager em, Combat combat) {
        em.merge(combat);
        return combat;
    }


    private List<SR4Char> listAllChars(EntityManager em) {
        return em.createQuery("select s from SR4Char s", SR4Char.class ).getResultList();
    }

    private List<CombatShort> listAllCombats(EntityManager em) {
        return em.createQuery("from CombatShort", CombatShort.class ).getResultList();
    }
    private List<CharRecord> iniList(EntityManager em, Integer combatId ) {
        return em.createNativeQuery("select * from char_record where combat_id = ? order by ini_value desc;", CharRecord.class )
            .setParameter(1,combatId)
            .getResultList();
    }
}