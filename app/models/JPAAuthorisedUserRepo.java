package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class JPAAuthorisedUserRepo implements AuthorisedUserRepo {

    private final JPAApi api;
    private final DatabaseExecutionContext ec;

    @Inject
    public JPAAuthorisedUserRepo(
        JPAApi api,
        DatabaseExecutionContext ec
    ) {
        this.api = api;
        this.ec = ec;
    }

    @Override
    public CompletionStage<AuthorisedUser> findById(int id) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> findById( em, id ) ),
            ec
        );
    }

    @Override
    public CompletionStage<AuthorisedUser> findByUserName( String userName ) {
        return CompletableFuture.supplyAsync(
            () -> wrap( em -> findByUserName( em, userName ) ),
            ec
        );
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return api.withTransaction(function);
    }

    private AuthorisedUser findById( EntityManager em, int id ) {
        return em.find( AuthorisedUser.class, id );
    }

    private AuthorisedUser findByUserName( EntityManager em, String userName ) {
        return em.createQuery(
            "from AuthorisedUser u where u.userName = :userName",
            AuthorisedUser.class
        ).setParameter( "userName", userName )
            .getSingleResult();
    }
}
