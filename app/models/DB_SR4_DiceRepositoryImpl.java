package models;

import play.db.jpa.JPAApi;
import services.HibernateUtil;

import javax.inject.Inject;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class DB_SR4_DiceRepositoryImpl implements DB_SR4_DiceRepository {

    private final JPAApi api;
    private final DB_DatabaseExecutionContext ec;

    private final HibernateUtil hu;

    @Inject
    public DB_SR4_DiceRepositoryImpl(
        JPAApi api,
        DB_DatabaseExecutionContext ec,
        HibernateUtil hu
    ) {
        this.api = api;
        this.ec = ec;
        this.hu = hu;
    }



    @Override
    public CompletionStage<SR4_DiceRoll> insert( SR4_DiceRoll diceRoll ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> insert( em, diceRoll ) ),
            ec
        );
    }

    @Override
    public CompletionStage<SR4_DiceRoll> merge( SR4_DiceRoll diceRoll ) {
       return CompletableFuture.supplyAsync(
           () -> api.withTransaction(
               em -> {
                   return em.merge( diceRoll );
               }
           ),
           ec
       );
    }


    @Override
    public CompletionStage<List<SR4_DiceRoll>> getLastDiceRolls( int combatId, LocalDateTime zeit ) {
        return CompletableFuture.supplyAsync(
            () -> api.withTransaction(
                entityManager -> {
                        return entityManager.createQuery(
                                "select d from SR4_DiceRoll d join d.charRecord r join r.combat c " +
                                    "where c.id = :combatId and d.zeit > :zeit"
                            ).setParameter( "combatId", combatId ).setParameter( "zeit", zeit )
                            .getResultList();

            }),
            ec
        );
    }

    @Override
    public List<SR4_DiceRoll> getDiceRollsSince( int combatId, LocalDateTime zeit ) {
        EntityManager em = hu.getEmf().createEntityManager();
        String queryString =
            "select d from " +
                "SR4_DiceRoll d join d.charRecord r join r.combat c " +
                "where c.id = :combatId and d.zeit > :zeit " +
                "order by d.zeit asc ";
        em.getTransaction().begin();
        Query query =  em.createQuery( queryString )
            .setParameter( "combatId", combatId ).setParameter( "zeit", zeit );
        List<SR4_DiceRoll> ret = query.getResultList();
        em.getTransaction().commit();
        em.close();
        return ret;
    }

    @Override
    public CompletionStage<Integer> getLastDiceRollsNbr( int combatId, LocalDateTime zeit ) {
        return CompletableFuture.supplyAsync(
            () -> api.withTransaction(
                entityManager -> {
                    int nbr = entityManager.createQuery(
                            "select count(*) from SR4_DiceRoll d join d.charRecord r join r.combat c " +
                                "where c.id = :combatId and d.zeit > :zeit"
                        ).setParameter( "combatId", combatId ).setParameter( "zeit", zeit )
                        .getFirstResult();
                    return nbr;
                }),
            ec
        );
    }


    private <T> T wrap(Function<EntityManager, T> function) {
        return api.withTransaction(function);
    }

    private SR4_DiceRoll insert( EntityManager em, SR4_DiceRoll diceRoll ) {
        em.persist( diceRoll );
        em.flush();
        return diceRoll;
    }



    private List<SR4_DiceRoll> get( EntityManager em, int combatId, LocalDateTime timestamp ) {
        //return em.createNativeQuery(
        //"select sr4_dice_roll.id, comment, eyes, zeit, sr4_record_id " +
        //"from sr4_dice_roll join sr4_char_record " +
        //"where sr4_char_record.combat_id = ? and zeit > ?; ",
        //    SR4_DiceRoll.class
        //    )
        //    .setParameter(1, combatId ).setParameter( 2, timestamp ).getResultList();
        return em.createQuery(
                    "select d from SR4_DiceRoll d join d.charRecord r join r.combat c " +
                    "where c.id = :combatId and d.zeit > :timestamp "
            )
            .setParameter("combatId", combatId ).setParameter( "timestamp", timestamp )
            .getResultList();
    }


    @Override
    public void flush() throws PersistenceException {
        api.withTransaction( EntityManager::flush );
    }
    @Override
    public void clear() {
        api.withTransaction( EntityManager::clear );
    }

}
