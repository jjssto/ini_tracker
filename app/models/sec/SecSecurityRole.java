package models.sec;

import be.objectify.deadbolt.java.models.Role;

import javax.persistence.*;

@Entity
@Table( name = "security_role" )
public class SecSecurityRole
    implements Role {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id" )
    private int id;

    @Column( name = "role_name" )
    private String roleName;

    public SecSecurityRole() {
        roleName = null;
    }

    public SecSecurityRole( String roleName ) {
        this.roleName = roleName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String getName() {
        return roleName;
    }

    @Override
    public String toString() {
        return getName();
    }
}
