package models.db.sr4;

import models.sr4.SR4_Char;
import models.db.DB_DatabaseExecutionContext;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class DB_SR4_CharRepositoryImp implements DB_SR4_CharRepository {

    private final JPAApi api;
    private final DB_DatabaseExecutionContext ec;

    @Inject
    public DB_SR4_CharRepositoryImp(
        JPAApi api,
        DB_DatabaseExecutionContext ec
    ) {
        this.api = api;
        this.ec = ec;
    }

    /** add new instance of class `SR4Char` to DB */
    @Override
    public CompletionStage<SR4_Char> persist( SR4_Char chara ) {
        return CompletableFuture.supplyAsync(
            () -> wrap(em -> persist(em, chara)), ec);
    }

    /* update */
    /** update an instance of class `SR4Char` in the DB */
    @Override
    public CompletionStage<SR4_Char> merge( SR4_Char chara ) {
        return CompletableFuture.supplyAsync(
            () -> wrap(em -> merge(em, chara)), ec);
    }

    /** Get instance of class `SR4Char` using its ID  */
    @Override
    public CompletionStage<SR4_Char> getChar( Integer charId ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> em.find( SR4_Char.class, charId )),
            ec
        );
    }


    @Override
    public CompletionStage<Integer> getRecordId( Integer charId, Integer combatId ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> getRecordId( em, charId, combatId )),
            ec
        );
    }

    /* Listen */
    /** Stream of all instances of class `SR4Char` in the DB */
    @Override
    public CompletionStage<List<SR4_Char>> allChars() {
        return CompletableFuture.supplyAsync(
            () -> wrap( this::listAllChars ),
            ec
        );
    }

    @Override
    public CompletionStage<List<SR4_Char>> allOthers( Integer combatId ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> listOtherChars( em, combatId ) ),
            ec
        );
    }
    @Override
    public CompletionStage<List<SR4_Char>> inCombat( Integer combatId ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> listCharInCombat( em, combatId ) ),
            ec
        );
    }

    @Override
    public CompletionStage<SR4_Char> remove( SR4_Char chara ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> remove( em, chara ))
        );
    }

    /* Private helper functions */

    private <T> T wrap(Function<EntityManager, T> function) {
        return api.withTransaction(function);
    }

    private SR4_Char persist( EntityManager em, SR4_Char chara) {
        em.persist(chara);
        return chara;
    }

    private SR4_Char merge( EntityManager em, SR4_Char chara) {
        em.merge(chara);
        return chara;
    }

    private SR4_Char remove( EntityManager em, SR4_Char chara) {
        em.remove( chara);
        return chara;
    }

    private Integer getRecordId(EntityManager em, int charId, int combatId ) {
        return em.createQuery( "select r.id from SR4_CharRecord r " +
            "join r.chara ch " +
            "join r.combat co " +
            "where ch.id = :charId and co.id = :combatId", Integer.class )
            .setParameter("charId", charId )
            .setParameter( "combatId", combatId )
            .getSingleResult();
    }

    private List<SR4_Char> listAllChars( EntityManager em) {
        return em.createQuery("select s from SR4_Char s", SR4_Char.class ).getResultList();
    }
    private List listOtherChars(EntityManager em, int combatId) {
        return em.createNativeQuery(
            "select c.id, c.ini, c.intuition, c.char_name, c.p_boxes, c.reaction, c.s_boxes, c.pc " +
                "from sr4_char c " +
                "left outer join sr4_char_record r on " +
                "c.id = r.char_id and r.combat_id = ? " +
                "where r.id is null;"
                , SR4_Char.class )
            .setParameter( 1, combatId ).getResultList();
    }
    private List<SR4_Char> listCharInCombat( EntityManager em, int combatId) {
        return em.createQuery("select s from SR4_Combat c join c.charas r join r.chara s where c.id = :id", SR4_Char.class )
            .setParameter( "id", combatId ).getResultList();
    }
}