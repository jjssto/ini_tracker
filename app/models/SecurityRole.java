package models;

import be.objectify.deadbolt.java.models.Role;

import javax.persistence.*;
import java.util.List;

@Entity
@Table( name = "security_role")
public class SecurityRole implements Role
{
    @Id
    @Column( name="id")
    private int id;

    @Column( name="role_name" )
    private String name;

    @ManyToMany( mappedBy = "roles" )
    private List<AuthorisedUser> authorisedUsers;

    public String getName()
    {
        return name;
    }

    public static SecurityRole findByName(String name)
    {
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AuthorisedUser> getAuthorisedUsers() {
        return authorisedUsers;
    }

    public void setAuthorisedUsers(List<AuthorisedUser> authorisedUsers) {
        this.authorisedUsers = authorisedUsers;
    }
}