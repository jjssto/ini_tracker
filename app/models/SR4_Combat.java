package models;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table( name = "sr4_combat")
public class SR4_Combat extends SR4_AbstractCombat {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "combat" )
    private List<SR4_CharRecord> charas;

    @Column( name ="combat_desc" )
    private String description;

    public SR4_Combat( Integer combatId ) {
        id = combatId;
        charas = new ArrayList<>();
        description = "";
    }
    public SR4_Combat() {
        id = null;
        charas = new ArrayList<>();
        description = "";
    }

    public SR4_Combat( String description ) {
        this.id = null;
        this.charas = new ArrayList<>();
        this.description = description;
    }



   public void
   addRecord ( SR4_CharRecord record ) {
       charas.add( record );
   }

   public void removeRecord( Integer charId ) {
        for ( int i = 0; i < charas.size(); i++ ) {
            if ( charas.get(i).getCharId().equals( charId ) ) {
                SR4_CharRecord record = charas.get(i);
                charas.remove( record );
            }
        }
   }


    public void setDescription( String description ) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public List<SR4_CharRecord> getCharas() {
        return charas;
    }
    public SR4_CharRecord getChar( Integer index ) {
        return charas.get( index );
    }

    public Integer getId() {
        return id;
    }
}
