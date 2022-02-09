import models.SEC_User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class SECITUserTest {

    private SEC_User user;

    @Before
    public void createUser() {
        user = new SEC_User( "user", "password" );
    }

    @Test
    public void checkGetUserName() {
        String userName = user.getUserName();
        assertEquals( userName, "user" );
    }

    @Test
    public void checkPassword() {
       assertTrue( user.checkPassword( "password" ));
    }
}
