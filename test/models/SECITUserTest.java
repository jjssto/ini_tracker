package models;

import models.sec.SecSecurityRole;
import models.sec.SecUser;
import models.sec.SecUserPermission;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class SECITUserTest {

    private SecUser user;
    private SecUserPermission permission;
    private SecSecurityRole role;
    private final String userName = "test_user";
    private final String pw = "dfasfda";

    @Before
    public void setup() {
       permission = new SecUserPermission( "test_permission" );
       role = new SecSecurityRole( "test_role" );
       user = new SecUser( userName, pw );
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
