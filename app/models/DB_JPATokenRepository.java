package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DB_JPATokenRepository implements DB_TokenRepository {

    private final JPAApi jpaApi;
    private final DB_DatabaseExecutionContext ec;

    @Inject
    public DB_JPATokenRepository(
        JPAApi jpaApi,
        DB_DatabaseExecutionContext ec
    ) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    @Override
    public CompletionStage<Integer> persist( SEC_Token token ) {
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
    public CompletionStage<SEC_User> getUser( String token ) {
       return CompletableFuture.supplyAsync(
       () -> {
           return jpaApi.withTransaction(
               entityManager -> {
                   List<SEC_User> list = entityManager.createQuery(
                           "select u from SEC_Token s join s.user u " +
                               "where s.token = :token",
                           SEC_User.class
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
}
