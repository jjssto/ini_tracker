package models;

import jdk.jfr.Timestamp;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class DiceRoll {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column( name = "eyes" )
    private int eyes;

    @Column( name="comment" )
    private String comment;

    @Column( name = "zeit")
    @Timestamp
    private LocalDateTime zeit;

    @Transient
    private final UniformIntegerDistribution dist;

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
        for (int i = 0; i < getRoll().size(); i++  ) {
            ret.append("\"").append(getRoll().get(i).toString()).append("\"");
            if ( i != getRoll().size() - 1 ) {
                ret.append(",");
            }
        }
        ret.append("]}");
        return ret.toString();
    }


    public List<Integer> rollRet( int nbr, List<Integer> roll ) {
        int[] result;
        try {
            result = dist.sample(nbr);
        } catch( NullPointerException e ) {
            result = new int[]{};
        }
        for ( Integer r : result ) {
            roll.add( r );
        }
        this.zeit = LocalDateTime.now();
        return roll;
    }

    public List<Integer> rollRet( int nbr ) {
        List<Integer> roll = new ArrayList<>();
        return rollRet( nbr, roll );
    }

    public List<Integer> rollRet( int nbr, String comment ) {
        this.comment = comment;
        return rollRet( nbr );
    }

    public abstract void roll(
        int nbr,
        List<Integer> roll
    );
    public abstract void roll( int nbr );
    public abstract void roll();

    public void explode( int nbr ) {
        int firstIndex = 0;
        int count = nbr;
        List<Integer> roll = new ArrayList<Integer>();
        while ( count > 0 ) {
            roll( count, roll );
            count = 0;
            for ( int i = firstIndex; i < roll.size(); i++ ) {
                if ( roll.get( i ).equals( 6 ) ) {
                    count++;
                }
            }
            firstIndex = roll.size();
        }
        setRoll( roll );
    }


    public int bigger_equal( int target ) {
        int ret = 0;
        for( int die : getRoll() ) {
            if ( die >= target ) {
                ret++;
            }
        }
        return ret;
    }

    public int sum() {
        int ret = 0;
        for( int die : getRoll() ) {
            ret += die;
        }
        return ret;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public abstract void setRoll( List<Integer> roll ) ;

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

    public abstract List<Integer> getRoll();


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
