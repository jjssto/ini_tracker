package models.db.rtc;

import models.db.DB_DatabaseExecutionContext;
import models.rtc.RtcCombat;
import models.sec.SEC_SecurityRole;
import models.sec.SEC_User;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DbRtcCombatRepoImpl implements DbRtcCombatRepo {

    private final DB_DatabaseExecutionContext ec;
    private final JPAApi jpaApi;

    @Inject
    public DbRtcCombatRepoImpl(
        DB_DatabaseExecutionContext ec,
        JPAApi jpaApi
    ) {
        this.ec = ec;
        this.jpaApi = jpaApi;
    }

    @Override
    public CompletionStage<RtcCombat> getById( int id ) {
        return CompletableFuture.supplyAsync(
            () -> jpaApi.withTransaction(
                em -> {
                    return em.find( RtcCombat.class, id );
            }),
            ec
        );
    }

    @Override
    public CompletionStage<List<RtcCombat>> getAllByAccess( SEC_SecurityRole securityRole ) {
        return CompletableFuture.supplyAsync(
            () -> jpaApi.withTransaction(
                em -> {
                    return em.createQuery(
                        "from RtcCombat c where c.securityRole = ?"
                    ).setParameter( 0, securityRole ).getResultList();
                }),
            ec
        );
    }

    @Override
    public CompletionStage<List<SEC_SecurityRole>> getAccessRoles() {
        return CompletableFuture.supplyAsync(
            () -> jpaApi.withTransaction(
                em -> {
                    return em.createQuery(
                        "select r from RtcCombat c join c.securityRole r"
                    ).getResultList();
                }),
            ec
        );
    }

    @Override
    public CompletionStage<List<RtcCombat>> getByUser( SEC_User user ) {
        return CompletableFuture.supplyAsync(
            () -> jpaApi.withTransaction(
                em -> {
                    return em.createQuery(
                        "select c from RtcCombat c " +
                            "join c.securityRole r" +
                            "where r in :roles"
                    ).setParameter( "roles", user.getRoles() ).getResultList();
                }),
            ec
        );
    }
}

