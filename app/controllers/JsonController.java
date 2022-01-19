package controllers;

import akka.http.javadsl.model.DateTime;
import models.*;

import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.libs.concurrent.HttpExecutionContext;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.regex.Pattern;
import javax.inject.Inject;
import com.google.gson.Gson;

import play.libs.Json;
import scala.concurrent.impl.FutureConvertersImpl;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * ApiController contains methods to request
 * JSON datasets and manipulate the data displayed.
 */
public class JsonController extends Controller {

    private final CharRepository repo;
    private final DiceRepository diceRepo;
    private final HttpExecutionContext ec;
    private final FormFactory formF;

    @Inject
    public JsonController(
        CharRepository repo,
        DiceRepository diceRepo,
        HttpExecutionContext ec,
        FormFactory formF
    ) {
        this.repo = repo;
        this.diceRepo = diceRepo;
        this.ec = ec;
        this.formF = formF;
    }

    /** Answers a Request by sending a JSON that contains
     * all Characters */
    public CompletionStage<Result> getAllChars() {
        return repo
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
        return repo
            .allOthers( combatId )
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
        Gson gson = new Gson();
        return repo
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
        return repo
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
        return repo
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
        return repo
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
        return repo
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
        Gson gson = new Gson();
        DynamicForm form = formF.form().bindFromRequest( request );
        int combatId = Integer.parseInt( form.get("id").toString() );
        String timeString = form.get("timestamp").toString();
        LocalDateTime timestamp;
        if ( timeString != "" ) {
            timestamp = LocalDateTime.parse(timeString);
        } else {
            timestamp = LocalDateTime.now().minus( 15, ChronoUnit.MINUTES );
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
        String ret = "{";
        for( Integer i = 0; i < list.size(); i++ ) {
            ret += "\"" + i.toString() + "\":";
            ret += list.get(i).toJson();
            if ( i != list.size() - 1 ) {
                ret += ",";
            }
        }
        return ret + "}";
    }
}