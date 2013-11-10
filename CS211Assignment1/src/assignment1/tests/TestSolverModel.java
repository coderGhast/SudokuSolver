package assignment1.tests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import assignment1.Cell;
import assignment1.SolverModel;

public class TestSolverModel {

    private SolverModel solver;
    private Cell[][] testGrid;
    private int[][] book65;
    private int[][] book63;

    @Before
    public void before() {
        solver = new SolverModel();
        testGrid = new Cell[9][9];
    }

    public void prepBook65() {
        // Hardcoded 'Book65'
        book65 = new int[9][9];
        book65[0][3] = 3;
        book65[0][5] = 6;
        book65[0][8] = 7;
        book65[1][2] = 7;
        book65[1][5] = 1;
        book65[1][7] = 5;
        book65[2][1] = 1;
        book65[2][5] = 8;
        book65[3][0] = 9;
        book65[3][5] = 5;
        book65[3][6] = 3;
        book65[3][7] = 1;
        book65[3][8] = 6;
        book65[5][0] = 5;
        book65[5][1] = 3;
        book65[5][2] = 8;
        book65[5][3] = 6;
        book65[5][8] = 9;
        book65[6][3] = 9;
        book65[6][7] = 2;
        book65[7][1] = 6;
        book65[7][3] = 7;
        book65[7][6] = 4;
        book65[8][0] = 8;
        book65[8][3] = 1;
        book65[8][5] = 2;
        solver.setCells(book65);
        setup();
    }

    public void prepBook63() {
        book63 = new int[9][9];
        book63[0][2] = 7;
        book63[0][3] = 4;
        book63[0][8] = 2;
        book63[1][1] = 8;
        book63[1][4] = 1;
        book63[1][7] = 5;
        book63[2][0] = 4;
        book63[2][6] = 3;
        book63[3][0] = 8;
        book63[3][3] = 5;
        book63[3][6] = 2;
        book63[4][1] = 4;
        book63[4][4] = 2;
        book63[4][7] = 8;
        book63[5][2] = 3;
        book63[5][5] = 7;
        book63[5][8] = 6;
        book63[6][2] = 5;
        book63[6][8] = 4;
        book63[7][1] = 9;
        book63[7][4] = 6;
        book63[7][7] = 1;
        book63[8][0] = 6;
        book63[8][5] = 9;
        book63[8][6] = 7;
        solver.setCells(book63);
        setup();
    }

    public void setup() {
        solver.defineRowsAndColumns();
        solver.defineBlocks();
    }

    @Test
    public void testSetCells() {
        int[][] grid1 = new int[9][9];
        grid1[0][5] = 3;
        testGrid[0][5] = new Cell(3, 0, 5);
        solver.setCells(grid1);
        Cell[][] grid2 = solver.getGrid();
        assertEquals("Cells should match - Blank cells currently",
                testGrid[0][5], grid2[0][5]);
    }

    @Test
    public void testDefineRowsAndColumnsAndBlocks() {
        int[][] grid1 = new int[9][9];
        LinkedList<Cell> testCollection;
        grid1[4][2] = 3;
        solver.setCells(grid1);
        setup();
        LinkedList<LinkedList<Cell>> allCollections = solver
                .getAllCollections();
        testCollection = allCollections.get(8);
        assertEquals("Cells not equal. Rows should be set", 3, testCollection
                .get(2).getValue());
        testCollection = allCollections.get(5);
        assertEquals("Cells not equal. Columns should be set", 3,
                testCollection.get(4).getValue());
    }

    @Test
    public void testRemoveCollectionCandidates() {
        prepBook65();
        solver.removeCollectionCandidates();
        testGrid = solver.getGrid();
        assertFalse("Exisiting values should be removed from candidates (3)",
                testGrid[0][0].hasCandidate(3));
        assertFalse("Existing values should be removed from candidiates(7)",
                testGrid[0][0].hasCandidate(3));
        assertTrue("Unsolved cells should still have valid candidiates(4)",
                testGrid[0][0].hasCandidate(4));
    }

    @Test
    public void testNakedSingles() {
        prepBook65();
        for (int i = 0; i < 10; i++) {
            solver.removeCollectionCandidates();
            solver.hiddenSingles();
        }
        solver.removeCollectionCandidates();
        testGrid = solver.getGrid();
        assertFalse(
                "Cell should have no value until Naked Singles method executed",
                testGrid[0][6].hasValue());
        solver.nakedSingles();
        assertTrue(
                "Cell should have value after Naked Singles method execture",
                testGrid[0][6].hasValue());
        assertEquals("Cell should hold solved value - 1", 1,
                testGrid[0][6].getValue());
    }

    @Test
    public void testNakedPairsAndTriples() {
        prepBook63();
        solver.removeCollectionCandidates();
        solver.hiddenSingles();
        solver.removeCollectionCandidates();
        testGrid = solver.getGrid();
        assertTrue("Cell should have candidiate - 9",
                testGrid[0][6].hasCandidate(9));
        solver.nakedPairsAndTriples();
        assertFalse(
                "Cell should no longer have candidiate after NakedPairs - 9",
                testGrid[0][6].hasCandidate(9));
    }

    @Test
    public void testPointingPairs() {
        prepBook65();
        for (int i = 0; i < 25; i++) {
            if (!solver.removeCollectionCandidates().getSuccess()) {
                if (!solver.hiddenSingles().getSuccess()) {
                    solver.nakedSingles();
                }
            }
        }
        testGrid = solver.getGrid();
        assertTrue("Cell should have candidiate - 8",
                testGrid[4][3].hasCandidate(8));
        solver.pointingPairs(1); // 1 is the modifier for using pointingPairs on
                                 // Rows.
        assertFalse(
                "Cell should no longer have candidiate after pointingPairs - 8",
                testGrid[4][3].hasCandidate(8));
    }

    @Test
    public void testHiddenSingles() {
        prepBook65();
        solver.removeCollectionCandidates();
        testGrid = solver.getGrid();
        assertFalse("Cell should have no value", testGrid[7][5].hasValue());
        solver.hiddenSingles();
        assertTrue("Cell should now have value", testGrid[7][5].hasValue());
        assertEquals("Cell should contain value - 3", 3,
                testGrid[7][5].getValue());
    }

    @Test
    public void testSolved() {
        prepBook65();
        assertFalse("Grid should not be solved before any method applied",
                solver.solved());
        while (!solver.solved()) {
            if (!solver.removeCollectionCandidates().getSuccess()) {
                if (!solver.hiddenSingles().getSuccess()) {
                    if (!solver.nakedSingles().getSuccess()) {
                        if (!solver.pointingPairs(1).getSuccess()) {
                            if (!solver.pointingPairs(3).getSuccess()) {
                                if (!solver.nakedPairsAndTriples().getSuccess()) {
                                }
                            }
                        }
                    }
                }
            }
        }
        assertTrue("Grid should be solved (all cells values are applied/true",
                solver.solved());
    }

}
