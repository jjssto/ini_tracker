package models.db.sec;

import models.db.DB_DatabaseExecutionContext;
import models.sec.SecSecurityRole;
import models.sec.SecUser;
import play.db.jpa.JPAApi;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.ok;

public class DbSecSecurityRoleRepositoryImpl implements DbSecSecurityRoleRepository {

    private final JPAApi jpaApi;
    private final DB_DatabaseExecutionContext ec;

    @Inject
    public DbSecSecurityRoleRepositoryImpl(
        JPAApi jpaApi,
        DB_DatabaseExecutionContext ec
    ) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Result> roles( int userId ) {
        return CompletableFuture.supplyAsync(
            () -> jpaApi.withTransaction(
                entityManager -> {
                    SecUser user = entityManager.find( SecUser.class, userId );
                    List<SecSecurityRole> userRoles = entityManager.createQuery(
                            "select r " +
                                "from SecUser u join u.securityRoles r " +
                                "where u = :user"
                        ).setParameter( "user", user )
                        .getResultList();

                    List<SecSecurityRole> allRoles = entityManager.createQuery( "from SecSecurityRole s ")
                        .getResultList();
                    return ok( views.html.adm.adm_roles.render(
                        user.getUserName(),
                        user.getId(),
                        userRoles,
                        allRoles
                    ));
                }
            ),
            ec
        );
    }

    @Override
    public CompletionStage<Result> roles( SecUser user ) {
        return CompletableFuture.supplyAsync(
            () -> jpaApi.withTransaction(
                entityManager -> {
                    List<SecSecurityRole> userRoles = entityManager.createQuery(
                            "select r " +
                                "from SecUser u join u.securityRoles r " +
                                "where u = :user"
                        ).setParameter( "user", user )
                        .getResultList();

                    List<SecSecurityRole> allRoles = entityManager.createQuery( "from SecSecurityRole s ")
                            .getResultList();
                    return ok( views.html.adm.adm_roles.render(
                        user.getUserName(),
                        user.getId(),
                        userRoles,
                        allRoles
                    ));
                }
            ),
            ec
        );
    }

    @Override
    public CompletionStage<Boolean> roleExists( String role ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        List<SecSecurityRole> list = entityManager.createQuery(
                                "from SecSecurityRole r where r.roleName = :role ", SecSecurityRole.class)
                            .setParameter("role", role )
                            .getResultList();
                        if ( list.size() > 0 ) {
                            return true;
                        } else {
                            return false;
                        }
                    });
            },
            ec
        );
    }

    @Override
    public CompletionStage<SecSecurityRole> getSecurityRole ( String role ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        List<SecSecurityRole> list = entityManager.createQuery(
                            "from SecSecurityRole r where r.roleName = :role "
                        ).setParameter( "role", role ).getResultList();
                        if ( list.size() > 0 ) {
                            return list.get( 0 );
                        } else {
                            return null;
                        }
                    });
            },
            ec
        );
    }

    @Override
    public CompletionStage<Integer> persist( SecSecurityRole role ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        entityManager.persist( role );
                        return 1;
                    }
                );
            },
            ec
        );
    }

    @Override
    public CompletionStage<SecSecurityRole> merge( SecSecurityRole role ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        return entityManager.merge( role );
                    }
                );
            },
            ec
        );
    }



}
