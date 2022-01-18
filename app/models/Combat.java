package models;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table( name = "combat")
public class Combat extends AbstractCombat {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="combat", cascade = CascadeType.ALL)
    private List<CharRecord> charas;

    @Column( name ="combat_desc" )
    private String description;

    public Combat( Integer combatId ) {
        id = combatId;
        charas = new ArrayList<CharRecord>();
        description = "";
    }
    public Combat() {
        this( null );
    }




   public void
   addRecord ( CharRecord record ) {
       charas.add( record );
   }



    public void setDescription( String description ) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public List<CharRecord> getCharas() {
        return charas;
    }
    public CharRecord getChar( Integer index ) {
        return charas.get( index );
    }

    public Integer getId() {
        return id;
    }


}
