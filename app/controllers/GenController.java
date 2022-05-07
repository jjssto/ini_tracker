package controllers;

import models.db.gen.DB_GEN_DiceRollRepository;
import models.gen.GEN_DiceRolls;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

public class GenController
    extends Controller {

    private final FormFactory formFactory;
    private final HttpExecutionContext ec;
    private final DB_GEN_DiceRollRepository diceRollRepo;

    @Inject
    public GenController(
        FormFactory formFactory,
        HttpExecutionContext ec,
        DB_GEN_DiceRollRepository diceRollRepo
    ) {
        this.formFactory = formFactory;
        this.ec = ec;
        this.diceRollRepo = diceRollRepo;
    }

    public Result index( Http.RequestHeader requestHeader ) {
        return ok( views.html.gen.gen_diceroller.render( requestHeader ) );
    }

    public Result roll( Http.Request request ) {
        GEN_DiceRolls diceRolls = new GEN_DiceRolls();
        DynamicForm form = formFactory.form().bindFromRequest( request );
        try {
            diceRolls.setNbrD4( Integer.parseInt(  form.get( "d4" ) ) );
        } catch ( NumberFormatException e ) {}
        try {
            diceRolls.setNbrD6( Integer.parseInt(  form.get( "d6" ) ) );
        } catch ( NumberFormatException e ) {}
        try {
            diceRolls.setNbrD8( Integer.parseInt(  form.get( "d8" ) ) );
        } catch ( NumberFormatException e ) {}
        try {
            diceRolls.setNbrD10( Integer.parseInt(  form.get( "d10" ) ) );
        } catch ( NumberFormatException e ) {}
        try {
            diceRolls.setNbrD12( Integer.parseInt(  form.get( "d12" ) ) );
        } catch ( NumberFormatException e ) {}
        try {
            diceRolls.setNbrD20( Integer.parseInt(  form.get( "d20" ) ) );
        } catch ( NumberFormatException e ) {}

        diceRolls.roll();
        return ok( diceRolls.toJson() );
    }
}
