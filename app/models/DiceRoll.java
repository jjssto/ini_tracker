package models;

import jdk.jfr.Timestamp;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance( strategy = InheritanceType.JOINED )
@Table( name = "dice_roll" )
public class DiceRoll {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @ElementCollection( fetch = FetchType.EAGER )
    @CollectionTable(
        name = "dice",
        joinColumns = @JoinColumn( name = "dice_roll_id", referencedColumnName = "id" ))
    private List<Integer> roll;

    @Column( name = "eyes" )
    private int eyes;

    @Column( name="comment" )
    private String comment;

    @Column( name = "zeit")
    @Timestamp
    private LocalDateTime zeit;

    @Transient
    private final UniformIntegerDistribution dist;

    public DiceRoll( ArrayList<Integer> roll ) {
        this.roll = roll;
        this.dist = null;
    }

    public DiceRoll() {
        this(6);
    }

    public DiceRoll( int eyes ) {
        this.dist = new UniformIntegerDistribution( 1, eyes );
        this.eyes = eyes;
        this.zeit = null;
    }
    public String toJson() {

        StringBuilder ret = new StringBuilder("{");
        ret.append("\"zeit\": \"").append(getZeit().toString()).append("\"").append(",\"comment\": \"").append( comment ).append("\"").append(",\"roll\": [");
        for (int i = 0; i < roll.size(); i++  ) {
            ret.append("\"").append(roll.get(i).toString()).append("\"");
            if ( i != roll.size() - 1 ) {
                ret.append(",");
            }
        }
        ret.append("]}");
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

    public void roll( int nbr, String comment ) {
        roll( nbr );
        this.comment = comment;
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

    public void setId( long id ) {
        this.id = id;
    }

    public void setRoll( List<Integer> roll ) {
        this.roll = roll;
    }

    public LocalDateTime getZeit() {
        return zeit;
    }

    public void setZeit( LocalDateTime zeit ) {
        this.zeit = zeit;
    }

    public UniformIntegerDistribution getDist() {
        return dist;
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

}
