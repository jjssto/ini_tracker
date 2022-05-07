package models.coc;

import models.sec.SecUser;

import javax.persistence.*;
import java.util.*;

@Entity( )
public class CocDiceRolls {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private long id;

    private short advantage = 0;

    @OneToOne
    private CocDiceRoll d10;

    @OneToOne
    private CocDiceRoll d100;

    private DegreeOfSuccess success = null;

    @ManyToOne
    @JoinColumn( name = "user_id" )
    private SecUser user = null;

    @ManyToOne
    @JoinColumn( name = "char_id" )
    private CocChar chara = null;

    @ManyToOne
    @JoinColumn( name = "combat_id" )
    private CocCombat combat = null;

    private String comment;

    public CocDiceRolls() {
        d10 = new CocDiceRoll( 10, false );
        d100 = new CocDiceRoll(10, true );
    }

    public CocDiceRolls( short advantage ) {
        this();
        this.advantage = advantage;
    }

    public void roll() {
        d10.roll( 1 );
        if ( advantage >= 0 ) {
            d100.roll( 1 + advantage );
        } else {
            d100.roll( 1 - advantage );
        }
    }

    public void roll( int skill ) {
        roll();
        setSuccess( skill );
    }

    public int result() {
        int d1 = get10();
        return get100( d1 ) + d1;
    }

    public void setSuccess( int skill ) {
        int res = result();
        if ( res > skill ) {
            success = DegreeOfSuccess.FAILURE;
        } else if ( res > skill / 2 ) {
            success = DegreeOfSuccess.SUCCESS;
        } else if ( res > skill / 4 ) {
            success = DegreeOfSuccess.HARD;
        } else {
            success = DegreeOfSuccess.EXTREME;
        }
    }

    private int get100( int d1 ) {
        List<Integer> list = d100.getRoll();
        int ret;
        if ( advantage == 0 ) {
            ret = list.get( 0 );
        } else if ( advantage < 0 ) {
            if ( d1 == 0 && list.contains( Integer.valueOf( 0 ) ) ) {
                ret = 0;
            } else {
                ret = Collections.max( list );
            }

        } else {
            ret = Collections.min( list );
            if ( ret == 0 && d1 == 0 ) {
                ArrayList<Integer> listCopy = new ArrayList<>( list );
                int i = listCopy.size() - 1;
                while ( i >= 0 ) {
                    if ( listCopy.get(i).equals( 0 ) ) {
                        listCopy.remove( i );
                    }
                    i = i - 1;
                }
                if ( listCopy.size() > 0 ) {
                    ret = Collections.min( listCopy );
                } else {
                    ret = 0;
                }
            }
        }
        if ( ret == 0 && d1 == 0 ) {
            return 100;
        } else {
            return ret;
        }
    }

    public String toJson() {

        StringBuilder sb = new StringBuilder();
        sb.append("{\"t\":\"").append( d10.getZeit().toString() ).append("\",");
        if ( chara != null ) {
            sb.append("\"c\":\"").append( chara.getName() ).append("\",");
        }
        if ( user != null ) {
            sb.append("\"u\":\"").append( user.getUserName() ).append("\",");
        }
        if ( success != null ) {
            sb.append("\"s\":\"").append( success.ordinal() ).append("\",");
        }
        sb.append("\"r1\":").append( d10.toJson() ).append(",");
        sb.append("\"r2\":").append( d100.toJson() ).append(",");
        sb.append("\"r\":\"").append( result() ).append("\"}");

        return sb.toString();
    }

    private int get10() {
        int ret =  d10.getRoll().get(0);
        if ( ret == 10 ) {
            return 0;
        } else {
            return ret;
        }
    }

    public CocDiceRoll getD10() {
        return d10;
    }

    public CocDiceRoll getD100() {
        return d100;
    }

   public DegreeOfSuccess getSuccess() {
        return success;
   }

    public void setCombat( CocCombat combat ) {
        this.combat = combat;
    }

    public void setChar( CocChar chara ) {
        this.chara = chara;
    }

    public void setUser( SecUser user ) {
        this.user = user;
    }

    public void setRoll( List<Integer> d10, List<Integer> d100, short advantage ) {
        this.d10.setRoll( d10 );
        this.d100.setRoll( d100 );
        this.advantage = advantage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment( String comment ) {
        this.comment = comment;
    }
}
