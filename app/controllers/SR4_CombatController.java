package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.api.i18n.MessagesApi;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@SubjectPresent
public class SR4_CombatController extends Controller {

    private final DB_CharRepository charRepo;
    private final DB_CombatRepository combatRepo;
    private final DB_DiceRepository diceRepo;
    private final HttpExecutionContext ec;
    private final FormFactory formF;
    private final MessagesApi messagesApi;

    @Inject
    public SR4_CombatController(
        FormFactory formF,
        DB_CharRepository charRepo,
        DB_CombatRepository combatRepo,
        DB_DiceRepository diceRepo,
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

    public Result combat(
        Integer combatId,
        Http.Request request
    ) {
        return ok(views.html.sr4_combat.render(
            combatId,
            request,
            messagesApi.preferred( request )));
    }

    public Result index( Http.Request request ) {
        return ok( views.html.sr4_overview.render() );
    }


    public Result updateCombat(Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json == null) {
            return badRequest("Expecting Json data");
        }
        int id = json.findPath("id").asInt(0);
        int sDmg = json.findPath("sDmg").asInt(-1);
        int pDmg = json.findPath("pDmg").asInt(-1);
        int localIni = json.findPath("localIni").asInt(-1);
        SR4_CharRecord record;
        try {
            record = combatRepo.getRecord(id).toCompletableFuture().get();
            if (sDmg != -1) {
                record.setSDmg(sDmg);
            }
            if (pDmg != -1) {
                record.setPDmg(pDmg);
            }
            if (localIni != -1) {
                record.setLocalIni(localIni);
            }
            combatRepo.merge(record).toCompletableFuture().get();
        } catch (InterruptedException | ExecutionException ie) {
            return badRequest();
        }
        return ok(views.html.sr4_combat.render(
            record.getCombatId(),
            request,
            messagesApi.preferred(request)
        ));
    }

    public Result rollInitiative( Http.Request request ) {

        DynamicForm form = formF.form().bindFromRequest(request);
        int combatId = Integer.parseInt( form.get( "combatId" ) );
        combatRepo.getCombat(combatId).thenApplyAsync(
            combat -> {
                for ( SR4_CharRecord charRecord : combat.getCharas()) {
                    SR4_DiceRoll diceRoll = new SR4_DiceRoll(6, charRecord);
                    int ini = charRecord.getNbrIniDice();
                    diceRoll.roll(ini);
                    charRecord.setIniValue(ini + diceRoll.bigger_equal(5));
                    diceRepo.insert(diceRoll);
                }
                return combat;
            }, ec.current()
        ).thenApplyAsync(combatRepo::merge, ec.current());
        return ok("OK");
    }

    public Result roll(Http.Request request) {
        DynamicForm recordForm = formF.form().bindFromRequest(request);
        int charId = Integer.parseInt(recordForm.get("charId"));
        int combatId = Integer.parseInt(recordForm.get("combatId"));
        int nbrOfDice = Integer.parseInt(recordForm.get("nbrOfDice"));
        boolean exploding;
        if ( recordForm.get( "exploding" ).equals("y") ) {
            exploding = true;
        } else {
            exploding = false;
        }
        if (nbrOfDice <= 0 || nbrOfDice > 30) {
            return badRequest();
        }
        SR4_CharRecord charRecord;
        try {
            int recordId = charRepo.getRecordId(charId, combatId).toCompletableFuture().get();
            charRecord = combatRepo.getRecord(recordId).toCompletableFuture().get();
        } catch (InterruptedException | ExecutionException ie) {
            return badRequest();
        }
        SR4_DiceRoll diceRoll = new SR4_DiceRoll(6, charRecord);
        if ( exploding ) {
            diceRoll.explode(nbrOfDice);
        } else {
            diceRoll.roll(nbrOfDice);
        }
        diceRepo.insert(diceRoll);
        return ok();
    }

    public Result addCharsToCombat(Http.Request request) {
        JsonNode json = request.body().asJson();
        Integer combatId = json.findPath("combatId").asInt(0);
        JsonNode charasJ = json.findPath("chars");
        try {
            SR4_Combat combat = combatRepo.getCombat(combatId).toCompletableFuture().get();
            for (int i = 0; i < charasJ.size(); i++) {
                SR4_Char chara = charRepo.getChar(charasJ.get(i).asInt()).toCompletableFuture().get();
                SR4_CharRecord record = new SR4_CharRecord(chara, combat);
                combatRepo.persist(record);
                combat.addRecord(record);
            }
            combatRepo.merge(combat);
        } catch (InterruptedException | ExecutionException ie) {
            return badRequest();
        }
        return ok();
    }

