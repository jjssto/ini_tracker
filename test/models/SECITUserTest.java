package models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SECITUserTest {

    private SEC_User user;
    private SEC_UserPermission permission;
    private SEC_SecurityRole role;
    private final String userName = "test_user";
    private final String pw = "dfasfda";

    @Before
    public void setup() {
       permission = new SEC_UserPermission( "test_permission" );
       role = new SEC_SecurityRole( "test_role" );
       user = new SEC_User( userName, pw );
       user.addPermission( permission );
       user.addRole( role );
    }


    @Test
    public void checkGetUserName() {
        String name = user.getIdentifier();
        assertEquals( name, userName );
    }

    @Test
    public void checkPassword() {
       assertTrue( user.checkPassword( pw ));
    }

    @Test
    public void checkGetRoles() {
        assertTrue( user.getRoles().contains( role ) );
    }

    @Test
    public void checkGetPermissions() {
        assertTrue( user.getPermissions().contains( permission ) );
    }
}
