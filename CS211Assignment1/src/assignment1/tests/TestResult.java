package assignment1.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import assignment1.Cell;
import assignment1.Result;

public class TestResult {

    Result result;
    
    @Before
    public void before(){
        result = new Result();
    }

    @Test
    public void testSetSuccess() {
        assertFalse("Should start False for success", result.getSuccess());
        result.setSuccess(true);
        assertTrue("Should be True after setting True", result.getSuccess());
    }

    @Test
    public void testGetSuccess() {
        result.setSuccess(true);
        assertTrue("Should be True after setting True", result.getSuccess());
        result.setSuccess(false);
        assertFalse("Should be False after setting False", result.getSuccess());
    }

    @Test
    public void testSetMessage() {
        result.setMessage("Hello World");
        assertEquals("Strings should be the same", "Hello World", result.getMessage());
    }

    @Test
    public void testGetMessage() {
        assertEquals("Message should start blank", " ", result.getMessage());
        result.setMessage("Balrog of Kazagoracle");
        assertEquals("Strings should be the same", "Balrog of Kazagoracle", result.getMessage());
    }

    @Test
    public void testAddAffected() {
        Cell cell = new Cell(0, 5, 4);
        result.addAffected(cell);
        assertEquals("Cell added should be same as put in", cell, result.getAffected().get(0));
    }

    @Test
    public void testGetAffected() {
        Cell cell1 = new Cell(0, 5, 4);
        Cell cell2 = new Cell(6, 1, 8);
        Cell cell3 = new Cell(2, 4, 1);
        result.addAffected(cell1);
        assertEquals("Cell added should be same as put in - cell1", cell1, result.getAffected().get(0));
        result.addAffected(cell2);
        result.addAffected(cell3);
        assertEquals("Cell added should be same as put in - cell3", cell3, result.getAffected().get(2));
        assertEquals("Cell added should be same as put in - cell2", cell2, result.getAffected().get(1));
    }

}
