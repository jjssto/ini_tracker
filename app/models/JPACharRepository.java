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

    /* update */
    /** update an instance of class `SR4Char` in the DB */
    @Override
    public CompletionStage<SR4Char> merge(SR4Char chara ) {
        return CompletableFuture.supplyAsync(
            () -> wrap(em -> merge(em, chara)), ec);
    }

    /** Get instance of class `SR4Char` using its ID  */
    @Override
    public CompletionStage<SR4Char> getChar( Integer charId ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> em.find( SR4Char.class, charId )),
            ec
        );
    }

    /* Listen */
    /** Stream of all instances of class `SR4Char` in the DB */
    @Override
    public CompletionStage<List<SR4Char>> allChars() {
        return CompletableFuture.supplyAsync(
            () -> wrap( this::listAllChars ),
            ec
        );
    }

    @Override
    public CompletionStage<List<SR4Char>> allOthers( Integer combatId ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> listOtherChars( em, combatId ) ),
            ec
        );
    }
    @Override
    public CompletionStage<List<SR4Char>> inCombat( Integer combatId ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> listCharInCombat( em, combatId ) ),
            ec
        );
    }

    @Override
    public CompletionStage<SR4Char> remove( SR4Char chara ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> remove( em, chara ))
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

    private SR4Char merge(EntityManager em, SR4Char chara) {
        em.merge(chara);
        return chara;
    }

    private SR4Char remove(EntityManager em, SR4Char chara) {
        em.remove( chara);
        return chara;
    }

    private List<SR4Char> listAllChars(EntityManager em) {
        return em.createQuery("select s from SR4Char s", SR4Char.class ).getResultList();
    }
    private List listOtherChars(EntityManager em, int combatId) {
        return em.createNativeQuery(
            "select c.id, c.ini, c.intuition, c.char_name, c.p_boxes, c.reaction, c.s_boxes " +
                "from sr4char c " +
                "left outer join char_record r on " +
                "c.id = r.char_id and r.combat_id = ? " +
                "where r.id is null;"
                , SR4Char.class )
            .setParameter( 1, combatId ).getResultList();
    }
    private List<SR4Char> listCharInCombat(EntityManager em, int combatId) {
        return em.createQuery("select s from Combat c join c.charas r join r.chara s where c.id = :id", SR4Char.class )
            .setParameter( "id", combatId ).getResultList();
    }
}