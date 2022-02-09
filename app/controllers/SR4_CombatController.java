package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
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
import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class SR4_CombatController extends Controller {

    private final DB_SR4_CharRepository charRepo;
    private final DB_SR4_CharRecordRepo recordRepo;
    private final DB_SR4_CombatRepo combatRepo;
    private final DB_SR4_DiceRepository diceRepo;
    private final HttpExecutionContext ec;
    private final FormFactory formF;
    private final MessagesApi messagesApi;

    @Inject
    public SR4_CombatController(
        FormFactory formF,
        DB_SR4_CharRepository charRepo,
        DB_SR4_CharRecordRepo recordRepo,
        DB_SR4_CombatRepo combatRepo,
        DB_SR4_DiceRepository diceRepo,
        HttpExecutionContext ec,
        MessagesApi messagesApi
    ) {
        this.formF = formF;
        this.charRepo = charRepo;
        this.recordRepo = recordRepo;
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

    public CompletionStage<Result> index( Http.Request request ) {
        return combatRepo.getAllCombats().thenApplyAsync(
            list -> {
                return ok( views.html.sr4_overview.render( list ) );
            }
        );
    }


    public Result updateCombat(Http.Request request) {
        DynamicForm form = formF.form().bindFromRequest( request );
        int id;
        try {
            id = Integer.parseInt( form.get( "id" ) );
        } catch( NumberFormatException e ) {
            return badRequest();
        }
        int sDmg;
        try {
            sDmg = Integer.parseInt( form.get( "sDmg" ) );
        } catch ( NumberFormatException e ) {
            sDmg = -1;
        }
        int pDmg;
        try {
            pDmg = Integer.parseInt( form.get( "pDmg" ) );
        } catch ( NumberFormatException e ) {
            pDmg = -1;
        }
        int localIni;
        try {
            localIni = Integer.parseInt( form.get( "localIni" ) );
        } catch ( NumberFormatException e ) {
            localIni = -1;
        }
        SR4_CharRecord record;
        try {
            record = recordRepo.getCharRecord(id).toCompletableFuture().get();
            if (sDmg != -1) {
                record.setSDmg(sDmg);
            }
            if (pDmg != -1) {
                record.setPDmg(pDmg);
            }
            if (localIni != -1) {
                record.setLocalIni(localIni);
            }
            recordRepo.merge( record );
        } catch (InterruptedException | ExecutionException | PersistenceException ie) {
            return badRequest();
        }
        combatRepo.getCombat( record.getCombatId() ).thenApplyAsync(
            combat -> {
                combat.setLastChanged();
                combatRepo.merge( combat );
                return 1;
            },
            ec.current()
        );
        return ok(views.html.sr4_combat.render(
            record.getCombatId(),
            request,
            messagesApi.preferred(request)
        ));
    }

    @SubjectPresent
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
                    diceRepo.merge(diceRoll);
                }
                combat.setLastChanged();
                return combat;
            }, ec.current()
        ).thenApplyAsync(
            combat -> {
                combat.setLastChanged();
                return combat;
            },
            ec.current()
        ).thenApplyAsync(
            combat -> {
                return combatRepo.merge( combat );
            }
        );
        combatRepo.flush();
        diceRepo.flush();
        return ok("OK");
    }

    public Result roll(Http.Request request) {
        DynamicForm recordForm = formF.form().bindFromRequest(request);
        int recordId = Integer.parseInt(recordForm.get("recordId"));
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
            charRecord = recordRepo.getCharRecord( recordId ).toCompletableFuture().get();
        } catch (InterruptedException | ExecutionException ie) {
            return badRequest();
        }
        SR4_DiceRoll diceRoll = new SR4_DiceRoll(6, charRecord);
        if ( exploding ) {
            diceRoll.explode(nbrOfDice);
        } else {
            diceRoll.roll(nbrOfDice);
        }
        diceRepo.merge( diceRoll );
        return ok( diceRoll.toJson() );
    }


    @SubjectPresent
    public Result addCharToCombat(Http.Request request) {
        DynamicForm form = formF.form().bindFromRequest( request );
        int charId = Integer.parseInt( form.get( "charId" ) );
        int combatId = Integer.parseInt( form.get( "combatId" ) );
        SR4_Combat combat;
        try {
            combat = combatRepo.getCombat( combatId ).toCompletableFuture().get();
        } catch ( ExecutionException | InterruptedException e) {
            return badRequest();
        }
        SR4_Char chara;
        try {
           chara = charRepo.getChar( charId ).toCompletableFuture().get();
        } catch ( ExecutionException | InterruptedException e) {
            return badRequest();
        }
        SR4_CharRecord record = new SR4_CharRecord( chara, combat );
        try {
            recordRepo.merge( record );
        } catch ( PersistenceException e ) {
            return badRequest();
        }
        combatRepo.getCombat( combatId ).thenApplyAsync(
            c -> {
                c.setLastChanged();
                return c;
            },
            ec.current()
        ).thenApplyAsync(
            c -> {
                return combatRepo.merge( c );
            }
        );
        return ok();
    }

    @SubjectPresent
    public Result removeCharFromCombat(Http.Request request) {
        DynamicForm form = formF.form().bindFromRequest( request );
        int combatId = Integer.parseInt( form.get( "combatId" ) );
        int charRecordId = Integer.parseInt( form.get( "recordId" ) );
        combatRepo.getCombat( combatId ).thenApplyAsync(
            c -> {
                c.setLastChanged();
                return c;
            },
            ec.current()
        ).thenApplyAsync(
            c -> {
                return combatRepo.merge( c );
            }
        );
        recordRepo.remove( charRecordId );
        return ok();
    }

    @SubjectPresent
    public Result addCombat(Http.Request request) {
        DynamicForm form = formF.form().bindFromRequest( request );
        String desc = form.get( "bez" );
        SR4_Combat combat = new SR4_Combat( desc );
        try {
            combatRepo.persist( combat );
            return ok( "{\"id\":\"" + combat.getId() + "\",\"bez\":\"" + combat.getDescription() + "\"}");
        } catch ( EntityExistsException e ) {
            return badRequest( "Entity already exists!");
        }
    }

    /**
     * Answers a Request by sending a JSON that contains
     * the Ids of all combats
     */
    public CompletionStage<Result> getCombats() {
        return combatRepo
            .getAllCombats()
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
        return recordRepo
            .getCharRecord( recordId )
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
        LocalDateTime timestamp;
        try {
            timestamp = LocalDateTime.parse( form.get( "timestamp" ) );
        } catch ( DateTimeParseException | NullPointerException e ) {
            timestamp = LocalDateTime.now().minusYears( 100 );
        }
        SR4_Combat combat;
        try {
            combat = combatRepo.getCombat( combatId )
                .toCompletableFuture().get();
        } catch( CancellationException | ExecutionException | InterruptedException e) {
            return badRequest();
        }
        if ( combat.getLastChanged().compareTo(  timestamp ) > 0 ){
            combat.sort();
            return ok( Json.toJson( combat ) );
        } else {
            return ok();
        }
    }
    public Result getDiceRolls(
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
        List<SR4_DiceRoll> list = null;
        try {
            list = diceRepo.getDiceRollsSince( combatId, timestamp );
            return ok( myToJason( list, time ) );
        } catch ( NullPointerException e ) {
            return ok();
        }

    }

    private String myToJason ( List<SR4_DiceRoll> list, String time ) {
        StringBuilder ret = new StringBuilder("{ \"time\":");
        ret.append("\"").append( time ).append("\",\"rolls\": {");
        for ( int i = 0; i < list.size(); i++ ) {
            ret.append( "\"" ).append( i ).append( "\":" );
            ret.append( list.get( i ).toJson() );
            if ( i != list.size() - 1 ) {
                ret.append( "," );
            }
        }
        return ret + "}}";
    }

}