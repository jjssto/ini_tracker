package models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    @Column( name = "last_changed" )
    private LocalDateTime lastChanged;

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



   public void addRecord ( SR4_CharRecord record ) {
       setLastChanged();
       charas.add( record );
   }

   public void removeRecord( Integer charId ) {
        for ( int i = 0; i < charas.size(); i++ ) {
            if ( charas.get(i).getCharId().equals( charId ) ) {
                SR4_CharRecord record = charas.get(i);
                charas.remove( record );
                setLastChanged();
            }
        }
   }

   public void sort() {
        RecordComparator comparator = new RecordComparator();
        charas.sort( comparator );
   }

    public void setDescription( String description ) {
        this.description = description;
        setLastChanged();
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

    public void setCharas( List<SR4_CharRecord> charas ) {
        this.charas = charas;
        setLastChanged();
    }

    public LocalDateTime getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged() {
        this.lastChanged = LocalDateTime.now();
    }

    private class RecordComparator implements Comparator<SR4_CharRecord> {
        @Override
        public int compare( SR4_CharRecord record1, SR4_CharRecord record2 ) {
            return -1 * record1.compareTo( record2 );
        }
    }
}
