package assignment1.tests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import assignment1.Cell;

public class TestCell {
    Cell cell;
    LinkedList<Integer> testCandidates;

    @Before
    public void before() {
        cell = new Cell(0, 5, 1);
        testCandidates = new LinkedList<Integer>();
    }

    @Test
    public void testCell() {
        cell = new Cell(6, 5, 1);
        assertEquals("Value of Cell not setup correctly", 6, cell.getValue());
        assertTrue("Should return True as Cell has value", cell.hasValue());
    }

    @Test
    public void testCellNoValue() {
        assertEquals("Cell should have returned 0 for value", 0,
                cell.getValue());
        assertFalse("Should return False as Cell has no value", cell.hasValue());
    }

    @Test
    public void testCellHighValue() {
        cell = new Cell(1000, 1, 1);
        assertEquals("Cell should have returned 0 for value", 0,
                cell.getValue());
        assertFalse("Should return False as Cell has no value", cell.hasValue());
    }

    @Test
    public void testCellNegativeValue() {
        cell = new Cell(-599, 1, 1);
        assertEquals("Cell should have returned 0 for value", 0,
                cell.getValue());
        assertFalse("Should return False as Cell has no value", cell.hasValue());
    }

    @Test
    public void testCellWrongValue() {
        cell = new Cell('a', 1, 1);
        assertEquals("Illegal value should still return 0 for cell", 0,
                cell.getValue());
    }

    @Test
    public void testRemoveCandidate() {
        cell.removeCandidate(7);
        assertFalse("Value should have been removed from Cell",
                cell.hasCandidate(7));

        cell.removeCandidate(3);
        assertFalse("Value should have been removed from Cell",
                cell.hasCandidate(3));

        cell.removeCandidate(100);
        assertFalse(
                "Value should have been ignored in remove AND return false",
                cell.hasCandidate(100));
    }

    @Test
    public void testReturnCandidates() {
        testCandidates = cell.returnCandidates();
        for (int i = 1; i <= 9; i++) {
            assertTrue("All returned candidates should match",
                    testCandidates.contains(i));
        }
        cell.removeCandidate(4);
        cell.removeCandidate(9);

        testCandidates = cell.returnCandidates();

        assertTrue("Returned candidates should match - 3",
                testCandidates.contains(3));
        assertTrue("Returned candidates should match - 5",
                testCandidates.contains(5));
        assertFalse("Returned candidates should not contain 4",
                testCandidates.contains(4));
    }

    @Test
    public void testReturnCandidatesOnCellWithValue() {
        cell = new Cell(4, 1, 1);
        testCandidates = cell.returnCandidates();
        for (int i = 1; i <= 9; i++) {
            assertFalse(
                    "Returned candidates should contain no numbers on Cell with a Value",
                    testCandidates.contains(i));
        }
    }

    @Test
    public void matchingCandidates() {
        testCandidates.add(5);
        testCandidates.add(9);
        testCandidates.add(1);

        assertEquals(
                "Fresh Cell with no value should contain all three candidates",
                3, cell.matchingCandidates(testCandidates));
        cell.removeCandidate(5);
        assertEquals(
                "Cell with all candidates except removed '5' should contain only 2 candidates from list",
                2, cell.matchingCandidates(testCandidates));
    }

    @Test
    public void testAssignOnlyCandidate() {
        for (int i = 1; i <= 9; i++) {
            if (i != 7) {
                cell.removeCandidate(i);
            }
        }
        assertEquals("Cell value should start at 0 before method", 0,
                cell.getValue());
        cell.assignOnlyCandidate();
        assertEquals("Cell value should be the only value left - 7", 7,
                cell.getValue());
    }

    @Test
    public void testClearCandidates() {
        assertEquals("Fresh Cell should start with 9 candidates", 9,
                cell.getNumCandidates());
        cell.clearCandidates();
        assertEquals("Cell should have 0 candidates after clear", 0,
                cell.getNumCandidates());
    }

    @Test
    public void testHasCandidate() {
        cell = new Cell(-1, 1, 1);
        assertTrue("Candidate 7 should exist on new fresh cell",
                cell.hasCandidate(7));
        assertTrue("Candidate 1 should exist on new fresh cell",
                cell.hasCandidate(1));
        assertTrue("Candidate 9 should exist on new fresh cell",
                cell.hasCandidate(9));
        cell.removeCandidate(9);
        assertFalse("Candidate 9 should not exist after Candidate removale",
                cell.hasCandidate(9));
    }

    @Test
    public void testGetNumCandidates() {
        assertEquals("Number of candidates should be 9", 9,
                cell.getNumCandidates());
        cell.removeCandidate(4);
        cell.removeCandidate(9);
        cell.removeCandidate(2);
        assertEquals("Number of candidates should be 6", 6,
                cell.getNumCandidates());
        cell.setValue(5);
        assertEquals("Number of candidates should be 0", 0,
                cell.getNumCandidates());
    }

    @Test
    public void testHasValue() {
        assertFalse("Cell should not have a value", cell.hasValue());
        cell = new Cell(5, 1, 1);
        assertTrue("Cell should contain value - 5", cell.hasValue());
    }

    @Test
    public void testSetValue() {
        cell = new Cell(6, 1, 1);
        assertEquals("Original Cell value should be 6", 6, cell.getValue());
        cell.setValue(1);
        assertEquals("Cell value should have been updated to 1", 1,
                cell.getValue());
    }

    @Test
    public void testGetValue() {
        cell = new Cell(6, 1, 1);
        assertEquals("Original Cell value should be 6", 6, cell.getValue());
        cell.setValue(9);
        assertEquals("Cell value should have been updated to 9", 9,
                cell.getValue());
    }

    @Test
    public void testGetRow() {
        assertEquals("Row should be 5", 5, cell.getRow());
    }

    @Test
    public void testGetColumn() {
        assertEquals("Column should be 1", 1, cell.getColumn());
    }

    @Test
    public void testGetAndSetBlock() {
        cell.setBlock(7);
        assertEquals("Block should be 7", 7, cell.getBlock());
    }

    @Test
    public void testFirstValueNoValue() {
        cell = new Cell(0, 1, 1);
        assertFalse("Cell should not be known as a first value",
                cell.firstValue());
        cell.setValue(6);
        assertFalse("Cell should not be known as a first value",
                cell.firstValue());
    }

    @Test
    public void testFirstValueWithValue() {
        cell = new Cell(5, 1, 1);
        assertTrue("Cell should be known as a first value", cell.firstValue());
    }

    @Test
    public void testTestBounds() {
        assertFalse("Cell should not accept out of bounds numbers - high 1000",
                cell.testBounds(1000));
        assertFalse("Cell should not accept out of bounds numbers - low -400",
                cell.testBounds(-400));
        assertTrue("Cell should accept regular value - 8",
                cell.testBounds(8));
    }

    @Test
    public void testEquals() {
        assertTrue(
                "Method should return True when comparing Cell against itself",
                cell.equals(cell));
        assertFalse(
                "Method should return False when comparing Cell against a different Cell in a different row/column",
                cell.equals(new Cell(0, 3, 7)));
    }
}
