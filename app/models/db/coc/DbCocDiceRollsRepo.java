package models.db.coc;

import com.google.inject.ImplementedBy;
import models.coc.CocDiceRolls;
import play.mvc.Result;

import java.time.LocalDateTime;
import java.util.concurrent.CompletionStage;

@ImplementedBy( DbCocDiceRollsRepoImpl.class )
public interface DbCocDiceRollsRepo {

    CompletionStage<Result> getLastDiceRolls(
        LocalDateTime timestamp,
        int combatId
    );

    void addDiceRoll(
        CocDiceRolls roll,
        int combatId,
        int charId,
        String token
    );
}
