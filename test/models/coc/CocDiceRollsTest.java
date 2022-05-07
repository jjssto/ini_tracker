package models.coc;

import com.fasterxml.jackson.databind.JsonNode;
import models.sec.SecUser;
import org.junit.Before;
import org.junit.Test;
import play.libs.Json;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CocDiceRollsTest {

    private CocDiceRolls rolls = new CocDiceRolls();
    private List<Integer> d10 = new ArrayList<>();
    private List<Integer> d100 = new ArrayList<>();

    @Before
    public void before() {
        d10.add( 0 );
        d100.add( 0 );
        d100.add( 0 );
        d100.add( 1 );
    }
    @Test
    public void result() {
        before();
        rolls.setRoll( d10, d100, (short) 2 );
        int res = rolls.result();
        assertEquals( 10, res );
        rolls.setRoll( d10, d100, (short) -2 );
        res = rolls.result();
        assertEquals( 100, res );
    }

    @Test
    public void toJson() {
        SecUser user = new SecUser("Test", "test");
        rolls.setUser( user );
        rolls.roll();
        String string = rolls.toJson();
        JsonNode json = Json.parse( string );
        assertNotNull( json );
    }

    @Test
    public void success() {
        d100 = new ArrayList<>();
        d100.add(1);
        rolls.setRoll( d10, d100, (short) 0 );
        rolls.setSuccess( 5 );
        assertEquals( DegreeOfSuccess.FAILURE, rolls.getSuccess() );
        rolls.setSuccess( 11 );
        assertEquals( DegreeOfSuccess.SUCCESS, rolls.getSuccess() );
        rolls.setSuccess( 21 );
        assertEquals( DegreeOfSuccess.HARD, rolls.getSuccess() );
        rolls.setSuccess( 41 );
        assertEquals( DegreeOfSuccess.EXTREME, rolls.getSuccess() );
    }
}