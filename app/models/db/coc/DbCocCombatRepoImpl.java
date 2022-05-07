package models.db.coc;

import models.coc.CocCombat;
import models.db.DB_DatabaseExecutionContext;
import play.db.jpa.JPAApi;
import play.mvc.Http;
import play.mvc.Result;
import views.html.coc.coc_index;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.ok;

public class DbCocCombatRepoImpl implements DbCocCombatRepo {

    private final JPAApi jpaApi;
    private final DB_DatabaseExecutionContext ec;

    @Inject
    public DbCocCombatRepoImpl(
        JPAApi jpaApi,
        DB_DatabaseExecutionContext ec
    ) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    @Override
    public void newCombat( String name ) {
        CocCombat combat = new CocCombat( name );
        jpaApi.withTransaction(
            entityManager -> {
                entityManager.persist( combat );
                entityManager.flush();
            }
        );
    }

    @Override
    public void removeCombat( int combatId ) {
        jpaApi.withTransaction(
            entityManager -> {
                CocCombat combat = entityManager.find( CocCombat.class, combatId );
                entityManager.remove( combat );
                entityManager.flush();
                entityManager.clear();
            }
        );
    }

    @Override
    public CompletionStage<Result> index( Http.Request request) {
        return CompletableFuture.supplyAsync(
            () -> {
                return jpaApi.withTransaction(
                    entityManager -> {
                        List<CocCombat> combatList = entityManager.createQuery(
                            "from CocCombat c"
                        ).getResultList();
                        return ok( coc_index.render( combatList, request ) );
                    }
                );
            }
            ,
            ec
        );
    }
}
