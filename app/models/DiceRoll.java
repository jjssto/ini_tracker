package models;

import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table( name = "dice_roll")
public class DiceRoll {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @ElementCollection( fetch = FetchType.EAGER )
    @CollectionTable(
        name = "dice",
        joinColumns = @JoinColumn( name = "dice_roll_id", referencedColumnName = "id" ))
    private List<Integer> roll;

    private int eyes;

    private String comment;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CharRecord charRecord;

    private LocalDateTime zeit;

    @Transient
    private final UniformIntegerDistribution dist;

    public DiceRoll( ArrayList<Integer> roll ) {
        this.roll = roll;
        dist = null;
    }

    public DiceRoll() {
        this(6);
    }

    public DiceRoll( int eyes ) {
        this.dist = new UniformIntegerDistribution( 1, eyes );
        this.zeit = null;
    }

    public DiceRoll( int eyes, CharRecord charRecord ) {
        this( eyes );
        this.charRecord = charRecord;
    }

    public String toJson() {
        String charName;
        StringBuilder ret = new StringBuilder("{");
        try {
            charName = charRecord.getChar().getName();
        } catch ( NullPointerException e) {
            charName = "";
        }
        ret.append("\"zeit\": \"").append(zeit.toString()).append("\"").append(",\"char\": \"").append( charName ).append("\"").append(",\"roll\": [");
        for (int i = 0; i < roll.size(); i++  ) {
           ret.append("\"").append(roll.get(i).toString()).append("\"");
            if ( i != roll.size() - 1 ) {
                ret.append(",");
            }
        }
        ret.append("],").append("\"result\":\"");
        ret.append( bigger_equal( 5 ) ).append("\"}");
        return ret.toString();
    }

    public void roll( int nbr, List<Integer> roll ) {
        this.roll = roll;
        int[] result;
        try {
            result = dist.sample(nbr);
        } catch( NullPointerException e ) {
            result = new int[]{};
        }
        for ( Integer r : result ) {
            this.roll.add( r );
        }
        this.zeit = LocalDateTime.now();
    }

    public void roll( int nbr ) {
        List<Integer> roll = new ArrayList<>();
        roll( nbr, roll );
    }

    public void roll( int nbr, CharRecord charRecord  ) {
        roll( nbr );
        this.charRecord = charRecord;
    }

    public void explode( int nbr ) {
        int firstIndex = 0;
        int count = nbr;
        this.roll = new ArrayList<Integer>();
        while ( count > 0 ) {
            roll( count, this.roll );
            count = 0;
            for ( int i = firstIndex; i < this.roll.size(); i++ ) {
                if ( this.roll.get( i ).equals( 6 ) ) {
                    count++;
                }
            }
            firstIndex = this.roll.size();
        }
    }


    public int bigger_equal( int target ) {
        int ret = 0;
        for( int die : roll) {
            if ( die >= target ) {
                ret++;
            }
        }
        return ret;
    }

    public int sum() {
        int ret = 0;
        for( int die : roll) {
            ret += die;
        }
        return ret;
    }


    public long getId() {
        return id;
    }

    public List<Integer> getRoll() {
        return roll;
    }


    public int getEyes() {
        return eyes;
    }

    public void setEyes(int eyes) {
        this.eyes = eyes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CharRecord getCharRecord() {
        return charRecord;
    }

    public void setCharRecord(CharRecord chara) {
        this.charRecord = chara;
    }
}
