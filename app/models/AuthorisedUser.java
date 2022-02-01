package models;


import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;

import javax.persistence.*;
import java.util.List;

@Entity
@Table( name = "user" )
public class AuthorisedUser implements Subject
{
    @Id
    @Column( name = "id" )
    private int id;

    @Column( name = "user_name" )
    private String userName;

    @ManyToMany
    @JoinTable(
        name = "user_permission",
        joinColumns = @JoinColumn( name = "user_id", referencedColumnName = "id" ),
        inverseJoinColumns = @JoinColumn( name = "permission_id", referencedColumnName = "id" )
    )
    private List<UserPermission> permissions;

    @ManyToMany
    @JoinTable(
        name = "user_security_role",
        joinColumns = @JoinColumn( name = "user_id" ),
        inverseJoinColumns = @JoinColumn( name = "security_role_id")
    )
    private List<SecurityRole> roles;

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

    public void setPermissions(List<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public void setRoles(List<SecurityRole> roles) {
        this.roles = roles;
    }

    @Override
    public List<? extends Role> getRoles()
    {
        return roles;
    }

    @Override
    public List<? extends Permission> getPermissions()
    {
        return permissions;
    }

    @Override
    public String getIdentifier()
    {
        return userName;
    }
}