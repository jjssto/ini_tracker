package models.gen;

import models.DiceRoll;

import javax.persistence.*;
import java.util.List;

@Entity
@Table( name = "gen_dice_roll" )
public class GEN_DiceRoll extends DiceRoll {

    public GEN_DiceRoll( int eyes ) {
        super( eyes );
    }

    @ElementCollection
    @CollectionTable( name = "gen_dice", joinColumns = @JoinColumn( name = "dice_roll_id") )
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

    public GEN_DiceRoll() {
        this( 6 );
    }
}
