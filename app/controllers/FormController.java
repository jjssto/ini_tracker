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

    private final CharRepository charRepo;
    private final CombatRepository combatRepo;
    private final DiceRepository diceRepo;
    private final HttpExecutionContext ec;
    private final FormFactory formF;
    private final MessagesApi messagesApi;

    @Inject
    public FormController(
        FormFactory formF,
        CharRepository charRepo,
        CombatRepository combatRepo,
        DiceRepository diceRepo,
        HttpExecutionContext ec,
        MessagesApi messagesApi
    ) {
        this.formF = formF;
        this.charRepo = charRepo;
        this.combatRepo = combatRepo;
        this.diceRepo = diceRepo;
        this.ec = ec;
        this.messagesApi = messagesApi;
    }

    public Result combat( Http.Request request ) {
        DynamicForm form = formF.form().bindFromRequest( request );
        int id = Integer.parseInt(form.get("id"));
        return redirect( "/combat/" + id);
    }

    public Result updateCombat( Http.Request request ) {
        JsonNode json = request.body().asJson();
        if ( json == null ) {
            return badRequest( "Expecting Json data" );
        }
        int id = json.findPath("id").asInt(0);
        int sDmg = json.findPath("sDmg").asInt(-1);
        int pDmg = json.findPath("pDmg").asInt(-1);
        int localIni = json.findPath("localIni").asInt(-1);
        CharRecord record;
        try {
            record = combatRepo.getRecord(id).toCompletableFuture().get();
            if ( sDmg != -1 ) {
                record.setSDmg(sDmg);
            }
            if ( pDmg != -1 ) {
                record.setPDmg(pDmg);
            }
            if ( localIni != -1 ) {
                record.setLocalIni( localIni );
            }
            combatRepo.merge( record ).toCompletableFuture().get();
        } catch ( InterruptedException | ExecutionException ie ) {
            return badRequest();
        }
        return ok( views.html.combat.render(
            record.getCombatId(),
            request,
            messagesApi.preferred( request )
        ));
    }

    public Result rollInitiative( Integer combatId ) {
        combatRepo.getCombat( combatId ).thenApplyAsync(
            combat -> {
                for( CharRecord charRecord : combat.getCharas() ) {
                    DiceRoll diceRoll = new DiceRoll( 6, charRecord );
                    int ini = charRecord.getNbrIniDice();
                    diceRoll.roll( ini );
                    charRecord.setIniValue( ini + diceRoll.bigger_equal( 5 ));
                    diceRepo.insert( diceRoll );
                }
                return combat;
                }, ec.current()
        ).thenApplyAsync(combatRepo::merge, ec.current() );
        return ok("OK");
    }

    public CompletionStage<Result> addChar( Http.Request request ) {
        DynamicForm charForm = formF.form().bindFromRequest( request );
        SR4Char chara = new SR4Char();
        chara.setName( charForm.get("name"));
        try {
            chara.setIni(Integer.valueOf(charForm.get("ini")));
        } catch( NumberFormatException e1 ) {
            chara.setIni( 0 );
        }
        try {
            chara.setReaction( Integer.valueOf( charForm.get("reaction")) );
        } catch( NumberFormatException e1 ) {
            chara.setReaction( 0 );
        }
        try {
            chara.setIntuition( Integer.valueOf( charForm.get("intuition")) );
        } catch( NumberFormatException e1 ) {
            chara.setIntuition( 0 );
        }
        try {
            chara.setSBoxes( Integer.valueOf( charForm.get("sBoxes")) );
        } catch( NumberFormatException e1 ) {
            chara.setSBoxes( 0 );
        }
        try {
            chara.setPBoxes( Integer.valueOf( charForm.get("pBoxes")) );
        } catch( NumberFormatException e1 ) {
            chara.setPBoxes( 0 );
        }
        return charRepo.persist( chara ).thenApplyAsync(
            cha -> ok(),
            ec.current()
        );
    }

    public Result addCharsToCombat( Http.Request request ) {
        JsonNode json = request.body().asJson();
        Integer combatId = json.findPath("combatId").asInt(0);
        JsonNode charasJ = json.findPath("chars");
        try {
            Combat combat = combatRepo.getCombat(combatId).toCompletableFuture().get();
            for (int i = 0; i < charasJ.size(); i++) {
                SR4Char chara = charRepo.getChar(charasJ.get(i).asInt()).toCompletableFuture().get();
                CharRecord record = new CharRecord( chara, combat);
                combatRepo.persist(record);
                combat.addRecord(record);
            }
            combatRepo.merge(combat);
        } catch ( InterruptedException | ExecutionException ie ) {
            return badRequest();
        }
        return ok() ;
    }

    public Result removeCharsFromCombat( Http.Request request ) {
        JsonNode json = request.body().asJson();
        Integer combatId = json.findPath("combatId").asInt(0);
        JsonNode charasJ = json.findPath("chars");
        try {
            Combat combat = combatRepo.getCombat(combatId).toCompletableFuture().get();
            for (int i = 0; i < charasJ.size(); i++) {
                Integer charId = charasJ.get(i).asInt();
                CharRecord record = combat.removeRecord( charId );
                combatRepo.remove( record.getId() );
            }
         //   repo.merge(combat);
        } catch ( InterruptedException | ExecutionException ie ) {
            return badRequest();
        }
        return ok() ;
    }

    public Result addCombat( Http.Request request ) {
        JsonNode json = request.body().asJson();
        String combat_desc = json.get(0).asText();
        Combat combat = new Combat( combat_desc );
        combatRepo.persist( combat );
        return ok();
    }
}