    public Result removeCharsFromCombat(Http.Request request) {
        JsonNode json = request.body().asJson();
        Integer combatId = json.findPath("combatId").asInt(0);
        JsonNode charasJ = json.findPath("sr4_chars");
        try {
            SR4_Combat combat = combatRepo.getCombat(combatId).toCompletableFuture().get();
            for (int i = 0; i < charasJ.size(); i++) {
                int charId = charasJ.get(i).asInt();
                int recordId = charRepo.getRecordId( charId, combatId ).toCompletableFuture().get();
                combatRepo.remove( recordId );
            }
        } catch (InterruptedException | ExecutionException ie) {
            return badRequest();
        }
        return ok();
    }

    public Result addCombat(Http.Request request) {
        JsonNode json = request.body().asJson();
        String combat_desc = json.get("0").asText();
        SR4_Combat combat = new SR4_Combat(combat_desc);
        combatRepo.persist(combat);
        return ok();
    }

    /**
     * Answers a Request by sending a JSON that contains
     * the Ids of all combats
     */
    public CompletionStage<Result> getCombats() {
        return combatRepo
            .allCombats()
            .thenApplyAsync(
                c -> ok(
                    Json.toJson(c)
                ),
                ec.current()
            );
    }

    /** Answers a Request by sending a JSON that contains
     * an object of class `SR4Char` that is identified by `charId` */
    public CompletionStage<Result> getChar(
        Integer charId
    ) {
        return combatRepo
            .getCombat( charId )
            .thenApplyAsync(
                c -> ok(
                    Json.toJson( c )
                ),
                ec.current()
            );
    }

    /** Answers a Request by sending a JSON that contains
     * an object of class `CharRecord` that is identified by `charId` */
    public CompletionStage<Result> getRecord(
        Integer recordId
    ) {
        return combatRepo
            .getRecord( recordId )
            .thenApplyAsync(
                c -> ok(
                    Json.toJson( c )
                ),
                ec.current()
            );
    }

    /** Answers a Request by sending a JSON that contains
     * an object of class `Combat` that is identified by `charId` */
    public CompletionStage<Result> getCombat(
        Integer combatId
    ) {
        return combatRepo
            .getCombat( combatId )
            .thenApplyAsync(
                c -> ok(
                    Json.toJson( c )
                ),
                ec.current()
            );
    }
    public Result getIniList(
        Http.Request request
    ) {
        DynamicForm form = formF.form().bindFromRequest( request );
        int combatId = Integer.parseInt( form.get( "combatId" ) );
        try {
            List<SR4_CharRecord> list = combatRepo.iniList(combatId).toCompletableFuture().get();
            return ok( Json.toJson( list ));
        } catch( IllegalArgumentException | ExecutionException | InterruptedException e) {
            return badRequest();
        }
    }
    public CompletionStage<Result> getDiceRolls(
        Http.Request request
    ) {
        DynamicForm form = formF.form().bindFromRequest( request );
        int combatId = Integer.parseInt( form.get("id") );
        String timeString = form.get("timestamp");
        LocalDateTime timestamp;
        try {
            timestamp = LocalDateTime.parse(timeString);
        } catch ( NullPointerException | DateTimeParseException e ) {
            timestamp = LocalDateTime.now().minus( 15, ChronoUnit.MINUTES );
        }
        String time = LocalDateTime.now().minus( 30, ChronoUnit.SECONDS ).toString();
        return diceRepo.getLastNDiceRolls(combatId, timestamp )
            .thenApplyAsync(
                c -> ok(
                    myToJason( c, time )
                ),
                ec.current()
            );
    }

    private String myToJason ( List<SR4_DiceRoll> list, String time ) {
        StringBuilder ret = new StringBuilder("{ \"time\":");
        ret.append("\"").append( time ).append("\",\"rolls\": {");
        for( int i = 0; i < list.size(); i++ ) {
            ret.append("\"").append(i).append("\":");
            ret.append( list.get(i).toJson() );
            if ( i != list.size() - 1 ) {
                ret.append(",");
            }
        }
        return ret + "}}";
    }

}