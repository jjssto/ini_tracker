package models;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table( name = "sr4_dice_roll")
@PrimaryKeyJoinColumn( name = "dice_roll_id" )
public class SR4_DiceRoll extends DiceRoll {

    @ManyToOne
    @JoinColumn( name = "char_record_id" )
    private SR4_CharRecord charRecord;


    public SR4_DiceRoll( ArrayList<Integer> roll ) {
        super( roll );
    }

    public SR4_DiceRoll() {
        this(6);
    }

    public SR4_DiceRoll( int eyes ) {
       super( eyes );
    }

    public SR4_DiceRoll( int eyes, SR4_CharRecord charRecord ) {
        this( eyes );
        this.charRecord = charRecord;
    }

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
