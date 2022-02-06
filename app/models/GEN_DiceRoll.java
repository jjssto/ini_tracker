package models;

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
    void roll( int nbr, List<Integer> roll ) {
        this.roll = rollRet( nbr, roll );
    }

    @Override
    void roll( int nbr ) {
        this.roll = rollRet( nbr );
    }

    @Override
     void roll() {
        roll( 6 );
    }

    @Override
    void setRoll( List<Integer> roll ) {
        this.roll = roll;
    }

    @Override
    List<Integer> getRoll() {
        return roll;
    }

    public GEN_DiceRoll() {
        this( 6 );
    }
}
