package controllers;

import models.coc.CocChar;
import models.coc.CocCharSheet;
import models.coc.CocDiceRolls;
import models.db.coc.DbCocCombatRepo;
import models.db.coc.DbCocDiceRollsRepo;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.util.concurrent.CompletionStage;

import views.html.coc.coc_charsheet;
import views.html.coc.coc_index;

public class CocController extends Controller {

    private final DbCocDiceRollsRepo cocDiceRollRepo;
    private final DbCocCombatRepo cocCombatRepo;
    private final FormFactory formFactory;

    @Inject
    public CocController(
        DbCocDiceRollsRepo cocDiceRollRepo,
        DbCocCombatRepo cocCombatRepo,
        FormFactory formFactory
    ) {
        this.formFactory = formFactory;
        this.cocDiceRollRepo = cocDiceRollRepo;
        this.cocCombatRepo = cocCombatRepo;
    }

    public CompletionStage<Result> index( Http.Request request ) {
        return cocCombatRepo.index( request );
    }

    public CompletionStage<Result> getLastRolls(
        LocalDateTime time,
        int combatId
    ) {
        return cocDiceRollRepo.getLastDiceRolls( time, combatId );
    }

    public Result roll( Http.Request request ) {
        DynamicForm form = getForm( request );
        int combatId = parseInt( "combat", form );
        int charId = parseInt( "char", form );
        int skill = parseInt( "skill", form );
        short advantage = (short) parseInt( "adv", form );
        String token = getLoginToken( request );
        CocDiceRolls dice = new CocDiceRolls( advantage );
        if ( skill > 0 ) {
            dice.roll( skill );
        } else {
            dice.roll();
        }
        if ( charId != 0 || combatId != 0 ) {
            cocDiceRollRepo.addDiceRoll(
                dice,
                combatId,
                charId,
                token
            );
        }
        return ok( dice.toJson() );
    }

    public Result addCombat( Http.Request request ) {
        DynamicForm form = getForm( request );
        String name = form.get( "name" );
        if ( name.length() > 0 )  {
            cocCombatRepo.newCombat( name );
            return ok( "Saved" );
        } else {
            return badRequest( "No name provided" );
        }

    }

    public Result charSheet() {
        CocChar chara = new CocChar( "Test" );
        CocCharSheet sheet = new CocCharSheet( chara );
        return ok( coc_charsheet.render( sheet, 1 ) );
    }

    private DynamicForm getForm( Http.Request request ) {
       return formFactory.form().bindFromRequest( request );
    }

    private int parseInt( String key, DynamicForm form ) {
        try {
            return Integer.parseInt( form.get( key ) );
        } catch( Throwable e ) {
            return 0;
        }
    }

    private String getLoginToken( Http.RequestHeader requestHeader ) {
        try {
            String loginToken = requestHeader.session().get( "LOGIN_TOKEN" ).map( token -> { return token; } ).get();
            return loginToken;
        } catch ( Exception e ) {
            return "";
        }
    }


}
