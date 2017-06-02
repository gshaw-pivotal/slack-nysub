package gs.nysub;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SubwayLinesTest {

    private SubwayLines subwayLines;

    @Before
    public void setup() {
        subwayLines = new SubwayLines();
    }

    @Test
    public void givenAValidLineId_returnsTrue() {
        assertTrue("", subwayLines.isValidLine("N"));
        assertTrue("", subwayLines.isValidLine("2"));
        assertTrue("", subwayLines.isValidLine("1"));
        assertTrue("", subwayLines.isValidLine("SIR"));
    }

    @Test
    public void givenAnInvalidLineId_returnsFalse() {
        assertFalse(subwayLines.isValidLine("X"));
        assertFalse(subwayLines.isValidLine("ACE"));
    }
}