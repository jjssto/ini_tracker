package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletionStage;
import javax.persistence.EntityManager;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class JPADiceRepository implements DiceRepository {

    private final JPAApi api;
    private final DatabaseExecutionContext ec;

    @Inject
    public JPADiceRepository(
        JPAApi api,
        DatabaseExecutionContext ec
    ) {
        this.api = api;
        this.ec = ec;
    }



    @Override
    public CompletionStage<DiceRoll> insert( DiceRoll diceRoll ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> insert( em, diceRoll ) ),
            ec
        );
    }

    @Override
    public CompletionStage<DiceRoll> getDiceRoll(int combatId, int recordId) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> get( em, recordId) ),
            ec
        );
    }

    @Override
    public CompletionStage<DiceRoll> getLastDiceRoll(int combatId) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> get( em, combatId ) ),
            ec
        );
    }

    @Override
    public CompletionStage<List<DiceRoll>> getLastNDiceRolls(int combatId, LocalDateTime zeit ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> get( em, combatId, zeit ) ),
            ec
        );
    }

    @Override
    public CompletionStage<List<DiceRoll>> getLastNDiceRolls(int combatId, int n, int recordId ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> get( em, combatId, n, recordId ) ),
            ec
        );
    }


    private <T> T wrap(Function<EntityManager, T> function) {
        return api.withTransaction(function);
    }

    private DiceRoll insert( EntityManager em, DiceRoll diceRoll ) {
        em.persist( diceRoll );
        return diceRoll;
    }


    private DiceRoll get( EntityManager em, int recordId ) {
        //em.createNativeQuery(
        //    "select * from dice_roll " +
        //        where
        //
        //);
        //return diceRoll;
        return null;
    }

    private List<DiceRoll> get( EntityManager em, int combatId, LocalDateTime timestamp ) {
        return em.createQuery( "from DiceRoll d where d.zeit > :ts", DiceRoll.class)
            .setParameter("ts", timestamp ).getResultList();
    }

    private List<DiceRoll> get( EntityManager em, int combatId, int n, int recordId ) {
        return null;
    }
}
