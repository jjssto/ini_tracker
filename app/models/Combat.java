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
        charas = new ArrayList<>();
        description = "";
    }
    public Combat() {
        id = null;
        charas = new ArrayList<>();
        description = "";
    }

    public Combat( String description ) {
        this.id = null;
        this.charas = new ArrayList<>();
        this.description = description;
    }



   public void
   addRecord ( CharRecord record ) {
       charas.add( record );
   }

   public void removeRecord( Integer charId ) {
        for ( int i = 0; i < charas.size(); i++ ) {
            if ( charas.get(i).getCharId().equals( charId ) ) {
                CharRecord record = charas.get(i);
                charas.remove( i );
            }
        }
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
