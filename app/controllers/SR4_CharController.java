package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import models.db.sr4.DB_SR4_CharRepository;
import models.db.sr4.DB_SR4_DiceRepository;
import models.sr4.SR4_Char;
import play.api.i18n.MessagesApi;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@SubjectPresent
public class SR4_CharController extends Controller {

    private final DB_SR4_CharRepository charRepo;
    private final DB_SR4_DiceRepository diceRepo;
    private final HttpExecutionContext ec;
    private final FormFactory formF;
    private final MessagesApi messagesApi;

    @Inject
    public SR4_CharController(
        FormFactory formF,
        DB_SR4_CharRepository charRepo,
        DB_SR4_DiceRepository diceRepo,
        HttpExecutionContext ec,
        MessagesApi messagesApi
    ) {
        this.formF = formF;
        this.charRepo = charRepo;
        this.diceRepo = diceRepo;
        this.ec = ec;
        this.messagesApi = messagesApi;
    }

    @Restrict( @Group("admin"))
    public Result chars( Http.Request request ) {
        return ok( views.html.sr4_chars.render(
            request,
            messagesApi.preferred(request)
        ) );
    }

    @Restrict( @Group("admin"))
    public CompletionStage<Result> addChar(Http.Request request) {
        DynamicForm charForm = formF.form().bindFromRequest(request);
        SR4_Char chara = new SR4_Char();
        chara.setName(charForm.get("name"));
        try {
            chara.setIni(Integer.valueOf(charForm.get("ini")));
        } catch (NumberFormatException e1) {
            chara.setIni(0);
        }
        try {
            chara.setReaction(Integer.valueOf(charForm.get("reaction")));
        } catch (NumberFormatException e1) {
            chara.setReaction(0);
        }
        try {
            chara.setIntuition(Integer.valueOf(charForm.get("intuition")));
        } catch (NumberFormatException e1) {
            chara.setIntuition(0);
        }
        try {
            chara.setSBoxes(Integer.valueOf(charForm.get("sBoxes")));
        } catch (NumberFormatException e1) {
            chara.setSBoxes(0);
        }
        try {
            chara.setPBoxes(Integer.valueOf(charForm.get("pBoxes")));
        } catch (NumberFormatException e1) {
            chara.setPBoxes(0);
        }
        if ( charForm.get( "pc" ).equals( 'j' )  ) {
            chara.setPc( true );
        } else {
            chara.setPc( false);
        }
        return charRepo.persist(chara).thenApplyAsync(
            cha -> ok(),
            ec.current()
        );
    }

    /**
     *
     * Answers a Request by sending a JSON that contains
     * all Characters
     */
    public CompletionStage<Result> getAllChars() {
        return charRepo
            .allChars()
            .thenApplyAsync(
                combatList -> ok(
                    Json.toJson(combatList)
                ),
                ec.current()
            );
    }

    public CompletionStage<Result> getOtherChars(Integer combatId) {
        return charRepo
            .allOthers(combatId)
            .thenApplyAsync(
                charList -> ok(
                    Json.toJson(charList)
                ),
                ec.current()
            );
    }

    public CompletionStage<Result> getCombatChars(Integer combatId) {
        return charRepo
            .inCombat(combatId)
            .thenApplyAsync(
                charList -> ok(
                    Json.toJson(charList)
                ),
                ec.current()
            );
    }

}