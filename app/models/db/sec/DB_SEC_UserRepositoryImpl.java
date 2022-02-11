package models.db.sec;

import models.db.DB_DatabaseExecutionContext;
import models.sec.SEC_SecurityRole;
import models.sec.SEC_User;
import models.sec.SEC_UserPermission;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class DB_SEC_UserRepositoryImpl implements DB_SEC_UserRepository {

    private JPAApi jpaApi;
    private final DB_DatabaseExecutionContext ec;


    @Inject
    public DB_SEC_UserRepositoryImpl(
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
                        List<SEC_User> list = entityManager.createQuery(
                                "select u " +
                                    "from SEC_Token t join t.user u " +
                                    "where t.token = :token ", SEC_User.class)
                            .setParameter("token", token)
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
                        List<SEC_User> list = entityManager.createQuery(
                                    "from SEC_User u where u.userName = :userName ", SEC_User.class)
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
                        List<SEC_User> list = entityManager.createQuery(
                                "from SEC_User u where u.userName = :userName ", SEC_User.class)
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
    public CompletionStage<Boolean> roleExists( String role ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        List<SEC_SecurityRole> list = entityManager.createQuery(
                                "from SEC_SecurityRole r where r.roleName = :role ", SEC_SecurityRole.class)
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
    public CompletionStage<SEC_SecurityRole> getSecurityRole ( String role ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        List<SEC_SecurityRole> list = entityManager.createQuery(
                            "from SEC_SecurityRole r where r.roleName = :role "
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
    public CompletionStage<SEC_UserPermission> getUserPermission ( String permission ) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        List<SEC_UserPermission> list = entityManager.createQuery(
                            "from SEC_UserPermission p where p.value = :perm "
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
                        List<SEC_UserPermission> list = entityManager.createQuery(
                                "from SEC_UserPermission p where p.value = :permission ", SEC_UserPermission.class)
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
                                    "from SEC_Token t join t.user u " +
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

    @Override
    public CompletionStage<Integer> persist( SEC_SecurityRole role ) {
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
    public CompletionStage<SEC_SecurityRole> merge( SEC_SecurityRole role ) {
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

    @Override
    public CompletionStage<Integer> persist( SEC_UserPermission permission ) {
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
    public CompletionStage<SEC_UserPermission> merge( SEC_UserPermission permission ) {
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
}
