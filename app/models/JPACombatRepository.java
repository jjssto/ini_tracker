package models;

import play.db.jpa.JPAApi;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class JPACombatRepository implements CombatRepository {

    private final JPAApi api;
    private final DatabaseExecutionContext ec;

    @Inject
    public JPACombatRepository(
        JPAApi api,
        DatabaseExecutionContext ec
    ) {
        this.api = api;
        this.ec = ec;
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
    /** Get instance of class `CharRecord` using its ID  */
    @Override
    public CompletionStage<CharRecord> getRecord( Integer recordId ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> em.find( CharRecord.class, recordId )),
            ec);
    }

    /** Get instance of class `Combat` using its ID  */
    @Override
    public CompletionStage<Combat> getCombat( Integer combatId ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> em.find( Combat.class, combatId ) ),
            ec
        );
    }

    /** Stream of the ids and description of all combats in the  DB */
    @Override
    public CompletionStage<List<CombatShort>> allCombats() {
        return CompletableFuture.supplyAsync(
            () -> wrap(this::listAllCombats),
            ec
        );
    }

    @Override
    public CompletionStage<List<CharRecord>> iniList( Integer combatId ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> iniList( em, combatId ) ),
            ec
        );
    }

    @Override
    public CompletableFuture<Object> remove(int recordId ) {
        return CompletableFuture.supplyAsync(
            () -> wrap(em -> remove(em, recordId))
        );
    }

    @Override
    public CompletionStage<Combat> remove( Combat combat ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> remove( em, combat ))
        );
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return api.withTransaction(function);
    }

    private CharRecord persist(EntityManager em, CharRecord record) {
        em.persist(record);
        return record;
    }
    private Combat persist(EntityManager em, Combat combat) {
        em.persist(combat);
        return combat;
    }
    private CharRecord merge(EntityManager em, CharRecord record) {
        em.merge(record);
        return record;
    }
    private Combat merge(EntityManager em, Combat combat) {
        em.merge(combat);
        return combat;
    }

    private List<CombatShort> listAllCombats(EntityManager em) {
        return em.createQuery("from CombatShort", CombatShort.class ).getResultList();
    }
    private List iniList(EntityManager em, Integer combatId ) {
        return em.createNativeQuery("select * from char_record where combat_id = ? order by ini_value desc;", CharRecord.class )
            .setParameter(1,combatId)
            .getResultList();
    }

    private Integer remove(EntityManager em, int recordId ) {
        em.createNativeQuery(
                "delete dice " +
                    "from dice inner join dice_roll on dice.dice_roll_id = dice_roll.id " +
                    "where dice_roll.char_record_id = ?")
            .setParameter( 1, recordId )
            .executeUpdate();
        em.createNativeQuery(
                "delete from char_record " +
                    "where id = ?")
            .setParameter( 1, recordId )
            .executeUpdate();
        em.flush();
        em.clear();
        return 1;
    }

    private Combat remove(EntityManager em, Combat combat) {
        em.remove(combat);
        return combat;
    }


}
