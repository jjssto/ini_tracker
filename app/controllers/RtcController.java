package controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.JsonNode;
import models.db.rtc.DbRtcDiceRollRepository;
import models.db.rtc.DbRtcCombatRepo;
import models.db.sec.DB_SEC_UserRepository;
import models.rtc.RtcCombat;
import models.rtc.RtcDiceRolls;
import models.sec.SEC_User;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import security.MyDeadboltHandler;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class RtcController
    extends Controller {

    private final FormFactory formFactory;
    private final HttpExecutionContext ec;
    private final MyDeadboltHandler dh;
    private final DbRtcDiceRollRepository diceRollRepo;
    private final DbRtcCombatRepo combatRepo;
    private final DB_SEC_UserRepository userRepo;

    @Inject
    public RtcController(
        FormFactory formFactory,
        HttpExecutionContext ec,
        MyDeadboltHandler dh,
        DbRtcDiceRollRepository diceRollRepo,
        DB_SEC_UserRepository userRpo,
        DbRtcCombatRepo combatRepo
    ) {
        this.formFactory = formFactory;
        this.ec = ec;
        this.dh = dh;
        this.diceRollRepo = diceRollRepo;
        this.userRepo = userRpo;
        this.combatRepo = combatRepo;
    }

    public Result index( Http.RequestHeader requestHeader ) {
        return ok( views.html.rtc_diceroller.render( requestHeader ) );
    }

    @Restrict( @Group( "rtc") )
    public CompletionStage<Result> getCombat( Integer combatId, Http.Request request ) {
        return combatRepo.getById( combatId ).thenApplyAsync(
            combat -> ok(
                views.html.rtc_diceroller_i.render( combat.getName(), request )
            ),
            ec.current()
        );
    }

    public Result roll( Http.Request request ) {
        SEC_User user;
        DynamicForm form = formFactory.form().bindFromRequest( request );
        RtcDiceRolls diceRolls = null;
        int combatId;
        try {
            combatId = Integer.parseInt( form.get( "combatId" ) );
        } catch ( NumberFormatException e ) {
            combatId = 0;
        }
        if ( combatId > 0 ) {
            try {
                RtcCombat combat = combatRepo.getById( combatId ).toCompletableFuture().get();
                user = userRepo.findByRequest( request ).toCompletableFuture().get();
                if ( user != null ) {
                    diceRolls = new RtcDiceRolls( combat, user );
                }
            } catch ( InterruptedException | ExecutionException e ) {
                return badRequest();
            }
        } else {
            diceRolls = new RtcDiceRolls();
        }
        try {
            diceRolls.setSkill( Integer.parseInt(  form.get( "skill" ) ) );
        } catch ( NumberFormatException e ) {
            return badRequest();
        }
        try {
            diceRolls.setAttribute( Integer.parseInt(  form.get( "attribute" ) ) );
        } catch ( NumberFormatException e ) {
            return badRequest();
        }

        boolean noTag;
        if ( form.get( "noTag").equals( "j" ) ) {
            noTag = true;
        } else {
            noTag = false;
        }
        diceRolls.setNoTag( noTag );
        diceRolls.roll();
        if ( combatId > 0 ) {
            diceRollRepo.merge( diceRolls );
        }
        return ok( diceRolls.toJson() );
    }

    public CompletionStage<Result> getCombats ( Http.RequestHeader requestHeader ){
        String loginToken;
        try {
            loginToken = requestHeader.session().get( "LOGIN_TOKEN" ).map( token -> { return token; } ).get();
        } catch ( Exception e ) {
            loginToken = "";
        }

        SEC_User user;
        try {
            user = userRepo.findByToken( loginToken ).toCompletableFuture().get();
        } catch ( InterruptedException | ExecutionException e ) {
            return null;
        }
        return combatRepo.getByUser( user ).thenApplyAsync(
            combatList -> {
                JsonNode json = Json.toJson( combatList );
                return ok( json );
            },
            ec.current()
        );
    }

    public CompletionStage<Result> getRolls( Integer combatId, Http.Request request ) {
        DynamicForm form = formFactory.form().bindFromRequest( request );
        LocalDateTime timestamp;
        try {
            timestamp = LocalDateTime.parse( form.get( "timestamp" ) );
        } catch( DateTimeParseException e ) {
            timestamp = LocalDateTime.now().minusMinutes( 15 );
        }
        return diceRollRepo.getRollsJson( combatId, timestamp ).thenApplyAsync(
            json -> ok(  json ),
            ec.current()
        );
    }


}
