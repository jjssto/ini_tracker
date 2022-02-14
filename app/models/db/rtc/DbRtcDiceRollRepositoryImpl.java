package models.db.rtc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.db.DB_DatabaseExecutionContext;
import models.rtc.RtcCombat;
import models.rtc.RtcDiceRoll;
import models.rtc.RtcDiceRolls;
import play.db.jpa.JPAApi;
import play.libs.Json;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DbRtcDiceRollRepositoryImpl
    implements DbRtcDiceRollRepository {

    private final JPAApi jpaApi;
    private final DB_DatabaseExecutionContext ec;

    @Inject
    public DbRtcDiceRollRepositoryImpl(
        JPAApi jpaApi,
        DB_DatabaseExecutionContext ec
    ) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Integer> persist( RtcDiceRoll diceRoll ) {
        return CompletableFuture.supplyAsync(
            () -> jpaApi.withTransaction(
                em -> {
                    em.persist( diceRoll );
                    return 1;
                }),
            ec
        );
    }

    @Override
    public CompletionStage<Integer> persist( RtcDiceRolls diceRoll ) {
        return null;
    }

    @Override
    public CompletionStage<Integer> persist( RtcCombat combat ) {
        return null;
    }

    @Override
    public CompletionStage<RtcDiceRolls> merge( RtcDiceRolls diceRolls ) {
        return CompletableFuture.supplyAsync(
            () -> jpaApi.withTransaction(
                em -> {
                    em.persist( diceRolls.getD6() );
                    em.persist( diceRolls.getD8() );
                    em.persist( diceRolls.getD12() );
                    em.flush();
                    return em.merge( diceRolls );
                }),
            ec
        );

    }

    @Override
    public CompletionStage<RtcCombat> merge( RtcCombat combat ) {
        return null;
    }


    @Override
    public CompletionStage<List<RtcDiceRolls>> getRolls( int combat_id ) {
        return null;
    }

    @Override
    public CompletionStage<List<RtcDiceRolls>> getRolls(
        int combatId,
        LocalDateTime timestamp
    ) {
        return CompletableFuture.supplyAsync(
            () -> jpaApi.withTransaction(
                em -> {
                    return getRolls( em, combatId, timestamp );
                }
                ),
            ec
        );

    }

    @Override
    public CompletionStage<String> getRollsJson(
        int combatId,
        LocalDateTime timestamp
    ) {
        return CompletableFuture.supplyAsync(
            () -> jpaApi.withTransaction(
                em -> {
                    List<RtcDiceRolls> list = getRolls( em, combatId, timestamp );
                    StringBuilder sb = new StringBuilder();
                    sb.append("{");
                    for ( int i = 0; i < list.size(); i++ ) {
                        if ( i != 0 ) {
                            sb.append(",");

                        }
                        sb.append("\"").append( i ).append("\":");
                        sb.append( list.get(i).toJson() );
                    }
                    sb.append( "}" );
                    return sb.toString();
                }
            ),
            ec
        );
    }

    @Override
    public CompletionStage<List<RtcCombat>> getCombats() {
        return null;
    }

    private List<RtcDiceRolls> getRolls(
        EntityManager em,
        int combatId,
        LocalDateTime timestamp
    ) {
        Query query = em.createQuery(
                "select r from RtcDiceRolls r " +
                    "join r.d6 d6 join r.d8 d8 join r.d12 d12 " +
                    "join r.combat c " +
                    "where c.id = :combatId " +
                    "and ( d6.zeit > :ts or d8.zeit > :ts or d12.zeit > :ts ) "
            ).setParameter("combatId", combatId )
            .setParameter( "ts", timestamp );
        return query.getResultList();
    }


}
