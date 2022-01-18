package models;

import org.apache.commons.math3.distribution.UniformIntegerDistribution;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity
@Table( name = "dice_roll")
public class DiceRoll {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @ElementCollection
    @CollectionTable( name = "dice" )
    private List<Integer> roll;

    private int eyes;

    private String comment;

    @ManyToOne
    private CharRecord charRecord;

    private long unixTime;

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
        this.unixTime = Instant.now().getEpochSecond();
    }

    public DiceRoll( int eyes, CharRecord charRecord ) {
        this( eyes );
        this.charRecord = charRecord;
    }

    //public void roll( int nbr  ) {
    //    int[] result = dist.sample( nbr );
    //    this.roll = new ArrayList<Die>();
    //    for( int r : result ) {
    //        Die newDie = new Die( r );
    //        roll.add( newDie );
    //    }
    //}
    public void roll( int nbr  ) {
        int[] result = dist.sample( nbr );
        for ( Integer r : result ) {
            this.roll.add( r );
        }
    }

    public void roll( int nbr, CharRecord charRecord  ) {
        roll( nbr );
        this.charRecord = charRecord;
    }


    //public String toString() {
    //    String ret = "( ";
    //    for( int i = 0; i < roll.size(); i++ ) {
    //       ret += roll.get(i).toString() + ", ";
    //    }
    //    return ret + ")";
    //}

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
