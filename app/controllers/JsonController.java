package controllers;

import models.*;

import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.libs.concurrent.HttpExecutionContext;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;

import play.libs.Json;

/**
 * ApiController contains methods to request
 * JSON datasets and manipulate the data displayed.
 */
public class JsonController extends Controller {

    private final CharRepository charRepo;
    private final CombatRepository combatRepo;
    private final DiceRepository diceRepo;
    private final HttpExecutionContext ec;
    private final FormFactory formF;

    @Inject
    public JsonController(
        CharRepository charRepo,
        DiceRepository diceRepo,
        CombatRepository combatRepo,
        HttpExecutionContext ec,
        FormFactory formF
    ) {
        this.charRepo = charRepo;
        this.diceRepo = diceRepo;
        this.combatRepo = combatRepo;
        this.ec = ec;
        this.formF = formF;
    }

    /** Answers a Request by sending a JSON that contains
     * all Characters */
    public CompletionStage<Result> getAllChars() {
        return charRepo
            .allChars()
            .thenApplyAsync(
                combatList -> ok(
                    //Json.toJson(combatStream.collect(Collectors.toList()))
                    Json.toJson( combatList )
                ),
                ec.current()
            );
    }
    public CompletionStage<Result> getOtherChars( Integer combatId ) {
        return charRepo
            .allOthers( combatId )
            .thenApplyAsync(
                charList -> ok(
                    Json.toJson( charList )
                ),
                ec.current()
            );
    }

    public CompletionStage<Result> getCombatChars( Integer combatId ) {
        return charRepo
            .inCombat( combatId )
            .thenApplyAsync(
                charList -> ok(
                    Json.toJson( charList )
                ),
                ec.current()
            );
    }


    /** Answers a Request by sending a JSON that contains
     * the Ids of all combats */
    public CompletionStage<Result> getCombats() {
        return combatRepo
            .allCombats()
            .thenApplyAsync(
                c -> ok(
                    Json.toJson( c )
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
    public CompletionStage<Result> getIniList(
        Integer combatId
    ) {
        return combatRepo
            .iniList( combatId )
            .thenApplyAsync(
                c -> ok(
                    Json.toJson( c )
                ),
                ec.current()
            );
    }
    public CompletionStage<Result> getDiceRolls(
        Http.Request request
    ) {
        DynamicForm form = formF.form().bindFromRequest( request );
        int combatId = Integer.parseInt( form.get("id") );
        String timeString = form.get("timestamp");
        LocalDateTime timestamp;
        if ( timeString.equals( "" ) ) {
            timestamp = LocalDateTime.now().minus( 15, ChronoUnit.MINUTES );
        } else {
            timestamp = LocalDateTime.parse( timeString );
        }
        return diceRepo.getLastNDiceRolls(combatId, timestamp )
            .thenApplyAsync(
                c -> ok(
                    myToJason( c )
                ),
                ec.current()
            );
    }

    private String myToJason (List<DiceRoll> list ) {
        StringBuilder ret = new StringBuilder("{");
        for( int i = 0; i < list.size(); i++ ) {
            ret.append("\"").append(i).append("\":");
            ret.append( list.get(i).toJson() );
            if ( i != list.size() - 1 ) {
                ret.append(",");
            }
        }
        return ret + "}";
    }
}