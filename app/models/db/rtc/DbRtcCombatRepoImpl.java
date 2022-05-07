package models.db.rtc;

import models.db.DB_DatabaseExecutionContext;
import models.rtc.RtcCombat;
import models.sec.SecSecurityRole;
import models.sec.SecUser;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.Query;
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
    public CompletionStage<List<RtcCombat>> getAllByAccess( SecSecurityRole securityRole ) {
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
    public CompletionStage<List<SecSecurityRole>> getAccessRoles() {
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
    public CompletionStage<List<RtcCombat>> getByUser( SecUser user ) {
        return CompletableFuture.supplyAsync(
            () -> jpaApi.withTransaction(
                em -> {
                    List<SecSecurityRole> list = em.createQuery(
                        "select r from SecUser u join u.securityRoles r " +
                            "where u = :user and r.roleName = 'rtc'")
                        .setParameter( "user", user ).getResultList();
                    SecSecurityRole rtc;
                    if ( list.size() > 0 ) {
                       rtc = list.get(0);
                    } else {
                        return null;
                    }
                    Query query = em.createQuery(
                        "from RtcCombat c where c.securityRole = :role"
                    ).setParameter( "role", rtc );
                    List<RtcCombat> combatList = query.getResultList();
                    return combatList;
                }),
            ec
        );
    }
}

