package models.rtc;

import models.DiceRoll;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table( name = "rtc_dice_roll" )
public class RtcDiceRoll
    extends DiceRoll {


    @ElementCollection
    @CollectionTable( name = "rtc_dice", joinColumns = @JoinColumn( name = "dice_roll_id") )
    private List<Integer> roll;

    @Override
    public void roll( int nbr, List<Integer> roll ) {
        this.roll = rollRet( nbr, roll );
    }

    @Override
    public void roll( int nbr ) {
        this.roll = rollRet( nbr );
    }

    @Override
    public void roll() {
        roll( 6 );
    }

    @Override
    public void setRoll( List<Integer> roll ) {
        this.roll = roll;
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


    public RtcDiceRoll( int eyes ) {
            super( eyes );
            roll = new ArrayList<Integer>();
    }
    public RtcDiceRoll() {
        this( 6 );
    }
}
