package models;

import be.objectify.deadbolt.java.models.Permission;

import javax.persistence.*;

@Entity
@Table( name = "permission" )
public class SEC_UserPermission implements Permission {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id" )
    private int id;

    @Column( name = "value" )
    private String value;

    @Override
    public String getValue() {
        return value;
    }
}
