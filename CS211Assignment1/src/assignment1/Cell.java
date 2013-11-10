package assignment1;

import java.util.LinkedList;

/**
 * <h2>Cell</h2>
 * <p>
 * Cell class holds all information needed for this implementation of the
 * SudokuSolver problem.<br />
 * The Cell holds data about it's position on the grid (row, column), what block
 * it is in, what candidates it has got, whether it was a value passed in on the
 * first setup of the grid or a solved Cell and any value it may contain (0-9).
 * </p>
 * <p>
 * It's methods deal with assigning values, such as its respective value and
 * attributes connected to its location on the grid and removing candidates. <br
 * />
 * <p>
 * There are also a number of methods for other classes to call upon to get
 * answers about the cells current status, such as whether it has a candidate,
 * what candidates it has, whether it has a value and if it is the same (in the
 * same location) as another Cell.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
public class Cell {

    private int value = 0;
    private int block = 0;
    private int row = 0;
    private int column = 0;
    private int[] candidates;
    private boolean firstValue;

    /**
     * <p>
     * The constructor is passed parameters of the potential value of the cell
     * and which row and column it belongs to on the grid.
     * </p>
     * <p>
     * From this information, the cell will either be set up with a list of
     * candidates (1 - 9) or will assign the value passed if given a legal
     * value. If a value is set in this way, it is also noted for use in the GUI
     * and setting the colour of all original values to different than that of
     * the solved Cells.
     * </p>
     * 
     * @param num
     * @param row
     * @param column
     */
    public Cell(int num, int row, int column) {
        candidates = new int[9];
        this.row = row;
        this.column = column;

        if (num < 1 || num > 10) {
            value = 0;
            for (int i = 0; i < 9; i++) {
                candidates[i] = (i + 1);
            }
        } else {
            value = num;
            firstValue = true;
        }

    }

    /**
     * <p>
     * Takes in a number to be removed from the list of candidates, checks that
     * the value is legal and then removes it from the correct element of the
     * array (- 1 added to remove the correct value with arrays beginning at 0
     * for candidate 1.
     * </p>
     * 
     * @param candidate
     *            - to be removed from the list
     */
    public void removeCandidate(int candidate) {
        if (this.testBounds(candidate)) {
            candidates[candidate - 1] = 0;
        }
    }

    /**
     * <p>
     * Goes through the list of candidates in the candidates array and adds them
     * to a LinkedList if the number's value is higher than 0, i.e. exists as a
     * valid candidiate.
     * </p>
     * 
     * @return toReturn - a list of the candidates, stripped of any 0's in the
     *         array they are held in here.
     */
    public LinkedList<Integer> returnCandidates() {
        LinkedList<Integer> toReturn = new LinkedList<Integer>();
        for (int i = 0; i < candidates.length; i++) {
            if (candidates[i] > 0) {
                toReturn.add(candidates[i]);
            }
        }
        return toReturn;
    }

    /**
     * <p>
     * A method that compares and counts the amount of matching candidates in
     * the candidates list here in the cell and the passed list by looking
     * through the array and finding numbers with a value higher than 0 and
     * incrementing a counter on each match.
     * </p>
     * 
     * @param toMatch
     *            - List with values to compare to the candidates in the cell.
     * @return amount - The amount of matches between the passed candidates and
     *         the candidates in this cell.
     */
    public int matchingCandidates(LinkedList<Integer> toMatch) {
        int amount = 0;
        for (int check : toMatch) {
            if (check > 0) {
                if (this.hasCandidate(check)) {
                    amount++;
                }
            }
        }
        return amount;
    }

    /**
     * <p>
     * On request of this method, the list of candidates is iterated through and
     * the value found above 0 is applied to be the value of this cell. <br />
     * Checks that there is only one candidate before applying the core
     * function.
     * </p>
     */
    public void assignOnlyCandidate() {
        if (this.getNumCandidates() == 1) {
            for (int i = 0; i < candidates.length; i++) {
                if (candidates[i] != 0) {
                    this.setValue(candidates[i]);
                }
            }
        }
    }

    /**
     * <p>
     * Sets all candidates in the array to '0', where '0' represents 'nothing'.
     * </p>
     */
    public void clearCandidates() {
        for (int i = 0; i < 9; i++) {
            candidates[i] = 0;
        }
    }

    /**
     * <p>
     * First checks to see if the value passed would be out of the array bounds
     * of the cell. If the test is passed, the value is passed to the array with
     * -1 to check the element location where the value would be. If the value
     * matches the expected value (e.g. element 5, value 6, given value 6, then
     * the method returns true.
     * </p>
     * 
     * @param candidate
     *            - Number to check
     * @return - Returns a boolean whether the candidate exists in this cell or
     *         not.
     */
    public boolean hasCandidate(int candidate) {
        if (this.testBounds(candidate)) {
            if (candidates[candidate - 1] == candidate) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * <p>
     * Checks through the array of candidates and checks to see which values are
     * above 0. Any above 0 are considered a valid candidate and a counter is
     * incremented to represent this. Once the iteration is completed, the
     * method returns how many candidates it found.
     * </p>
     * 
     * @return numCandidates - The amount of candidates contained in the array
     *         of candidates.
     */
    public int getNumCandidates() {
        int numCandidates = 0;
        for (int candidate : candidates) {
            if (candidate != 0) {
                numCandidates++;
            }
        }
        return numCandidates;
    }

    /**
     * <p>
     * Checks if the number in the variable 'value' is not 0. Logically, if it
     * is not 0, it should be a valid value. <br />
     * The method returns whether this value is 0 or the cell contains an actual
     * value.
     * </p>
     * 
     * @return boolean value of if the cell has a value or not
     */
    public boolean hasValue() {
        if (value != 0) {
            return true;
        }
        return false;
    }

    /**
     * <p>
     * Sets the passed value as the value for this cell and then clears the
     * candidate list to be sure that any other class later requesting
     * candidates doesn't confuse this cell with an assigned value with an
     * unsolved cell.
     * </p>
     * 
     * @param value
     *            - The 'solved' value for the cell to hold
     */
    public void setValue(int value) {
        this.value = value;
        this.clearCandidates();
    }

    /**
     * @return the cell value.
     */
    public int getValue() {
        return value;
    }

    /**
     * @return the row the cell is on.
     */
    public int getRow() {
        return row;
    }

    /**
     * @return the column the cell is on.
     */
    public int getColumn() {
        return column;
    }

    /**
     * @return the block the cell belongs to.
     */
    public int getBlock() {
        return block;
    }

    /**
     * <p>
     * Sets the block the cell belongs to from the passed integer.
     * </p>
     * 
     * @param block
     *            the block this cell should belong to.
     */
    public void setBlock(int block) {
        this.block = block;
    }

    /**
     * @return boolean whether this cell was assigned on the inital read in of
     *         the grid or a later solved cell.
     */
    public boolean firstValue() {
        return firstValue;
    }

    /**
     * <p>
     * Tests whether a value is valid (between 0 and up to and including 9).
     * </p>
     * 
     * @param num
     *            - passed value to check
     * @return boolean if the value passed the test.
     */
    public boolean testBounds(int num) {
        if (num < 10 && num > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Overwritten equals method: Implementation only requires that the Cells in
     * comparison would occupy the same row/column to be known as 'equal', i.e.
     * The same Cell.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     * 
     */
    @Override
    public boolean equals(Object checker) {
        Cell cellCheck = (Cell) checker;
        if (cellCheck.getRow() == row && cellCheck.getColumn() == column) {
            return true;
        } else {
            return false;
        }

    }

}
