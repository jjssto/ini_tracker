package models.rtc;

import javax.persistence.*;

@Entity
@Table( name = "rtc_combat" )
public class RTC_Combat {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id" )
    private int id;

    @Column( name = "combat_name" )
    private String name;

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }
}
