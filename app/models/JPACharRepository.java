package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;


public class JPACharRepository implements CharRepository {

    private final JPAApi api;
    private final DatabaseExecutionContext ec;

    @Inject
    public JPACharRepository(
        JPAApi api,
        DatabaseExecutionContext ec
    ) {
        this.api = api;
        this.ec = ec;
    }



    /** add new instance of class `SR4Char` to DB */
    @Override
    public CompletionStage<SR4Char> persist(SR4Char chara ) {
        return CompletableFuture.supplyAsync(
            () -> wrap(em -> persist(em, chara)), ec);
    }

    /** add new instance of class `CharRecord` to DB */
    @Override
    public CompletionStage<CharRecord> persist(CharRecord record ) {
        return CompletableFuture.supplyAsync(
            () -> wrap(em -> persist(em, record)), ec);
    }

    /** add new instance of class `Combat` to DB */
    @Override
    public CompletionStage<Combat> persist(Combat combat ) {
        return CompletableFuture.supplyAsync(
            () -> wrap(em -> persist(em, combat)), ec);
    }


    /* update */
    /** update an instance of class `SR4Char` in the DB */
    @Override
    public CompletionStage<SR4Char> merge(SR4Char chara ) {
        return CompletableFuture.supplyAsync(
            () -> wrap(em -> merge(em, chara)), ec);
    }

    /** update an instance of class `CharRecord` in the DB */
    @Override
    public CompletionStage<CharRecord> merge(CharRecord record ) {
        return CompletableFuture.supplyAsync(
            () -> wrap(em -> merge(em, record)), ec);
    }

    /** update an instance of class `Combat` in the DB */
    @Override
    public CompletionStage<Combat> merge(Combat combat ) {
        return CompletableFuture.supplyAsync(
            () -> wrap(em -> merge(em, combat)), ec);
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
    @Override
    public CompletionStage<List<SR4Char>> allOthers( Integer combatId ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> listOtherChars( em, combatId ) ),
            ec
        );
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

    @Override
    public CompletionStage<SR4Char> remove( SR4Char chara ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> remove( em, chara ))
        );
    }
    public CompletableFuture<Object> remove(int recordId ) {
        CompletableFuture<Object> objectCompletableFuture = CompletableFuture.supplyAsync(
            () -> wrap(em -> remove(em, recordId))
        );
        return objectCompletableFuture;
    }
    @Override
    public CompletionStage<Combat> remove( Combat combat ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> remove( em, combat ))
        );
    }


    /* Private helper functions */

    private <T> T wrap(Function<EntityManager, T> function) {
        return api.withTransaction(function);
    }

    private SR4Char persist(EntityManager em, SR4Char chara) {
        em.persist(chara);
        return chara;
    }
    private CharRecord persist(EntityManager em, CharRecord record) {
        em.persist(record);
        return record;
    }
    private Combat persist(EntityManager em, Combat combat) {
        em.persist(combat);
        return combat;
    }

    private SR4Char merge(EntityManager em, SR4Char chara) {
        em.merge(chara);
        return chara;
    }
    private CharRecord merge(EntityManager em, CharRecord record) {
        em.merge(record);
        return record;
    }
    private Combat merge(EntityManager em, Combat combat) {
        em.merge(combat);
        return combat;
    }


    private SR4Char remove(EntityManager em, SR4Char chara) {
        em.remove( chara);
        return chara;
    }

    private Integer remove(EntityManager em, int recordId ) {
        em.createNativeQuery(
            "delete from char_record " +
                "where id = ?")
            .setParameter( 1, recordId )
            .executeUpdate();
        return 1;
    }
    private Combat remove(EntityManager em, Combat combat) {
        em.remove(combat);
        return combat;
    }


    private List<SR4Char> listAllChars(EntityManager em) {
        return em.createQuery("select s from SR4Char s", SR4Char.class ).getResultList();
    }
    private List<SR4Char> listOtherChars(EntityManager em, int combatId) {
        return em.createQuery("select s from Combat c join c.charas r join r.chara s where c.id <> :id", SR4Char.class )
            .setParameter( "id", combatId ).getResultList();
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