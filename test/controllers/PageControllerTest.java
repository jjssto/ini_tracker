package controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.mvc.Http;
import play.test.Helpers;

import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class PageControllerTest {
    private Application fa;

    @Before
    public void setup() {
        fa = fakeApplication();
    }

    @Test
    public void index() {
        Http.RequestBuilder request = Helpers.fakeRequest().method(GET).uri("/");
        Assert.assertEquals( OK, route(fa, request).status() );
    }

    @Test
    public void loginPage() {
    }

    @Test
    public void login() {
    }

    @Test
    public void logout() {
    }

    @Test
    public void createUserPage() {
    }

    @Test
    public void userList() {
    }

    @Test
    public void newPassword() {
    }

    @Test
    public void passwordPage() {
    }

    @Test
    public void changePassword() {
    }

    @Test
    public void removeUser() {
    }

    @Test
    public void createUser() {
    }

    @Test
    public void getUser() {
    }
}