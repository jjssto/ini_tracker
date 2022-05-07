package models.db.sec;

import models.db.DB_DatabaseExecutionContext;
import models.sec.SecSecurityRole;
import models.sec.SecUser;
import models.sec.SecUserPermission;
import play.db.jpa.JPAApi;
import play.mvc.Http;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class DbSecUserRepositoryImpl
    implements DbSecUserRepository {

    private JPAApi jpaApi;
    private final DB_DatabaseExecutionContext ec;


    @Inject
    public DbSecUserRepositoryImpl(
        JPAApi jpaApi,
        DB_DatabaseExecutionContext ec
    ) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    @Override
    public CompletionStage<SecUser> findByToken( String token) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        return findByToken( token, entityManager );
                    });
            },
            ec
        );
    }

    @Override
    public CompletionStage<SecUser> findByRequest( Http.RequestHeader requestHeader) {
        String loginToken;
        try {
            loginToken = requestHeader.session().get( "LOGIN_TOKEN" ).map( token -> { return token; } ).get();
        } catch ( Exception e ) {
            loginToken = "";
        }
        return findByToken( loginToken );
    }



    @Override
    public CompletionStage<SecUser> get( int userId ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        return entityManager.find( SecUser.class, userId );
                    });
            },
            ec
        );
    }


    @Override
    public CompletionStage<SecUser> findByUserName( String userName ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        List<SecUser> list = entityManager.createQuery(
                                    "from SecUser u where u.userName = :userName ", SecUser.class)
                            .setParameter("userName", userName )
                            .getResultList();
                        if ( list.size() > 0 ) {
                            return list.get(0);
                        } else {
                            return null;
                        }
                    });
            },
            ec
        );
    }


    @Override
    public CompletionStage<Boolean> userExists( String userName ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        List<SecUser> list = entityManager.createQuery(
                                "from SecUser u where u.userName = :userName ", SecUser.class)
                            .setParameter("userName", userName )
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
    public CompletionStage<SecUserPermission> getUserPermission ( String permission ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        List<SecUserPermission> list = entityManager.createQuery(
                            "from SecUserPermission p where p.value = :perm "
                        ).setParameter( "perm", permission ).getResultList();
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
    public CompletionStage<Boolean> permissionExists( String permission ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        List<SecUserPermission> list = entityManager.createQuery(
                                "from SecUserPermission p where p.value = :permission ", SecUserPermission.class)
                            .setParameter("permission", permission )
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
    public CompletionStage<String> userNameFromToken(String token) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        List<String> list = entityManager.createQuery(
                                "select u.userName " +
                                    "from SecToken t join t.user u " +
                                    "where t.token = :token ", String.class)
                            .setParameter("token", token)
                            .getResultList();
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
    public CompletionStage<List<SecUser>> getAll() {
        return CompletableFuture.supplyAsync(
            () -> { return jpaApi.withTransaction(
                em -> {
                    List<SecUser> list = em.createQuery(
                        "select u from SecUser u"
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
                   SecUser user = em.find( SecUser.class, userId );
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
    public CompletionStage<Integer> persist( SecUser user ) {
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
    public CompletionStage<Integer> merge( SecUser user ) {
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

    @Override
    public CompletionStage<Integer> persist( SecUserPermission permission ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        entityManager.persist( permission );
                        return 1;
                    }
                );
            },
            ec
        );
    }

    @Override
    public CompletionStage<SecUserPermission> merge( SecUserPermission permission ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        return entityManager.merge( permission );
                    }
                );
            },
            ec
        );
    }

    @Override
    public void removeRole(
        int userId,
        int roleId
    ) throws DbSecException {
        if ( userId == 0 || roleId == 0 ) {
            throw new DbSecException( "Bad arguments" );
        } else {
            CompletableFuture.supplyAsync(
                () -> {
                    return jpaApi.withTransaction(
                        entityManager -> {
                            SecUser user = entityManager.find(
                                SecUser.class,
                                userId
                            );
                            SecSecurityRole role = entityManager.find(
                                SecSecurityRole.class,
                                roleId
                            );
                            user.removeRole( role );
                            entityManager.flush();
                            entityManager.clear();
                            return 0;
                        }
                    );
                },
                ec
            );
        }
    }


    @Override
    public void addRole(
        int userId,
        int roleId
    ) throws DbSecException {
        if ( userId == 0 || roleId == 0 ) {
            throw new DbSecException( "Bad arguments" );
        } else {
            jpaApi.withTransaction(
                entityManager -> {
                    SecUser user = entityManager.find(
                        SecUser.class,
                        userId
                    );
                    SecSecurityRole role = entityManager.find(
                        SecSecurityRole.class,
                        roleId
                    );
                    user.addRole( role );
                    entityManager.flush();
                    entityManager.clear();
                    return 0;
                }
            );
        }
    }

    private SecUser findByToken(
        String token,
        EntityManager entityManager
    ) {
        List<SecUser> list = entityManager.createQuery(
                "select u " +
                    "from SecToken t join t.user u " +
                    "where t.token = :token ", SecUser.class)
            .setParameter("token", token)
            .getResultList();
        if ( list.size() > 0 ) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public class UserDoesNotExist extends Exception{

    }
    public class RoleDoesNotExist extends Exception{

    }

}
