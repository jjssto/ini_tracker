package controllers;

import models.DB_RTC_DiceRollRepository;
import models.RTC_DiceRolls;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

public class RTC_Controller extends Controller {

    private final FormFactory formFactory;
    private final HttpExecutionContext ec;
    private final DB_RTC_DiceRollRepository diceRollRepo;

    @Inject
    public RTC_Controller(
        FormFactory formFactory,
        HttpExecutionContext ec,
        DB_RTC_DiceRollRepository diceRollRepo
    ) {
        this.formFactory = formFactory;
        this.ec = ec;
        this.diceRollRepo = diceRollRepo;
    }

    public Result index( Http.RequestHeader requestHeader ) {
        return ok( views.html.rtc_diceroller.render( requestHeader ) );
    }

    public Result roll( Http.Request request ) {
        RTC_DiceRolls diceRolls = new RTC_DiceRolls();
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
}
