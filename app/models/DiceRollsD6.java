package models;

import java.util.List;

public class DiceRollsD6 {

    private List<DiceRoll> rolls;


    public DiceRoll roll( int nbr ) {
        DiceRoll newRoll = new DiceRoll( 6 );
        newRoll.roll( nbr );
        rolls.add( newRoll  );
        return newRoll;
    };


    public List<DiceRoll> getRolls() {
        return rolls;
    }

    public void setRolls(List<DiceRoll> rolls) {
        this.rolls = rolls;
    }
}
