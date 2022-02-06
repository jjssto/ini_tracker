package models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table( name = "sr4_dice_roll")
public class SR4_DiceRoll extends DiceRoll {

    public SR4_DiceRoll () {
        this( 6 );
    }

    public SR4_DiceRoll ( int eyes ) {
        super( 6 );
    }

    public SR4_DiceRoll ( int eyes, SR4_CharRecord record ) {
        super( 6 );
        this.charRecord = record;
    }

    public SR4_DiceRoll( List<Integer> roll ) {
        this.roll = roll;
    }

    @ManyToOne
    @JoinColumn( name = "sr4_record_id")
    private SR4_CharRecord charRecord;

    @ElementCollection( fetch = FetchType.EAGER )
    @CollectionTable( name = "sr4_dice", joinColumns = @JoinColumn( name = "dice_roll_id") )
    private List<Integer> roll;


    @Override
    public String toJson() {
        String charName;
        StringBuilder ret = new StringBuilder("{");
        try {
            charName = charRecord.getChar().getName();
        } catch ( NullPointerException e) {
            charName = "";
        }
        ret.append("\"zeit\": \"").append(getZeit().toString()).append("\"").append(",\"char\": \"").append( charName ).append("\"").append(",\"roll\": [");
        for (int i = 0; i < getRoll().size(); i++  ) {
           ret.append("\"").append(getRoll().get(i).toString()).append("\"");
            if ( i != getRoll().size() - 1 ) {
                ret.append(",");
            }
        }
        ret.append("],").append("\"result\":\"");
        ret.append( bigger_equal( 5 ) ).append("\"}");
        return ret.toString();
    }

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
        roll( 1 );
    }

    @Override
    public void setRoll( List<Integer> roll ) {
        this.roll = roll;
    }

    @Override
    public List<Integer> getRoll() {
        return this.roll;
    }

    public void roll( int nbr, SR4_CharRecord charRecord  ) {
        roll( nbr );
        this.charRecord = charRecord;
    }

    public SR4_CharRecord getCharRecord() {
        return charRecord;
    }

    public void setCharRecord( SR4_CharRecord chara) {
        this.charRecord = chara;
    }
}
