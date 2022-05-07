package controllers;

import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;

public class MyController extends Controller {

    private FormFactory formFactory;

    public MyController( FormFactory formFactory ) {
        this.formFactory = formFactory;
    }

    protected int parseInt(
        String key,
        DynamicForm form
    ) {
        try {
            return Integer.parseInt( form.get( key ) );
        } catch ( Exception e ) {
            return 0;
        }
    }

    protected DynamicForm getForm(
        Http.Request request
    ) {
        return formFactory.form().bindFromRequest( request );
    }

}
