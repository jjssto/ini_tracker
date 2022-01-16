package controllers;

import models.*;

import play.mvc.Controller;
import play.mvc.Result;
import play.libs.concurrent.HttpExecutionContext;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import com.google.gson.Gson;

import play.libs.Json;

/**
 * ApiController contains methods to request
 * JSON datasets and manipulate the data displayed.
 */
public class JsonController extends Controller {

    private final Repository repo;
    private final HttpExecutionContext ec;

    @Inject
    public JsonController(
        Repository  repo,
        HttpExecutionContext ec
    ) {
        this.repo = repo;
        this.ec = ec;
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
}