package models;

import be.objectify.deadbolt.java.models.Permission;

import javax.persistence.*;
import java.util.List;

@Entity
@Table( name = "permission" )
public class UserPermission implements Permission
{
    @Id
    @Column( name = "id" )
    private int id;

    @Column( name = "value" )
    private String value;

    @ManyToMany( mappedBy = "permissions" )
    private List<AuthorisedUser> authorisedUsers;

    public String getValue()
    {
        return value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<AuthorisedUser> getAuthorisedUsers() {
        return authorisedUsers;
    }

    public void setAuthorisedUsers(List<AuthorisedUser> authorisedUsers) {
        this.authorisedUsers = authorisedUsers;
    }

    public static UserPermission findByValue(String value)
    {
        return null;
    }
}