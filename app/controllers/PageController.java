package controllers;

import models.*;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javax.inject.Inject;
import play.api.i18n.MessagesApi;

/**
 * Contains methods to display pages
 */
public class PageController extends Controller {

    private final Repository repo;
    private final HttpExecutionContext ec;
    private final FormFactory formF;
    private final MessagesApi messagesApi;

    @Inject
    public PageController(
        FormFactory formF,
        Repository  repo,
        HttpExecutionContext ec,
        MessagesApi messagesApi
    ) {
        this.formF = formF;
        this.repo = repo;
        this.ec = ec;
        this.messagesApi = messagesApi;
    }


    /** Displays page that contains a list of available combats */
    public Result index() {
        return ok( views.html.index.render() );

    }

    /** Displays page that contains a list of available combats */
    public Result chars( Http.Request request ) {
        return ok( views.html.chars.render(
            request,
            messagesApi.preferred(request)
        ) );
    }

    public Result combat(
        Integer combatId,
        Http.Request request
    ) {
        return ok(views.html.combat.render(
            combatId,
            request,
            messagesApi.preferred( request )));
    }
}
