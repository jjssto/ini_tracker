package controllers;

import models.*;

import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.libs.concurrent.HttpExecutionContext;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Date;
import java.util.concurrent.CompletionStage;
import java.util.regex.Pattern;
import javax.inject.Inject;
import com.google.gson.Gson;

import play.libs.Json;

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
        DynamicForm form = formF.form().bindFromRequest( request );
        int combatId = Integer.parseInt( form.get("id").toString() );
        long timestamp = Long.parseLong( form.get("timestamp").toString() );
        return diceRepo.getLastNDiceRolls(combatId, timestamp )
            .thenApplyAsync(
                c -> ok(
                    Json.toJson( c )
                ),
                ec.current()
            );
    }
}