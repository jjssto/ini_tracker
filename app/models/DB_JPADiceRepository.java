package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class DB_JPADiceRepository implements DB_DiceRepository {

    private final JPAApi api;
    private final DB_DatabaseExecutionContext ec;

    @Inject
    public DB_JPADiceRepository(
        JPAApi api,
        DB_DatabaseExecutionContext ec
    ) {
        this.api = api;
        this.ec = ec;
    }



    @Override
    public CompletionStage<SR4_DiceRoll> insert( SR4_DiceRoll diceRoll ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> insert( em, diceRoll ) ),
            ec
        );
    }

    @Override
    public CompletionStage<SR4_DiceRoll> getDiceRoll( int combatId, int recordId) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> get( em, recordId) ),
            ec
        );
    }

    @Override
    public CompletionStage<SR4_DiceRoll> getLastDiceRoll( int combatId) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> get( em, combatId ) ),
            ec
        );
    }

    @Override
    public CompletionStage<List<SR4_DiceRoll>> getLastNDiceRolls( int combatId, LocalDateTime zeit ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> get( em, combatId, zeit ) ),
            ec
        );
    }

    @Override
    public CompletionStage<List<SR4_DiceRoll>> getLastNDiceRolls( int combatId, int n, int recordId ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> get( em, combatId, n, recordId ) ),
            ec
        );
    }


    private <T> T wrap(Function<EntityManager, T> function) {
        return api.withTransaction(function);
    }

    private SR4_DiceRoll insert( EntityManager em, SR4_DiceRoll diceRoll ) {
        em.persist( diceRoll );
        return diceRoll;
    }


    private SR4_DiceRoll get( EntityManager em, int recordId ) {
        //em.createNativeQuery(
        //    "select * from dice_roll " +
        //        where
        //
        //);
        //return diceRoll;
        return null;
    }

    private List<SR4_DiceRoll> get( EntityManager em, int combatId, LocalDateTime timestamp ) {
        return em.createNativeQuery(
        "select dice_roll.id, comment, eyes, zeit, dice_roll_id, char_record_id " +
        "from dice_roll join sr4_dice_roll join sr4_char_record " +
        "where sr4_char_record.combat_id = ? and zeit > ?; ",
            SR4_DiceRoll.class
            )
            .setParameter(1, combatId ).setParameter( 2, timestamp ).getResultList();
    }

    private List<SR4_DiceRoll> get( EntityManager em, int combatId, int n, int recordId ) {
        return null;
    }
}
