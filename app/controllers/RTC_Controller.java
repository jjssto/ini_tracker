package controllers;

import models.db.rtc.DB_RTC_DiceRollRepository;
import models.db.rtc.DbRtcCombatRepo;
import models.db.sec.DB_SEC_UserRepository;
import models.rtc.RtcDiceRolls;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class RTC_Controller extends Controller {

    private final FormFactory formFactory;
    private final HttpExecutionContext ec;
    private final DB_RTC_DiceRollRepository diceRollRepo;
    private final DbRtcCombatRepo combatRepo;
    private final DB_SEC_UserRepository userRepo;

    @Inject
    public RTC_Controller(
        FormFactory formFactory,
        HttpExecutionContext ec,
        DB_RTC_DiceRollRepository diceRollRepo,
        DB_SEC_UserRepository userRpo,
        DbRtcCombatRepo combatRepo
    ) {
        this.formFactory = formFactory;
        this.ec = ec;
        this.diceRollRepo = diceRollRepo;
        this.userRepo = userRpo;
        this.combatRepo = combatRepo;
    }

    public Result index( Http.RequestHeader requestHeader ) {
        return ok( views.html.rtc_diceroller.render( requestHeader ) );
    }

    public Result roll( Http.Request request ) {
        RtcDiceRolls diceRolls = new RtcDiceRolls();
        DynamicForm form = formFactory.form().bindFromRequest( request );
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
        return ok( diceRolls.toJson() );
    }

    public CompletionStage<Result> getCombats ( Http.Request request ){
        String token = request.session().get( "LOGIN_TOKEN" ).toString();
        return
            userRepo.findByToken( token ).thenApplyAsync(
            user -> {
                return ok( Json.toJson( combatRepo.getByUser( user ) ) );
            },
            ec.current()
        );
    }

    public Result getCombat( Http.Request request ) {
        return null;
    }
}
