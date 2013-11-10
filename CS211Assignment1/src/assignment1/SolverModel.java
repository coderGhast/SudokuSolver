package assignment1;

import java.util.LinkedList;
import java.util.Stack;

/**
 * <h2>SolverModel</h2>
 * <p>
 * Deals with all operations upon the values and candidates of the Cells, in
 * respect to their location and status within the 'Sudoku' grid. <br />
 * Many different algorithms are used in order to solve a variety of different
 * sudoku puzzles, some easy and some a little tougher. This is the 'heart' of
 * the SudokuSolver.
 * </p>
 * <p>
 * Many algorithms are self contained, and attempt to use the same code for both
 * Rows and Columns. Some methods grew larger and needed to be split into
 * multiple methods (i.e. PointingPairs - Large If statement conditions), for
 * ease of maintainability and explanation.
 * </p>
 * <p>
 * Collection tends to refer to a Row, Column or Block. <br />
 * Each method returns a 'Result', containing it's success, any cells affected
 * and any message attached to the success. More can be seen on this in the
 * JavaDoc for 'Result.class'.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
public class SolverModel {

    private Cell[][] grid = new Cell[9][9];
    private Cell cell;
    private Result result;
    private static final int SUDOKU_SPACES = 9;
    private static final int BLOCK_SPACES = 3;
    private static final int MAX_CELLS = 81;
    private LinkedList<Cell> collection;
    private LinkedList<LinkedList<Cell>> allCollections;
    private StringBuffer sb;

    /**
     * <p>
     * Constructor prepares class for use by making new instances of
     * 'allCollections', which holds all rows, columns and blocks as ordered
     * lists.
     * </p>
     */
    public SolverModel() {
        allCollections = new LinkedList<LinkedList<Cell>>();
    }

    /**
     * <p>
     * What values, including 0, to set to particular Cells to build the current
     * Sudoku puzzle.
     * </p>
     * 
     * @param lines
     *            - Cells as read in by FileHandler.
     */
    public void setCells(int[][] lines) {
        for (int i = 0; i < SUDOKU_SPACES; i++) {
            for (int j = 0; j < SUDOKU_SPACES; j++) {
                cell = new Cell(lines[i][j], i, j);
                grid[i][j] = cell;
            }
        }
    }

    /**
     * <p>
     * Through the use of a nested for loop, gets what would be each Cell in a
     * 'Row' or 'Column' (depdning on 'direction' of i and j) and adds them into
     * the overall list of collections (Row, Column, Block).
     * </p>
     */
    public void defineRowsAndColumns() {
        for (int i = 0; i < SUDOKU_SPACES; i++) {
            LinkedList<Cell> collectionRow = new LinkedList<Cell>();
            LinkedList<Cell> collectionColumn = new LinkedList<Cell>();
            for (int j = 0; j < SUDOKU_SPACES; j++) {
                collectionRow.add(grid[i][j]);
                collectionColumn.add(grid[j][i]);
            }
            allCollections.add(collectionRow);
            allCollections.add(collectionColumn);
        }
    }

    /**
     * <p>
     * Similar to defineRowsAndColumns, yet involving slightly different use of
     * nested for loops, as each block starts, continues for 3 cells, then moves
     * down a row (or across a column, depending how you build it. This example
     * starts and iterates over rows).
     * </p>
     * <p>
     * By using multiple nested for loops, and knowing that each block is size
     * of 3, we can increment through the Cells relatively easy. <br />
     * For example, we know that Block 2 (last on top Row), starts at, and
     * includes, cell (0, 6), which would be reached by rowStart = n *
     * Block_Spaces, or, 2 * Block_Spaces = 6. From there, it's a simple case of
     * iterating through the grid to grab the rows in each block below the
     * starting block when we know where to start and that no block goes more
     * than +3 in either rows or columns from the start.
     * </p>
     */
    public void defineBlocks() {
        int rowStart;
        int columnStart;
        int block = 0;
        for (int n = 0; n < BLOCK_SPACES; n++) {
            for (int k = 0; k < BLOCK_SPACES; k++) {
                rowStart = n * BLOCK_SPACES;
                columnStart = k * BLOCK_SPACES;
                collection = new LinkedList<Cell>();
                for (int i = rowStart; i < (rowStart + BLOCK_SPACES); i++) {
                    for (int j = columnStart; j < (columnStart + BLOCK_SPACES); j++) {
                        collection.add(grid[i][j]);
                        grid[i][j].setBlock(block);
                    }
                }
                allCollections.add(collection);
                block++;
            }
        }

    }

    /**
     * @return The full list of Row, Column and Blocks.
     */
    public LinkedList<LinkedList<Cell>> getAllCollections() {
        return allCollections;
    }

    /**
     * <p>
     * Removes candidates from each Cell based on what candidates currently
     * exist in the grid. <br />
     * Searches the grid and adds existing values to a list, then goes through
     * this list and removes any cells that hold them as candidates in each type
     * of collection.
     * </p>
     * 
     * @return Result of operation.
     */
    public Result removeCollectionCandidates() {
        result = new Result();
        for (LinkedList<Cell> currentCollection : allCollections) {
            LinkedList<Integer> existsInCollection = new LinkedList<Integer>();
            for (Cell currentCell : currentCollection) {
                if (currentCell.hasValue()) {
                    existsInCollection.add(currentCell.getValue());
                }
            }
            for (Cell currentCell : currentCollection) {
                for (int candidate : existsInCollection) {
                    if (!currentCell.hasValue()) {
                        if (currentCell.hasCandidate(candidate)) {
                            currentCell.removeCandidate(candidate);
                            result.setSuccess(true);
                            result.setMessage("<br />Checked existing values<br />Removed candidates<br />");
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * <p>
     * Standard Sudoku technique involving looking through each Cell in the grid
     * in a particular collection and finding those with a value that appears
     * within that Cell and only that Cell in that one particular collection,
     * even if it belongs to another collection that has many cells looking for
     * this candidate. <br />
     * Due to only one cell available for this candidate, it is only possible
     * for this cell to be this value, and so it is set to the cell.
     * </p>
     * 
     * @return Result of operation.
     */
    public Result nakedSingles() {
        result = new Result();
        Stack<Cell> stack = new Stack<Cell>();
        for (LinkedList<Cell> currentCollection : allCollections) {
            /*
             * For all collections, and all cells in that collection, iterate
             * and find the ones who have the candidate of the current valueNum
             * we are looking for.
             */
            for (int valueNum = 1; valueNum <= SUDOKU_SPACES; valueNum++) {
                stack.clear(); // Being sure to keep a clean stack on the start
                               // of new candidate/value checks.
                for (Cell currentCell : currentCollection) {
                    if (currentCell.hasCandidate(valueNum)) {
                        /*
                         * If a Cell is found to have the current searched for
                         * value, add Cell to stack.
                         */
                        stack.push(currentCell);
                    }
                }
                if (stack.size() == 1) {
                    /*
                     * If the stack size is one, we know that it must contain
                     * only one Cell from that collection currently searched,
                     * and so that Cell must be the only cell that can hold this
                     * particular value, so set it.
                     */
                    Cell c = stack.pop();
                    c.setValue(valueNum);
                    // Result generation.
                    result.setSuccess(true);
                    sb = new StringBuffer();
                    sb.append("<br />Checked for Naked Singles");
                    appendCellInfo(c);
                    result.setMessage(sb.toString());
                    result.addAffected(c);
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * <p>
     * The technique of nakedPairsAndTriples relies upon finding Cells who have
     * only two or three values, shared between one another, within a
     * collection. <br />
     * We can be assured that if we find two cells in the collection who share
     * the two same candidates, then the values must belong to these cells and
     * no others, so those candidates are removed from other cells in the
     * collection. <br />
     * In the case of finding three cells with three values, the same applies.
     * However, the same is also true if we find a cell that may have three
     * candidates, shared between two cells with only two candidates (e.g. {3,
     * 6, 9}, {6,9}, {3,9}). Even with this in case, we know that out of all
     * cells, only these three can really contain these values, as other cells
     * have other options. Once again, this leads to removing, this time three,
     * candidates from the other cells in the collection. <br />
     * The other possibility is that three cells appear with three candidates
     * split between them, but no one cell has three candidates to itself. My
     * algorithm here is not strong enough handle these situations. (e.g. {3,4},
     * {3,9}, {4,9} in the same collection).
     * </p>
     * 
     * @return Result of operation
     */
    public Result nakedPairsAndTriples() {
        result = new Result();
        for (LinkedList<Cell> currentCollection : allCollections) {
            for (int i = 0; i < SUDOKU_SPACES - 1; i++) {
                cell = currentCollection.get(i);
                if (!cell.hasValue()) {
                    if ((cell.getNumCandidates() > 1)
                            && (cell.getNumCandidates() < 4)) {
                        /*
                         * For each cell in each collection, find unsolved
                         * cells, if they have 2 or three candidates, add their
                         * candidates to a list of candidates to remove, and add
                         * the cell to a list of cells NOT to be altered when
                         * changing collection candidates.
                         */
                        LinkedList<Integer> toRemove = cell.returnCandidates();
                        LinkedList<Cell> doNotTouch = new LinkedList<Cell>();
                        doNotTouch.add(cell);

                        for (Cell otherCell : currentCollection) {
                            /*
                             * For all possible candidates, look through cells
                             * in our collection (that aren't our current first
                             * found cell with 2/3 candidates), and check to see
                             * if they have 2/3 candidates too. If they do,
                             * check if we have 2 or 3 matches to the current
                             * candidates to remove, in order to 'pair' up Cells
                             * with the same candidates.
                             */
                            if (!otherCell.equals(cell)) {
                                if (cell.matchingCandidates(otherCell
                                        .returnCandidates()) > 1
                                        && (cell.matchingCandidates(otherCell
                                                .returnCandidates()) < 4)
                                        && (otherCell.getNumCandidates() < 4)) {
                                    LinkedList<Integer> foundCandidates = otherCell
                                            .returnCandidates();
                                    /*
                                     * If some candidates are different, yet
                                     * there are still matches (e.g. {3,5,7} and
                                     * {3,7}), add the extra candidate to the
                                     * potential list of candidates to be
                                     * removed. Then add the newly found
                                     * potential pair to the list of candidates
                                     * not to be altered.
                                     */
                                    for (int candidate : foundCandidates) {
                                        if (!toRemove.contains(candidate)) {
                                            toRemove.add(candidate);
                                        }
                                    }
                                    doNotTouch.add(otherCell);
                                }
                            }
                        }
                        /*
                         * Check that our list of candidates to remove isn't
                         * empty, then if it is, send values to be evaluated and
                         * have candidates removed in another function for ease
                         * of maintainability. Then generate results.
                         */
                        if (!toRemove.isEmpty()) {
                            if(doNotTouch.size() == toRemove.size()){
                            result.setSuccess(nakedPairsAndTriplesRemoveCandidates(
                                    currentCollection, toRemove, doNotTouch));
                            result.setMessage("<br />Checked for Naked Pairs/Triples<br />Removed Candidates<br />");
                        }
                        }
                        /*
                         * Return lists to initial state to not confuse new
                         * pairs and collections with old pairs and collections.
                         */
                        toRemove = null;
                        doNotTouch.clear();
                    }
                }
            }
        }
        return result;
    }

    /**
     * <p>
     * Part of the nakedPairsAndTriples algorithm, we first go through each
     * candidate for potential removal and with this look at each cell in the
     * collection. <br />
     * For each cell, if it is unsolved, is not one of the Cells not to be
     * altered and that the amount of candidates to remove is equal to the
     * amount of cells not to be altered, remove the candidate from the cell.
     * </p>
     * <p>
     * The check of size of the list of Cells not to be altered versus the
     * amount of candidates to be removed is very important. It is this check
     * that ensures if we are removing anything, it is no more and no less than
     * we have pairs. If it were otherwise, it would mean that the values do not
     * work as pairs/triples.
     * </p>
     * 
     * @param currentCollection
     *            - Current Row/Column/Block
     * @param toRemove
     *            - candidates for potential removal
     * @param doNotTouch
     *            - Cells not to be altered
     * @return Result of operation.
     */
    public boolean nakedPairsAndTriplesRemoveCandidates(
            LinkedList<Cell> currentCollection, LinkedList<Integer> toRemove,
            LinkedList<Cell> doNotTouch) {
        boolean success = false;
        for (int candidate : toRemove) {
            for (Cell currentCell : currentCollection) {
                if (!currentCell.hasValue()){
                        if(!doNotTouch.contains(currentCell)) {
                    currentCell.removeCandidate(candidate);
                    success = true;
                }
                }
            }
        }
        return success;
    }

    /**
     * <p>
     * Checks for pairs within a row or column that 'point'
     * towards other Cells that hold candidates to be removed,
     * as the pair itself hold the candidates to be removed.
     * </p>
     * <p>
     * A 'pair' is looked for along the row or a column or 
     * each individual Block. If they are found to have matching
     * candidates, and only those have the candidate in the 
     * individual block, we know we can remove the candidates
     * from all other cells in the row/column, that the cells
     * appear in, outside of the box. They are 'pointing' at
     * cells that need candidates removed, hence the name.
     * </p>
     * <p>
     * The 'mod' parameter is a modifier that should either be
     * a 1 (rows) or 3 (columns). Using simple arithmetic, and
     * knowing that the size of a sudoku grid is always 9x9, a
     * block is always 3x3 and there is also a static amount of
     * Cells, columns, rows and blocks, the behaviour of the
     * method can be altered using this modifier.
     * </p>
     * @param mod - A modifier, either 1 or 3, representing rows
     * or columns, respectively.
     * @return The result of the operation
     */
    public Result pointingPairs(int mod) {
        result = new Result();
        String type = "Columns";
        if (mod == 1) {
            type = "Rows";
        }
        boolean removeCandidates = false;
        int modifier = mod;
        /*
         * For 'all blocks', technically, as blocks are added into the list
         * of allCollections last. Knowing there is only 9 blocks, and that
         * SUDOKU_SPACES is the same value as the amount of blocks, we can just
         * start our for loop from where the blocks start in this list
         */
        for (int n = allCollections.size() - SUDOKU_SPACES; n < allCollections
                .size(); n++) {
            LinkedList<Cell> currentBlock = allCollections.get(n);
            Cell firstCell = null;
            /*
             * Goes through and looks at all Cells in either a row or
             * a column of the Block, based on the modifier. We know that
             * if we have a list that looks like {0:0,0, 1:0,1, 2:0,2, 3:1,0, 4:1,1.. etc}
             * that in order to get a column from a normal for loop, we
             * need to add 3 onto the first element to get the second Cell
             * in the column. This is used in the if else checks.
             * The for loop itself uses a similar principle, except allowing the 
             * for loop to reach '8' for columns, while keeping the limit to 3
             * for the rows modifier(1). This can be tricky to understand at first,
             * attempt to draw the 3x3 grid on paper and work it out manually,
             * inserting the modifier.
             */
            for (int i = 0; i < (SUDOKU_SPACES / modifier); i = i
                    + (BLOCK_SPACES / modifier)) {
                boolean useFirst = true;
                /*
                 * Will get either a the first Cell in the Row or Column,
                 * or will get the second Cell if the first always has a value.
                 * Will never look to the last Cell in a Row or Column as
                 * there would be nothing to compare it to should the first
                 * two be illegal!
                 */
                if (!currentBlock.get(i).hasValue()) {
                    firstCell = currentBlock.get(i);
                    /*
                     * As stated before, this looks for the 'next' row Cell
                     * or column Cell based on the modifier, with the knowledge
                     * that each 'next' column cell is always 3 away from the last,
                     * while each row cell is always 1 away from the last.
                     */
                } else if (!currentBlock.get(i + (1 * modifier)).hasValue()) {
                    firstCell = currentBlock.get(i + (1 * modifier));
                    useFirst = false;
                }
                /*
                 * If a valid cell for pairing was found, send it to the method
                 * to compare with all other cells in the row/column of that
                 * block.
                 */
                if (firstCell != null) {
                    for (int candidate : firstCell.returnCandidates()) {
                        if (pointingPairsPairUp(currentBlock, modifier,
                                candidate, i, useFirst)) {

                            boolean foundCandidate = false;
                            for (Cell cellCheck : currentBlock) {
                                /*
                                 * If the cells match each others pairings, check
                                 * to see if the candidates they have matched up with
                                 * exist in any other Cell in the block, not just in
                                 * the Row or Column. If it does exist in another cell
                                 * outside of the collection, we know that these
                                 * cannot be a pointing pair.
                                 */
                                if (pointingPairsValidateCell(firstCell,
                                        cellCheck, modifier)) {
                                    if (cellCheck.hasCandidate(candidate)
                                            || cellCheck.getValue() == candidate) {
                                        /*
                                         * The 'getValue' check is necessary as
                                         * the current candidate to be removed
                                         * needs to be checked against
                                         * pre-existing values/solved cells in
                                         * the whole collection, not just in the
                                         * block (where it might not exist yet)
                                         * to see if it should not be removed at 
                                         * all and move onto  the next candidate value.
                                         */
                                        removeCandidates = false;
                                        foundCandidate = true;
                                    } else {
                                        removeCandidates = true;
                                    }
                                }

                            }
                            /*
                             * Should the checks of removing candidates
                             * and also not finding the candidates in other
                             * cells in the Block pass, then the method may
                             * continue to strip the candidates from other
                             * cells in the row/column that they 'point' to.
                             * 
                             * After this, the Result it updated to successful.
                             */
                            if (removeCandidates && !foundCandidate) {
                                if (pointingPairsStripCandidates(modifier,
                                        firstCell, candidate)) {
                                    result.setSuccess(true);
                                    sb = new StringBuffer();
                                    sb.append("<br />Checked for Pointing Pairs (");
                                    sb.append(type);
                                    sb.append(") <br />Removed Candidates<br />");
                                    result.setMessage(sb.toString());
                                    removeCandidates = false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * <p>
     * Part of pointingPairs - removed to break up the code
     * and make it easier to read and maintain.
     * <br />
     * Checks if the two Cell to potentially be paired up
     * in the Row/Column both don't have a value and share the
     * candidate expected.
     * <br />
     * Use first refers to whether the first Cell in the Row/Column
     * was selected, or if the second one was. The if statement
     * handles both of these outcomes with an OR statement, so
     * that if either of these is true, then it may indeed be
     * a pair.
     * </p>
     * @param currentBlock - The block to be checked
     * @param modifier - rows/columns modifier
     * @param candidate - What candidate to be expected and compared
     * @param i - the location of the Cells being compared in the grid, when used
     * with the modifier
     * @param useFirst - whether the first or second Cell is the paired Cell
     * to be checked, based on which was available.
     * @return
     */
    public boolean pointingPairsPairUp(LinkedList<Cell> currentBlock,
            int modifier, int candidate, int i, boolean useFirst) {
        Cell pairCell1 = currentBlock.get(i + (1 * modifier));
        Cell pairCell2 = currentBlock.get(i + (2 * modifier));
        if (((!pairCell1.hasValue() && pairCell1.hasCandidate(candidate)) && useFirst)
                || ((!pairCell2.hasValue()) && (pairCell2
                        .hasCandidate(candidate)))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>
     * Checks that the Cell being passed does not belong
     * on the same Row or Column as the Pair, ensuring that
     * candidates are not removed from the pair or that
     * candidates are valid (i.e. contained only in that
     * one row/column and not any others in the block).
     * </p>
     * @param firstCell - First cell for pairing check
     * @param cellCheck - Cell to validate whether the
     * candidate is in the single Row/Column or elsewhere
     * in the block.
     * @param modifier - Rows/Columns modifier
     * @return - Whether the validation was true or false.
     */
    public boolean pointingPairsValidateCell(Cell firstCell, Cell cellCheck,
            int modifier) {
        if (((modifier == 1) && cellCheck.getRow() != firstCell.getRow())
                || ((modifier == BLOCK_SPACES) && cellCheck.getColumn() != firstCell
                        .getColumn())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>
     * In order to split larger chunks of
     * code up from pointingPairs() and make
     * it more maintainable, there is this method
     * that strips the candidates from the other
     * Cells in the Row/Column should the previous
     * tests and validations have passed.
     * </p>
     * @param modifier
     * @param firstCell
     * @param candidate
     * @return
     */
    public boolean pointingPairsStripCandidates(int modifier, Cell firstCell,
            int candidate) {
        boolean success = false;
        /*
         * Based on the modifier, the if statement determines where
         * the row or column is in the list of allCollections.
         * Due to them being added alternatively (Row, Column, Row, Column),
         * it was necessary to use the modifier to add an additional 1 onto
         * the .get command from allCollections, as the Column that might be
         * Column 8, will actually be stored at *2 (as there are Rows there too,
         * doubling the amount of collections), and then +1, next after the Row
         * at location *2.
         */
        if (modifier == 1) {
            collection = allCollections.get(firstCell.getRow() * 2);
        } else {
            collection = allCollections.get((firstCell.getColumn() * 2) + 1);
        }
        for (Cell cell : collection) {
            if (!cell.hasValue()) {
                /*
                 * As long as the Cell in the collection (row/block)
                 * in particular is not in the same block as the
                 * Pointing Pair (so the Pair that cause this event),
                 * remove that candidate as we know it cannot be legal
                 * for this Cell if only the pointing pair can be
                 * this value in their block.
                 */
                if ((cell.getBlock() != firstCell.getBlock())
                        && (cell.hasCandidate(candidate))) {
                    cell.removeCandidate(candidate);
                    success = true;
                }
            }
        }
        return success;
    }

    /**
     * <p>
     * Hidden Singles in Sudoku looks for any cells that have only one candidate
     * left in their candidates list. If they only have one candidate option, it
     * means they must be this candidate without a doubt (presuming correct
     * solutions to this point).
     * </p>
     * 
     * @return - Result of the operation.
     */
    public Result hiddenSingles() {
        result = new Result();
        for (int i = 0; i < SUDOKU_SPACES; i++) {
            for (Cell currentCell : allCollections.get(i * 2)) {
                if (!currentCell.hasValue()) {
                    if (currentCell.getNumCandidates() == 1) {
                        /*
                         * For all cells in all rows (as what type of collection
                         * doesn't matter as this is solved on a cell to cell
                         * basis) check if there is only one option for a cell
                         * to be out of their candidates. If yes, set it to this
                         * value.
                         */
                        currentCell.assignOnlyCandidate();
                        result.setSuccess(true);
                        result.addAffected(currentCell);
                        result.setMessage("<br />Checked for Hidden Singles<br />");
                    }
                }
            }
        }
        return result;
    }

    /**
     * <p>
     * Checks all Cells in the puzzle. If each one of them has a value (based on
     * a counter increasing on each positive value found, then the method
     * returns true. <br />
     * MAX_CELLS = 81, the full Sudoku grid cells.
     * </p>
     * 
     * @return boolean referring to is the puzzle has been solved or not.
     */
    public boolean solved() {
        int solvedCells = 0;
        for (int i = 0; i < SUDOKU_SPACES; i++) {
            for (int j = 0; j < SUDOKU_SPACES; j++) {
                if (grid[i][j].hasValue()) {
                    solvedCells++;
                }
            }
        }
        if (solvedCells == MAX_CELLS) {
            return true;
        } else {
            return false;
        }
    }

    // ---------------------------------------------------------

    /**
     * @return the current full grid of Cells.
     */
    public Cell[][] getGrid() {
        return grid;
    }

    /**
     * <p>
     * Appends common information to a StringBuffer to be set and returned in
     * the result. In particular, information concerning cells and their
     * location.
     * </p>
     * 
     * @param cell
     *            - current cell modified
     * @return the completed and concatanated String
     */
    public String appendCellInfo(Cell cell) {
        sb.append("<br />Updated: ");
        sb.append(cell.getRow() + 1);
        sb.append(", ");
        sb.append(cell.getColumn() + 1);
        sb.append("<br /");
        return sb.toString();
    }

    /**
     * <p>
     * My original way of viewing the steps and results taken as the Sudoku
     * puzzle solves. Left in for sake of re-usability should the GUI need to be
     * removed. <br />
     * Prints out the current status of the board, with dividers for the Blocks
     * and underscores for unsolved cells. Does not display candidates.
     * </p>
     */
    public void printLines() {
        for (int i = 0; i < SUDOKU_SPACES; i++) {
            if ((i + 1 / BLOCK_SPACES) % BLOCK_SPACES == 0) {
                for (int k = 0; k < 7; k++) {
                    System.out.print("_ ");
                }
                System.out.println();
            }
            for (int j = 0; j < SUDOKU_SPACES; j++) {
                if ((j + 1 / BLOCK_SPACES) % BLOCK_SPACES == 0) {
                    System.out.print('|');
                }
                if (grid[i][j].getValue() == 0) {
                    System.out.print('-');
                } else {
                    System.out.print(grid[i][j].getValue());
                }
            }
            System.out.print("| \n");
        }

    }

}
