package models.rtc;

import models.sec.SEC_User;

import javax.persistence.*;

@Entity
@Table( name = "rtc_dice_rolls" )
public class RtcDiceRolls {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    @ManyToOne
    @JoinColumn( name = "combat_id")
    private final RtcCombat combat;

    @ManyToOne ( fetch = FetchType.EAGER )
    @JoinColumn( name = "user_id" )
    private final SEC_User user;

    @OneToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "d6_id" )
    private final RtcDiceRoll d6;

    @OneToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "d8_id" )
    private final RtcDiceRoll d8;

    @OneToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "d12_id" )
    private final RtcDiceRoll d12;

    @Column( name = "skill" )
    private int skill = -1;

    @Column( name = "attribute" )
    private int attribute = -1;

    @Column( name = "no_tag")
    private boolean noTag = false;


    /* Constructers */


    public RtcDiceRolls(
        RtcCombat combat,
        SEC_User user
    ) {
        this.user = user;
        this.combat = combat;
        d6 = new RtcDiceRoll( 6  );
        d8 = new RtcDiceRoll( 8  );
        d12 = new RtcDiceRoll( 12  );
    }

    public RtcDiceRolls( RtcCombat combat ) {
        this( combat, null );
    }

    public RtcDiceRolls() {
        this( null );
    }


    public void roll() {

        int nbrD6 = 0;
        int nbrD8 = 0;
        int nbrD12 = 0;

        if ( skill > -1 ) {
            if ( attribute > skill ) {
                if ( noTag ) {
                    nbrD8 = skill;
                    nbrD6 = attribute - skill;
                } else {
                    nbrD12 = skill;
                    nbrD8 = attribute - skill;
                }
            } else {
                nbrD6 = skill - attribute;
                if ( noTag ) {
                    nbrD8 = attribute;
                } else {
                    nbrD12 = attribute;
                }
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
        sb.append("\",\"noTag\":\"");
        if ( noTag ) {
            sb.append("j");
        } else {
            sb.append("n");
        }
        sb.append("\",\"user\":\"");
        if ( this.user != null ) {
            sb.append( user.getUserName() );
        }
        int success = d6.result() + d8.result() + d12.result();
        sb.append("\",\"success\":\"").append(success).append("\",");
        if ( d6.getRoll().size() > 0 ) {
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

    public RtcCombat getCombat() {
        return combat;
    }


    public RtcDiceRoll getD6() {
        return d6;
    }

    public RtcDiceRoll getD8() {
        return d8;
    }

    public RtcDiceRoll getD12() {
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

    public boolean isNoTag() {
        return noTag;
    }

    public void setNoTag( boolean noTag ) {
        this.noTag = noTag;
    }

    public SEC_User getUser() {
        return user;
    }
}
