package models.sec;

import be.objectify.deadbolt.java.models.Subject;
import net.bytebuddy.utility.RandomString;
import play.shaded.oauth.org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table( name = "user" )
public class SecUser
    implements Subject {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id" )
    private int id;

    @Column( name = "user_name" )
    private String userName;

    @ManyToMany()
    @JoinTable(
        name = "user_permission",
        joinColumns = @JoinColumn( name = "user_id" ),
        inverseJoinColumns = @JoinColumn( name = "permission_id")
    )
    private List<SecUserPermission> permissions;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(
        name = "user_security_role",
        joinColumns = @JoinColumn( name = "user_id" ),
        inverseJoinColumns = @JoinColumn( name = "security_role_id")
    )
    private List<SecSecurityRole> securityRoles;

    @Column( name = "password_hash")
    @Lob
    private byte[] passwordHash;

    @Column( name = "salt")
    private String salt;

    @Transient
    private static MessageDigest digest;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPermissions(List<SecUserPermission> permissions) {
        this.permissions = permissions;
    }

    public SecUser() {
        securityRoles = new ArrayList<>();
        permissions = new ArrayList<>();
    }

    public SecUser(
        String userName,
        String password
    ) {
        this();
        this.userName = userName;
        createSalt();
        changePassword( password );
    }


    public void changePassword( String password ) {
        this.passwordHash = DigestUtils.sha(
            password + salt
        );
    }

    public boolean checkPassword( String password ) {
        byte[] hash = DigestUtils.sha(
            password + salt
        );
        if ( Arrays.equals( this.passwordHash, hash ) )
            return true;
        else
            return false;
    }

    public List<SecSecurityRole> getSecurityRoles() {
        return securityRoles;
    }

    public void setSecurityRoles(List<SecSecurityRole> securityRoles) {
        this.securityRoles = securityRoles;
    }

    @Override
    public List<SecSecurityRole> getRoles() {
        return securityRoles;
    }

    public List<Integer> getRolesId() {
        List<Integer> list = new ArrayList<>();
        this.securityRoles.forEach(
            role -> list.add( role.getId() )
        );
        return list;
    }



    @Override
    public List<SecUserPermission> getPermissions() {
        //return permissions;
        return null;
    }

    @Override
    public String getIdentifier() {
        return userName;
    }

    private void createSalt() {
        RandomString randomString = new RandomString( 32 );
        this.salt = randomString.nextString();
    }

    public void addRole( SecSecurityRole role ) {
        securityRoles.add( role );
    }

    public void removeRole( SecSecurityRole role ) {
        securityRoles.remove( role );
    }

    public void addPermission( SecUserPermission permission ) {
        permissions.add( permission );
    }

    public void removePermission( SecUserPermission permission ) {
        permissions.remove( permission );
    }
}
