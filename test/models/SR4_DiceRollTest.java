package models;

import com.fasterxml.jackson.databind.JsonNode;
import junit.framework.TestCase;
import models.sr4.SR4_DiceRoll;
import org.junit.Test;
import play.libs.Json;

import java.util.List;

public class SR4_DiceRollTest extends TestCase {
    private SR4_DiceRoll dr1, dr2;
    private List<Integer> list;

    @Test
    public void average() {
        dr2 = new SR4_DiceRoll( 6 );
        dr2.roll( 300 );
        int sum = 0;
        for ( int i : dr2.getRoll() ) {
            sum += i;
        }
        assertTrue( sum > 850 && sum < 1250 );
    }

    public void testToJson() {
       SR4_DiceRoll dr = new SR4_DiceRoll(6);
       dr.roll( 10 );
       JsonNode json = Json.parse( dr.toJson() );
        assertNotNull( json );
    }

    public void testRoll() {
    }

    public void testTestRoll() {
    }

    public void testTestRoll1() {
    }

    public void testTestRoll2() {
    }
}