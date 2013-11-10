package assignment1;

/**
 * <h2>Driver</h2>
 * <p>
 * A 'between' class that goes between the main classes the
 * Main method class, keeping the classes in use private.
 * <br />
 * A reference to SudokuSolver is passed to the window
 * to begin with and help setup the SolverFrame GUI.
 * This will be overwritten later as a 
 * new Sudoku puzzle is opened however.
 * </p>
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
public class Driver {
    @SuppressWarnings("unused")
    private SolverFrame window;
    private SudokuSolver solver;
    
    /**
     * <p>
     * Creates instances of the solver and GUI viewer.
     * </p>
     */
    public Driver(){
        solver = new SudokuSolver();
        window = new SolverFrame(solver);
    }
}
