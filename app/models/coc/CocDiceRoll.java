package models.coc;

import models.DiceRoll;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CocDiceRoll
    extends DiceRoll {

    @ElementCollection
    @CollectionTable( name = "coc_dice", joinColumns = @JoinColumn( name = "dice_roll_id") )
    private List<Integer> roll;

    private boolean tens = false;

    @Override
    public void roll( int nbr, List<Integer> roll ) {
        this.roll = rollRet( nbr, roll );
        transformRoll( roll );
    }

    @Override
    public void roll( int nbr ) {
        this.roll = rollRet( nbr );
        transformRoll( roll );

    }

    @Override
    public void roll() {
        roll( 6 );
    }

    @Override
    public void setRoll( List<Integer> roll ) {
        this.roll = roll;
        transformRoll( roll );
    }

    @Override
    public List<Integer> getRoll() {
        return roll;
    }


    public int result() {
        int sum = 0;
        for( int die : roll ) {
            if ( die > 9 ) {
                sum += 2;
            } else if ( die > 4 ) {
                sum += 1;
            }
        }
        return sum;
    }

    @Override
    public String toJson() {
        StringBuilder ret = new StringBuilder("[");
        for (int i = 0; i < getRoll().size(); i++  ) {
            Integer roll = getRoll().get(i);
            if ( roll.equals(10) ) {
                roll = 0;
            }
            ret.append("\"").append(roll.toString()).append("\"");
            if ( i != getRoll().size() - 1 ) {
                ret.append(",");
            }
        }
        ret.append("]");
        return ret.toString();
    }

    public CocDiceRoll( int eyes, boolean tens ) {
        super( eyes );
        roll = new ArrayList<Integer>();
        this.tens = tens;
    }
    public CocDiceRoll( int eyes ) {
        this( eyes, false );
    }
    public CocDiceRoll() {
        this( 10 );
    }

    private void transformRoll( List<Integer> roll ) {
        int i = roll.size() - 1;
        while( i >= 0 ){
            int val = roll.get(i);
            if ( val == 10 ) {
                roll.remove( i );
                roll.add( 0 );
            } else if ( tens && val != 0 && val < 10) {
                roll.remove( i );
                roll.add( 10 * val );
            }
            i = i - 1;
        }
    }

    public boolean isTens() {
        return tens;
    }

    public void setTens( boolean tens ) {
        this.tens = tens;
    }

}
