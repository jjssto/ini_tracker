package models;

import javax.persistence.*;

@Entity
@Table( name = "gen_dice_rolls" )
public class RTC_DiceRolls {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @ManyToOne
    @JoinColumn( name = "combat_id")
    private final RTC_Combat combat;


    @OneToOne
    @JoinColumn( name = "d6_id" )
    private final RTC_DiceRoll d6;

    @OneToOne
    @JoinColumn( name = "d8_id" )
    private final RTC_DiceRoll d8;

    @OneToOne
    @JoinColumn( name = "d12_id" )
    private final RTC_DiceRoll d12;

    @Column( name = "skill" )
    private int skill = -1;

    @Column( name = "attribute" )
    private int attribute = -1;


    /* Constructers */

    public RTC_DiceRolls(
        RTC_Combat combat
    ) {
        this.combat = combat;
        d6 = new RTC_DiceRoll( 6  );
        d8 = new RTC_DiceRoll( 8  );
        d12 = new RTC_DiceRoll( 12  );
    }

    public RTC_DiceRolls() {
        this( null );
    }


    public void roll() {

        int nbrD6 = 0;
        int nbrD8 = 0;
        int nbrD12 = 0;

        if ( skill > -1 ) {
            if ( attribute >= skill ) {
                nbrD12 = skill;
                nbrD8 = attribute - skill;
            } else {
                nbrD12 = attribute;
                nbrD6 = skill - attribute;
            }
        } else {
            nbrD6 = attribute;
        }

        if ( nbrD6 > 0 ) {
            d6.roll( nbrD6 );
        }
        if ( nbrD8 > 0 ) {
            d8.roll( nbrD8 );
        }
        if ( nbrD12 > 0 ) {
            d12.roll( nbrD12 );
        }
    }

    public String toJson() {
        boolean commaNeeded = false;
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"skill\":\"");
        if ( skill == -1 ) {
            sb.append("0");
        } else if ( skill == 0 ) {
            sb.append("Ø");
        } else {
            sb.append(skill);
        }
        sb.append("\",\"attribute\":\"").append(attribute);
        int success = d6.result() + d8.result() + d12.result();
        sb.append("\",\"success\":\"").append(success).append("\",");
        if ( d6.getRoll().size() > 0 ) {
            if ( commaNeeded ) sb.append(",");
            sb.append("\"d6\":");
            sb.append( d6.toJson() );
            commaNeeded = true;
        }
        if ( d8.getRoll().size() > 0 ) {
            if ( commaNeeded ) sb.append(",");
            sb.append("\"d8\":");
            sb.append( d8.toJson() );
            commaNeeded = true;
        }
        if ( d12.getRoll().size() > 0 ) {
            if ( commaNeeded ) sb.append(",");
            sb.append("\"d12\":");
            sb.append( d12.toJson() );
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

    public RTC_Combat getCombat() {
        return combat;
    }


    public RTC_DiceRoll getD6() {
        return d6;
    }

    public RTC_DiceRoll getD8() {
        return d8;
    }

    public RTC_DiceRoll getD12() {
        return d12;
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill( int skill ) {
        this.skill = skill;
    }

    public int getAttribute() {
        return attribute;
    }

    public void setAttribute( int attribute ) {
        this.attribute = attribute;
    }
}
