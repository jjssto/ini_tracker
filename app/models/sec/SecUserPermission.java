package models.sec;

import be.objectify.deadbolt.java.models.Permission;

import javax.persistence.*;

@Entity
@Table( name = "permission" )
public class SecUserPermission
    implements Permission {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id" )
    private int id;

    @Column( name = "value" )
    private String value;


    public SecUserPermission() {
        this.value = null;
    }
    public SecUserPermission( String value ) {
        this.value = value;
    }



    @Override
    public String getValue() {
        return value;
    }

    public void setValue( String value ) {
        this.value = value;
    }
}
