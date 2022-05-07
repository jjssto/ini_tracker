package models.db.coc;

import com.google.inject.ImplementedBy;
import models.coc.CocDiceRolls;
import play.mvc.Http;
import play.mvc.Result;

import java.time.LocalDateTime;
import java.util.concurrent.CompletionStage;

@ImplementedBy( DbCocCombatRepoImpl.class )
public interface DbCocCombatRepo {


    void newCombat( String name );

    void removeCombat( int combatId );

    CompletionStage<Result> index( Http.Request request );
}
