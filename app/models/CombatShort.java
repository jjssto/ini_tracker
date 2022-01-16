package models;

import javax.persistence.*;

@Entity
@Table( name = "combat" )
public class CombatShort extends AbstractCombat {
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
