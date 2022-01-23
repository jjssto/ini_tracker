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
    @CollectionTable( name = "dice" )
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
        StringBuilder ret = new StringBuilder("{");
        ret.append("\"zeit\": \"").append(zeit.toString()).append("\"").append(",\"char\": \"").append(charRecord.getChar().getName()).append("\"").append(",\"roll\": [");
        for (int i = 0; i < roll.size(); i++  ) {
           ret.append("\"").append(roll.get(i).toString()).append("\"");
            if ( i != roll.size() - 1 ) {
                ret.append(",");
            }
        }
        return ret + "]}";
    }

    public void roll( int nbr  ) {
        this.roll = new ArrayList<Integer>();
        int[] result = dist.sample( nbr );
        for ( Integer r : result ) {
            this.roll.add( r );
        }
        this.zeit = LocalDateTime.now();
    }

    public void roll( int nbr, CharRecord charRecord  ) {
        roll( nbr );
        this.charRecord = charRecord;
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
