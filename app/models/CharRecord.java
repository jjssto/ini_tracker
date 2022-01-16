package models;

import models.SR4Char;
import javax.persistence.*;
import java.util.ArrayList;
import org.apache.commons.math3.distribution.BinomialDistribution;

@Entity
@Table( name = "char_record")
public class CharRecord {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne( cascade = CascadeType.MERGE )
    @JoinColumn( name = "char_id")
    private SR4Char chara;

    @Column( name = "local_ini")
    private Integer localIni = 0;

    @Column( name = "ini_value")
    private Integer iniValue = 0;

    @Column( name = "s_dmg")
    private Integer sDmg = 0;

    @Column( name = "p_dmg")
    private Integer pDmg = 0;

    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn( name = "combat_id" )
    private Combat combat;

    private static BinomialDistribution dist = new BinomialDistribution( 1, 1.0/3 );

    public
    CharRecord() {
        this.combat = null;
        this.chara = new SR4Char();
        this.localIni = 0;
    }
    public CharRecord( Combat combat ) {
        this( null, combat );
    }
    public
    CharRecord( SR4Char chara, Combat combat ) {
        this.combat = combat;
        this.chara = chara;
        this.localIni = 0;
    }


    public Integer
    rollInitiative()
    {
        int initiative;
        if ( localIni != 0 && localIni > 0 ) {
            initiative = localIni;
        } else {
            initiative = chara.getIni();
        }
        int penalty = (int) Math.max( getPDmg(), getSDmg() ) / 3;
        int nbr_dice;
        if ( initiative > penalty ) {
            nbr_dice = initiative - penalty ;
        } else {
            nbr_dice = 1;
        }
        int diceRolls[] = dist.sample( nbr_dice );
        Integer sum = 0;
        for( Integer die : diceRolls )
        {
            sum += die;
        }
        iniValue = sum + initiative;
        return iniValue;
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
    }

    public Integer getIniValue()
    {
        return iniValue;
    }

    public void setIniValue( Integer iniValue )
    {
        this.iniValue = iniValue;
    }

    public Integer getSDmg()
    {
        return sDmg;
    }

    public void setSDmg( Integer sDmg )
    {
        this.sDmg = sDmg;
    }

    public Integer getPDmg()
    {
        return pDmg;
    }

    public void setPDmg( Integer pDmg )
    {
        this.pDmg = pDmg;
    }

    public SR4Char
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
