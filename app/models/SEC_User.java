package models;

import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;
import net.bytebuddy.utility.RandomString;
import play.shaded.oauth.org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

@Entity
@Table( name = "user" )
public class SEC_User implements Subject {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id" )
    private int id;

    @Column( name = "user_name" )
    private String userName;

    @ManyToMany
    @JoinTable(
        name = "user_permission",
        joinColumns = @JoinColumn( name = "user_id" ),
        inverseJoinColumns = @JoinColumn( name = "permission_id")
    )
    private List<SEC_UserPermission> permissions;

    @ManyToMany
    @JoinTable(
        name = "user_security_role",
        joinColumns = @JoinColumn( name = "user_id" ),
        inverseJoinColumns = @JoinColumn( name = "security_role_id")
    )
    private List<SEC_SecurityRole> securityRoles;

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

    public void setPermissions(List<SEC_UserPermission> permissions) {
        this.permissions = permissions;
    }

    public SEC_User() {
    }

    public SEC_User(
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

    public List<SEC_SecurityRole> getSecurityRoles() {
        return securityRoles;
    }

    public void setSecurityRoles(List<SEC_SecurityRole> securityRoles) {
        this.securityRoles = securityRoles;
    }

    @Override
    public List<? extends Role> getRoles() {
        return securityRoles;
    }

    @Override
    public List<? extends Permission> getPermissions() {
        return permissions;
    }

    @Override
    public String getIdentifier() {
        return userName;
    }

    private void createSalt() {
        RandomString randomString = new RandomString( 32 );
        this.salt = randomString.nextString();
    }
}
