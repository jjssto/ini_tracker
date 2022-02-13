package models;

import junit.framework.TestCase;
import models.rtc.RtcDiceRoll;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RTC_DiceRollTest extends TestCase {

    private RtcDiceRoll dr1, dr2;
    private List<Integer> list;

    @Test
    public void average() {
        dr2 = new RtcDiceRoll( 6 );
        dr2.roll( 300 );
        int sum = 0;
        for ( int i : dr2.getRoll() ) {
            sum += i;
        }
        assertTrue( sum > 850 && sum < 1250 );
    }

    @Test
    public void testResult() {
        int[] roll = {4, 5, 8, 9, 11, 5, 4, 8, 7} ;
        list = new ArrayList<>();
        dr1 = new RtcDiceRoll();
        for ( Integer i : roll ) {
            list.add( i );
        }
        dr1.setRoll( list );
        int result = dr1.result();
        assertEquals(  8, result );
    }
}