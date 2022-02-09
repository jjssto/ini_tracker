package models;

import javax.persistence.*;

@Entity
@Table( name = "sr4_char_record")
public class SR4_CharRecord implements Comparable<SR4_CharRecord> {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne( cascade = CascadeType.MERGE )
    @JoinColumn( name = "char_id")
    private SR4_Char chara;

    @Column( name = "local_ini")
    private Integer localIni = 1;

    @Column( name = "ini_value")
    private Integer iniValue = 1;

    @Column( name = "s_dmg")
    private Integer sDmg = 0;

    @Column( name = "p_dmg")
    private Integer pDmg = 0;

    @ManyToOne()
    @JoinColumn( name = "combat_id" )
    private SR4_Combat combat;


    public SR4_CharRecord() {
        this.combat = new SR4_Combat();
        this.chara = new SR4_Char();
        this.localIni = 0;
        this.iniValue = 0;
        this.sDmg = 0;
        this.pDmg = 0;
    }
    public SR4_CharRecord( SR4_Combat combat ) {
        this( null, combat );
    }
    public SR4_CharRecord( SR4_Char chara, SR4_Combat combat ) {
        this.combat = combat;
        this.chara = chara;
        this.localIni = 0;
    }

    @Override
    public int compareTo( SR4_CharRecord otherRecord ) {
        int compValue = iniValue.compareTo( otherRecord.getIniValue() );
        if ( compValue  != 0 ) {
            return compValue;
        } else if ( chara.getReaction() != otherRecord.getChar().getReaction() ) {
            return chara.getReaction().compareTo( otherRecord.getChar().getReaction() );
        } else if ( chara.getIntuition() != otherRecord.getChar().getIntuition() ) {
            return chara.getIntuition().compareTo( otherRecord.getChar().getIntuition() );
        } else {
            return 0;
        }
    }



    public Integer getNbrIniDice() {
        int initiative;
        if ( localIni != 0 && localIni > 0 ) {
            initiative = localIni;
        } else {
            initiative = chara.getIni();
        }
        int penalty = Math.max( getPDmg(), getSDmg() ) / 3;
        int nbr_dice;
        if ( initiative > penalty ) {
            nbr_dice = initiative - penalty ;
        } else {
            nbr_dice = 1;
        }
        return nbr_dice;
    }




    /* Setters and Getters */

    public Integer
    getId()
    {
        return id;
    }

    public void
    setId( Integer id )
    {
        this.id = id;
    }

    public Integer
    getLocalIni()
    {
        return localIni;
    }

    public void setLocalIni( Integer localIni )
    {
        this.localIni = localIni;
        combat.setLastChanged();
    }

    public Integer getIniValue()
    {
        return iniValue;
    }

    public void setIniValue( Integer iniValue )
    {
        this.iniValue = iniValue;
        combat.setLastChanged();
    }

    public Integer getSDmg()
    {
        return sDmg;
    }

    public void setSDmg( Integer sDmg )
    {
        this.sDmg = sDmg;
        combat.setLastChanged();
    }

    public Integer getPDmg()
    {
        return pDmg;
    }

    public void setPDmg( Integer pDmg )
    {
        this.pDmg = pDmg;
        combat.setLastChanged();
    }

    public void setChara ( SR4_Char chara ) {
        this.chara = chara;
    }

    public SR4_Char
    getChar()
    {
        return chara;
    }

    public Integer
    getCharId()
    {
        return chara.getId();
    }

    public Integer
    getIni()
    {
        return chara.getIni();
    }

    public Integer
    getSBoxes()
    {
        return chara.getSBoxes();
    }

    public Integer
    getPBoxes()
    {
        return chara.getPBoxes();
    }

    public Integer
    getCombatId() {
        if ( combat != null ) {
            return combat.getId();
        } else {
            return 0;
        }
    }


}
