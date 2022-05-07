package models.db.coc;

import models.coc.CocChar;
import models.coc.CocCombat;
import models.coc.CocDiceRolls;
import models.db.DB_DatabaseExecutionContext;
import models.sec.SecUser;
import play.db.jpa.JPAApi;
import play.mvc.Result;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.ok;

public class DbCocDiceRollsRepoImpl
    implements DbCocDiceRollsRepo {

    private final JPAApi jpaApi;
    private final DB_DatabaseExecutionContext ec;

    @Inject
    public DbCocDiceRollsRepoImpl(
        JPAApi jpaApi,
        DB_DatabaseExecutionContext ec
    ) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Result> getLastDiceRolls(
        LocalDateTime timestamp,
        int combatId
    ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                       String json = jsonLastDiceRolls(
                           entityManager,
                           timestamp,
                           combatId
                       );
                       return ok( json );
                });
            },
            ec
        );
    }

    private String jsonLastDiceRolls(
        EntityManager em,
        LocalDateTime timestamp,
        int combatId
    ) {
        List<CocDiceRolls> list = em.createQuery(
            "select r from CocDiceRolls r " +
                "join r.d10 d join r.combat c " +
                "where d.zeit > :ts and c.id = :id "
        ).setParameter( "ts", timestamp )
        .setParameter( "id", combatId )
            .getResultList();
        StringBuilder sb = new StringBuilder();

        sb.append("{");
        for( int i = 0; i < list.size(); ) {
            sb.append("\"").append(i).append("\"{");
            sb.append( list.get(i).toJson() );
            if ( ++i < list.size() ) {
               sb.append("},");
            }
        }
        sb.append("}}");
        return sb.toString();
    }

    @Override
    public void addDiceRoll(
        CocDiceRolls roll,
        int combatId,
        int charId,
        String token
    ) {
        jpaApi.withTransaction(
            entityManager -> {
                if ( combatId != 0 ) {
                    CocCombat combat = entityManager.find(
                        CocCombat.class,
                        combatId
                    );
                    roll.setCombat( combat );
                }
                if ( charId != 0 ) {
                    CocChar chara = entityManager.find(
                        CocChar.class,
                        charId
                    );
                    roll.setChar( chara );
                }
                if ( token.length() != 0 ) {
                    List<SecUser> list = entityManager.createQuery(
                        "select u from SecToken t join t.user u " +
                            "where t.token = :token "
                    ).setParameter( "token", token ).getResultList();
                    if ( list.size() > 0 ) {
                        roll.setUser( list.get( 0 ) );
                    }
                }
                entityManager.persist( roll.getD10() );
                entityManager.persist( roll.getD100() );
                entityManager.merge( roll );
                entityManager.flush();
                entityManager.clear();
            }
        );
    }

}
