package models.sr4;

import javax.persistence.*;

@Entity
@Table( name = "sr4_combat" )
public class SR4_CombatShort extends SR4_AbstractCombat {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;


    @Column( name ="combat_desc" )
    private String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }
}
