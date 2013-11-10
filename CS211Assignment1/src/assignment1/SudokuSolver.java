package assignment1;

/**
 * <h2>SudokuSolver</h2>
 * <p>
 * The top level of the model, from where the Model is created and it's
 * attributes for the particular Sudoku puzzle loaded are applied, along with
 * any actions to take during solving. <br />
 * This class holds the solving method that calls on each individual solving
 * algorithm as and when they are needed from the SudokuModel.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
public class SudokuSolver {

    private SolverModel grid;
    private Result result;
    private static final int SUDOKU_SPACES = 9;
    /**
     * mRows and mColumns are the 'modifiers' used
     * in Pointing Pairs, to determine if the method
     * is being used on Rows or Columns.
     * More can be seen on this in SudokuModel.
     */
    private static final int mRows = 1;
    private static final int mColumns = 3;
    private int steps;
    private StringBuffer sb;

    /**
     * <p>
     * Constructs the SolverModel and sets it up to have a set of blank Cells in
     * preparation for loading .sud files with come cells filled.
     * </p>
     */
    SudokuSolver() {
        grid = new SolverModel();
        sb = new StringBuffer();
        result = new Result();
        grid.setCells(new int[9][9]);
        steps = 0;
    }

    /**
     * <p>
     * Sends a 2D Character array to a method to be converted into a 2D Integer
     * array and then sends this as an argument to the SudokuModel class to be
     * set as the current Cells to work with for solving a puzzle. <br />
     * The char array comes from reading in a File where all text is stored as
     * char/Strings and needs converting in order to be used in the sudoku grid
     * as Integers with values.
     * </p>
     * 
     * @param lines
     *            - 2D Character array that will be converted into an array of
     *            Integers in this class (see 'convertChar(char[][])') and sent
     *            to the SudokuModel.
     */
    public void setGrid(char[][] lines) {
        grid.setCells(this.convertChar(lines));
    }

    /**
     * <p>
     * A request to get the SudokuModel's grid, used by classes holding
     * reference to this class but not directly to the SudokuModel.
     * </p>
     * 
     * @return The Grid from SudokuModel
     */
    public Cell[][] getGrid() {
        return grid.getGrid();
    }

    /**
     * <p>
     * When the request to 'take a step' in solving the Sudoku puzzle is called,
     * this method runs through the order of priority the solving algorithms
     * should be called in, and is written such that only one step can be taken
     * at a time, and only after those that have come before it have failed to
     * succeed.
     * </p>
     * <p>
     * In light of Complexity, I feel there is likely a much more efficient way
     * of handling this request than 'if this, then do this', as if we wish to
     * use the most advanced techniques, we must first attempt every other
     * technique before it, adding complexity to each failed result. This would
     * be addressed with more time on the project, however, my main goal was to
     * address the data structures and individual solving algorithms on their
     * own.
     * </p>
     * <p>
     * Each method creates it's own Result from running the method. More
     * information can be seen about this in the 'Result' class. However, these
     * contain information on whether the operation was a success, what message
     * to show the user and any Cells that were affected with updated
     * Values/solved. <br />
     * With more time, I would expand into highlighting in text where the Cells
     * were updated, and also visually show which candidates would be removed.
     * </p>
     * 
     * @return A String containing the step taken in order to solve the next
     *         section of the grid.
     */
    public String takeStep() {
        result = grid.removeCollectionCandidates();
        if (!result.getSuccess()) {
            result = grid.hiddenSingles();
            if (!result.getSuccess()) {
                result = grid.nakedSingles();
                if (!result.getSuccess()) {
                    result = grid.pointingPairs(mRows);
                    if (!result.getSuccess()) {
                        result = grid.pointingPairs(mColumns);
                        if (!result.getSuccess()) {
                            result = grid.nakedPairsAndTriples();
                            if (!result.getSuccess()) {
                                result = new Result();
                            }
                        }
                    }
                }
            }
        }
        successfulMethod(result.getMessage());
        return sb.toString();
    }

    /**
     * <p>
     * Defines what a 'row', 'column' and 'block' is, adding each of them to a
     * single LinkedList holding all types. <br />
     * Ordering: Row, Column, Row, Column, etc, ending with Column, Block,
     * Block.. Block.
     * </p>
     */
    public void setup() {
        grid.defineRowsAndColumns();
        grid.defineBlocks();
    }

    /**
     * <p>
     * After being passed a 2D array of Characters, the method creates a new 2D
     * array of Integers and begins converting the values. <br />
     * The conversion is as simple as getting the numerical value of the
     * character, rather than it's Hex, Decimal or Octal value. The Numeric
     * value is quite literally 1 = 1, 2 = 2, 3 = 3, etc. <br />
     * Any blank spaces are converted to -1, which are dealt with when passed to
     * the SolverModel.
     * </p>
     * <p>
     * The filled and resulting 2D array of Integers converted from the 2D
     * Character array is returned to where it was called.
     * </p>
     * 
     * @param lines
     * @return
     */
    public int[][] convertChar(char[][] lines) {
        int[][] numbers = new int[SUDOKU_SPACES][SUDOKU_SPACES];
        for (int i = 0; i < SUDOKU_SPACES; i++) {
            for (int j = 0; j < SUDOKU_SPACES; j++) {
                numbers[i][j] = Character.getNumericValue(lines[i][j]);
            }
        }
        return numbers;
    }

    /**
     * <p>
     * Asks the SudokuModel if the puzzle is solved and returns a boolean answer
     * based on the response received.
     * </p>
     * 
     * @return
     */
    public boolean isSolved() {
        if (grid.solved()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * @return the amount of steps taken to completion so far.
     */
    public int getSteps() {
        return steps;
    }

    /**
     * <p>
     * Builds and returns the message based on the successful solving steps
     * taken.
     * </p>
     * 
     * @param msg
     */
    public void successfulMethod(String msg) {
        steps++;
        sb.append("Step: ");
        sb.append(steps);
        sb.append(msg);
    }

    /**
     * @return - The Result currently stored here from the last successful
     *         algorithm operation.
     */
    public Result returnResult() {
        return result;
    }

}
