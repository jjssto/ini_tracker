package models.db.sec;

import models.db.DB_DatabaseExecutionContext;
import models.sec.SecToken;
import models.sec.SecUser;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DbSecTokenRepositoryImpl
    implements DbSecTokenRepository {

    private final JPAApi jpaApi;
    private final DB_DatabaseExecutionContext ec;

    @Inject
    public DbSecTokenRepositoryImpl(
        JPAApi jpaApi,
        DB_DatabaseExecutionContext ec
    ) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Integer> persist( SecToken token ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        entityManager.persist( token );
                        return 1;
                    }
                );
            },
            ec
        );
    }

    @Override
    public CompletionStage<SecUser> getUser( String token ) {
       return CompletableFuture.supplyAsync(
       () -> {
           return jpaApi.withTransaction(
               entityManager -> {
                   List<SecUser> list = entityManager.createQuery(
                           "select u from SecToken s join s.user u " +
                               "where s.token = :token",
                           SecUser.class
                       ).setParameter( "token", token ).getResultList();
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
    public void deleteToken( String token ){
        CompletableFuture.supplyAsync(
            () -> { return jpaApi.withTransaction(
                entityManager -> {
                    SecToken secToken = entityManager.createQuery(
                        "from SecToken t where t.token = :token",
                        SecToken.class
                    ).setParameter( "token", token ).getSingleResult();
                    entityManager.remove( secToken );
                    entityManager.flush();
                    entityManager.clear();
                    return 1;
                }
            );
            }
        );
    }

}
