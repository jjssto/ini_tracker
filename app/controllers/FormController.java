package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.api.i18n.MessagesApi;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;


/**
 * Contains methods for user input
 */
public class FormController extends Controller {

    private final CharRepository repo;
    private final DiceRepository diceRepo;
    private final HttpExecutionContext ec;
    private final FormFactory formF;
    private final MessagesApi messagesApi;
    private Http.Request request;

    @Inject
    public FormController(
        FormFactory formF,
        CharRepository repo,
        DiceRepository diceRepo,
        HttpExecutionContext ec,
        MessagesApi messagesApi
    ) {
        this.formF = formF;
        this.repo = repo;
        this.diceRepo = diceRepo;
        this.ec = ec;
        this.messagesApi = messagesApi;
    }

    public Result combat( Http.Request request ) {
        DynamicForm form = formF.form().bindFromRequest( request );
        Integer id = Integer.valueOf( form.get("id") );
        return redirect( "/combat/" + id.toString() );
    }

    public Result updateCombat( Http.Request request ) {
        JsonNode json = request.body().asJson();
        if ( json == null ) {
            return badRequest( "Expecting Json data" );
        }
        Integer id = json.findPath("id").asInt(0);
        Integer sDmg = json.findPath("sDmg").asInt(-1);
        Integer pDmg = json.findPath("pDmg").asInt(-1);
        Integer localIni = json.findPath("localIni").asInt(-1);
        CharRecord record;
        if ( id == null ) {
            return badRequest( "No valid Id" );
        }
        try {
            record = repo.getRecord(id).toCompletableFuture().get();
            if ( sDmg != -1 ) {
                record.setSDmg(sDmg);
            }
            if ( pDmg != -1 ) {
                record.setPDmg(pDmg);
            }
            if ( localIni != -1 ) {
                record.setLocalIni( localIni );
            }
            repo.update( record ).toCompletableFuture().get();
        } catch ( InterruptedException ie ) {
            return badRequest();
        } catch ( ExecutionException ee ) {
            return badRequest();
        }
        return ok( views.html.combat.render(
            record.getCombatId(),
            request,
            messagesApi.preferred( request )
        ));
    }

    public Result rollInitiative( Integer combatId ) {
        repo.getCombat( combatId ).thenApplyAsync(
            combat -> {
                for( CharRecord charRecord : combat.getCharas() ) {
                    DiceRoll diceRoll = new DiceRoll( 6, charRecord );
                    int ini = charRecord.getNbrIniDice();
                    diceRoll.roll( ini );
                    charRecord.setIniValue( ini + diceRoll.bigger_equal( 5 ));
                    //for ( Die die : diceRoll.getRoll()) {
                    //    diceRepo.insert( die );
                    //}
                    //diceRepo.insert( diceRoll );
                }
                return combat;
                }, ec.current()
        ).thenApplyAsync( combat -> repo.update( combat ), ec.current() );
        return ok("OK");
    }

    public CompletionStage<Result> addChar( Http.Request request ) {
        DynamicForm charForm = formF.form().bindFromRequest( request );
        SR4Char chara = new SR4Char();
        chara.setName( charForm.field("name").toString());
        chara.setIni( Integer.valueOf( charForm.get("ini").toString() ) );
        chara.setReaction( Integer.valueOf( charForm.get("reaction").toString() ) );
        chara.setIntuition( Integer.valueOf( charForm.get("intuition").toString() ) );
        chara.setSBoxes( Integer.valueOf( charForm.get("sBoxes").toString() ) );
        chara.setPBoxes( Integer.valueOf( charForm.get("pBoxes").toString() ) );
        return repo.add( chara ).thenApplyAsync(
            cha -> ok(),
            ec.current()
        );
    }
}