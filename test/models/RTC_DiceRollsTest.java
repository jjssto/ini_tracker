package models;

import com.fasterxml.jackson.databind.JsonNode;
import junit.framework.TestCase;
import models.rtc.RTC_DiceRolls;
import org.junit.Test;
import play.libs.Json;

public class RTC_DiceRollsTest extends TestCase {


    @Test
    public void testRoll() {
        RTC_DiceRolls dr = new RTC_DiceRolls();
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
        RTC_DiceRolls dr = new RTC_DiceRolls();
        dr.setSkill( 3 );
        dr.setAttribute( 4 );
        dr.roll();
        JsonNode json = Json.parse( dr.toJson() );
        assertNotNull( json );
    }
}