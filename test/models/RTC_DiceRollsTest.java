package models;

import com.fasterxml.jackson.databind.JsonNode;
import junit.framework.TestCase;
import models.rtc.RtcDiceRolls;
import org.junit.Test;
import play.libs.Json;

public class RTC_DiceRollsTest extends TestCase {


    @Test
    public void testRoll() {
        RtcDiceRolls dr = new RtcDiceRolls();
        dr.setSkill( 3 );
        dr.setAttribute( 4 );
        dr.roll();
        assertTrue(
            dr.getD12().getRoll().size() == 3 &&
                dr.getD8().getRoll().size() == 1
        );
    }

    @Test
    public void testToJson() {
        RtcDiceRolls dr = new RtcDiceRolls();
        dr.setSkill( 3 );
        dr.setAttribute( 4 );
        dr.roll();
        JsonNode json = Json.parse( dr.toJson() );
        assertNotNull( json );
    }
}