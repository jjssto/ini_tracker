package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.api.i18n.MessagesApi;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.inject.Inject;
import play.api.i18n.MessagesApi;


/**
 * Contains methods for user input
 */
public class FormController extends Controller {

    private final Repository repo;
    private final HttpExecutionContext ec;
    private final FormFactory formF;
    private final MessagesApi messagesApi;
    private Http.Request request;

    @Inject
    public FormController(
        FormFactory formF,
        Repository repo,
        HttpExecutionContext ec,
        MessagesApi messagesApi
    ) {
        this.formF = formF;
        this.repo = repo;
        this.ec = ec;
        this.messagesApi = messagesApi;
    }

    public Result combat( Http.Request request ) {
        return redirect( "/combat/1" );
    }

    public Result updateCombat( Http.Request request ) {
        JsonNode json = request.body().asJson();
        if ( json == null ) {
            return badRequest( "Expecting Json data" );
        }
        Integer id = json.findPath("id").asInt(0);
        Integer sDmg = json.findPath("sDmg").asInt(-1);
        Integer pDmg = json.findPath("pDmg").asInt(-1);
        Integer localIni = json.findPath("localIni").asInt(0);
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
            record.setLocalIni( localIni );
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
            combat -> combat.rollInitiative(),
            ec.current()
        ).thenApplyAsync( combat -> repo.update( combat ), ec.current() );
        return ok("OK");
    }
}