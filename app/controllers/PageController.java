package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * Contains methods to display pages
 */
public class PageController extends Controller {

    @Inject
    public PageController(
    ) {
    }

    /** Displays page that contains a list of available combats */
    public Result index() {
        return ok( views.html.index.render() );
    }
}
