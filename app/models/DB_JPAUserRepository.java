package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DB_JPAUserRepository implements DB_UserRepository {

    private JPAApi jpaApi;
    private final DB_DatabaseExecutionContext ec;


    @Inject
    public DB_JPAUserRepository(
        JPAApi jpaApi,
        DB_DatabaseExecutionContext ec
    ) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    @Override
    public CompletionStage<SEC_User> findByToken( String token) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        return entityManager.createQuery(
                                "select u " +
                                    "from SEC_Token t join t.user u " +
                                    "where t.token = :token ", SEC_User.class)
                            .setParameter("token", token)
                            .getSingleResult();
                    });
            },
            ec
        );
    }

    @Override
    public CompletionStage<SEC_User> get( int userId ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        return entityManager.find( SEC_User.class, userId );
                    });
            },
            ec
        );
    }


    @Override
    public CompletionStage<SEC_User> findByUserName( String userName ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        return entityManager.createQuery(
                                    "from SEC_User u where u.userName = :userName ", SEC_User.class)
                            .setParameter("userName", userName )
                            .getSingleResult();
                    });
            },
            ec
        );
    }


    @Override
    public CompletionStage<String> userNameFromToken(String token) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        return entityManager.createQuery(
                                "select u.userName " +
                                    "from SEC_Token t join t.user u " +
                                    "where t.token = :token ", String.class)
                            .setParameter("token", token)
                            .getSingleResult();
                    });
            },
            ec
        );
    }

    @Override
    public CompletionStage<List<SEC_User>> getAll() {
        return CompletableFuture.supplyAsync(
            () -> { return jpaApi.withTransaction(
                em -> {
                    List<SEC_User> list = em.createQuery(
                        "select u from SEC_User u"
                    ).getResultList();
                    return list;
                });
            },
            ec
        );
    }

    @Override
    public void remove( int userId ) {
        CompletableFuture.supplyAsync(
            () -> { return jpaApi.withTransaction(
                em -> {
                   SEC_User user = em.find( SEC_User.class, userId );
                   em.remove( user );
                   em.flush();
                   em.clear();
                   return 1;
                });
            },
            ec
        );
    }

    @Override
    public CompletionStage<Integer> persist( SEC_User user ) {
        return CompletableFuture.supplyAsync(
            () -> {
                try {
                    return jpaApi.withTransaction(
                   entityManager -> {
                           entityManager.persist( user );
                           return 1;
                   }
                );
               } catch ( Throwable e) {
                    return -1;
                }
            },
            ec
        );
    }

    @Override
    public CompletionStage<Integer> merge( SEC_User user ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        entityManager.merge( user );
                        return 1;
                    }
                );
            },
            ec
        );
    }

    @Override
    public CompletionStage<Integer> flush() {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        entityManager.flush();
                        return 1;
                    }
                );
            },
            ec
        );
    }
}
