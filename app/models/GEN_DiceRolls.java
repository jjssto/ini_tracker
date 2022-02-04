package models;

import javax.persistence.*;

@Entity
@Table( name = "gen_dice_rolls" )
public class GEN_DiceRolls {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @ManyToOne
    @JoinColumn( name = "combat_id")
    private final GEN_Combat combat;

    @OneToOne
    @JoinColumn( name = "d4_id")
    private final DiceRoll d4;

    @OneToOne
    @JoinColumn( name = "d6_id" )
    private final DiceRoll d6;

    @OneToOne
    @JoinColumn( name = "d8_id" )
    private final DiceRoll d8;

    @OneToOne
    @JoinColumn( name = "d10_id" )
    private final DiceRoll d10;

    @OneToOne
    @JoinColumn( name = "d12_id" )
    private final DiceRoll d12;

    @OneToOne
    @JoinColumn( name = "d20_id" )
    private final DiceRoll d20;

    @Transient
    private int nbrD4 = 0;

    @Transient
    private int nbrD6 = 0;

    @Transient
    private int nbrD8 = 0;

    @Transient
    private int nbrD10 = 0;

    @Transient
    private int nbrD12 = 0;

    @Transient
    private int nbrD20 = 0;


    /* Constructers */

    public GEN_DiceRolls(
        GEN_Combat combat
    ) {
        this.combat = combat;
        d4 = new DiceRoll( 4  );
        d6 = new DiceRoll( 6  );
        d8 = new DiceRoll( 8  );
        d10 = new DiceRoll( 10  );
        d12 = new DiceRoll( 12  );
        d20 = new DiceRoll( 20  );
    }

    public GEN_DiceRolls() {
        this( null );
    }

    public void roll() {
        if ( nbrD4 > 0 ) {
            d4.roll( nbrD4 );
        }
        if ( nbrD6 > 0 ) {
            d6.roll( nbrD6 );
        }
        if ( nbrD8 > 0 ) {
            d8.roll( nbrD8 );
        }
        if ( nbrD10 > 0 ) {
            d10.roll( nbrD10 );
        }
        if ( nbrD12 > 0 ) {
            d12.roll( nbrD12 );
        }
        if ( nbrD20 > 0 ) {
            d20.roll( nbrD20 );
        }
    }

    public String toJson() {
        boolean commaNeeded = false;
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if ( nbrD4 > 0 ) {
           sb.append("\"d4\":");
           sb.append( d4.toJson() );
           commaNeeded = true;
        }
        if ( nbrD6 > 0 ) {
            if ( commaNeeded ) sb.append(",");
            sb.append("\"d6\":");
            sb.append( d6.toJson() );
            commaNeeded = true;
        }
        if ( nbrD8 > 0 ) {
            if ( commaNeeded ) sb.append(",");
            sb.append("\"d8\":");
            sb.append( d8.toJson() );
            commaNeeded = true;
        }
        if ( nbrD10 > 0 ) {
            if ( commaNeeded ) sb.append(",");
            sb.append("\"d10\":");
            sb.append( d10.toJson() );
            commaNeeded = true;
        }
        if ( nbrD12 > 0 ) {
            if ( commaNeeded ) sb.append(",");
            sb.append("\"d12\":");
            sb.append( d12.toJson() );
            commaNeeded = true;
        }
        if ( nbrD20 > 0 ) {
            if ( commaNeeded ) sb.append(",");
            sb.append("\"d20\":");
            sb.append( d20.toJson() );
            commaNeeded = true;
        }
        return sb.append("}").toString();
    }

    /* Getters and Setters */
    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public GEN_Combat getCombat() {
        return combat;
    }

    public DiceRoll getD4() {
        return d4;
    }

    public DiceRoll getD6() {
        return d6;
    }

    public DiceRoll getD8() {
        return d8;
    }

    public DiceRoll getD10() {
        return d10;
    }

    public DiceRoll getD12() {
        return d12;
    }

    public DiceRoll getD20() {
        return d20;
    }

    public int getNbrD4() {
        return nbrD4;
    }

    public void setNbrD4( int nbrD4 ) {
        this.nbrD4 = nbrD4;
    }

    public int getNbrD6() {
        return nbrD6;
    }

    public void setNbrD6( int nbrD6 ) {
        this.nbrD6 = nbrD6;
    }

    public int getNbrD8() {
        return nbrD8;
    }

    public void setNbrD8( int nbrD8 ) {
        this.nbrD8 = nbrD8;
    }

    public int getNbrD10() {
        return nbrD10;
    }

    public void setNbrD10( int nbrD10 ) {
        this.nbrD10 = nbrD10;
    }

    public int getNbrD12() {
        return nbrD12;
    }

    public void setNbrD12( int nbrD12 ) {
        this.nbrD12 = nbrD12;
    }

    public int getNbrD20() {
        return nbrD20;
    }

    public void setNbrD20( int nbrD20 ) {
        this.nbrD20 = nbrD20;
    }
}
