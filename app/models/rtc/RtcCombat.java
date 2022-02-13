package models.rtc;

import models.sec.SEC_SecurityRole;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table( name = "rtc_combat" )
public class RtcCombat {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "id" )
    private int id;

    @Column( name = "combat_name" )
    private String name;

    @Column( name = "last_changed" )
    @UpdateTimestamp
    private LocalDateTime lastChanged;

    @ManyToOne
    @JoinTable(
        name = "rtc_combat_security_role",
        joinColumns = @JoinColumn( name = "combat_id"),
        inverseJoinColumns = @JoinColumn( name = "role_id")
    )
    private SEC_SecurityRole securityRole;

    public RtcCombat(
        String name,
        SEC_SecurityRole securityRole
    ){
        this.name = name;
        this.securityRole = securityRole;
    }

    public RtcCombat() {
        this( null, null);
    }

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
