package assignment1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * <h2>SudokuCanvas</h2>
 * <p>
 * The visual JPanel that holds the Cells of the Sudoku puzzle grid and displays
 * them and their status to the user.
 * </p>
 * <p>
 * This class is controlled by the SolverFrame, and holds reference to the
 * SudokuSolver, allowing it to get data directly from the Solver, and pass on
 * requests from the SudokuFrame when buttons to take solving steps are clicked.
 * </p>
 * <p>
 * Given more time I would work more on this section of the application,
 * displaying references to each cell and assigning them 'names', e.g. A1, A2,
 * C4, etc. Due to time constraints, I kept the canvas as simple yet informative
 * of the solving steps taking place as I could.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SolverCanvas extends JPanel implements Runnable {
    private SudokuSolver solver;
    private FileHandler fileH;
    private boolean gridLoaded = false;
    private boolean keepSolving = false;
    private Result result;

    // ===== GUI ELEMENTS =====
    private JLabel cellLabel;
    private JLabel[][] cells;
    private String textValue;
    private Font lFont;
    private Font sFont;
    private JLabel stepsLabel;

    private int largeText = 30;
    private int smallText = 12;

    private Cell[][] grid;
    private StringBuffer sb;
    private StringBuffer stepB;

    private static final int SUDOKU_SPACES = 9;

    /**
     * <p>
     * Constructor creates instances of variables needed to allow the 'canvas'
     * piece to function and display correctly.
     * </p>
     * 
     * @param solver
     *            - a reference of the solver created on the applications start
     *            up.
     */
    SolverCanvas(SudokuSolver solver) {
        this.solver = solver;
        cells = new JLabel[SUDOKU_SPACES][SUDOKU_SPACES];
        lFont = new Font("Arial", Font.BOLD, largeText);
        sFont = new Font("Arial", Font.PLAIN, smallText);
        stepsLabel = new JLabel();
        stepsLabel.setVerticalAlignment(SwingConstants.TOP);
        stepsLabel.setLayout(new GridLayout());
        stepsLabel.setVerticalAlignment(SwingConstants.TOP);
        stepsLabel.setLayout(new GridLayout());
        setupCanvas();
    }

    /**
     * <p>
     * Sets up various things for this canvas, in order to help display the
     * actions and steps being taken in solving the problem.
     * </p>
     * <p>
     * In particular, the background is set to White to better display darker
     * coloured cells, and a GridLayout is used as it ensures uniform size to
     * each of the elements contained. In particular, the only things contained
     * are the 'cellLabel's, one for each Cell of the Sudoku grid. <br />
     * Each cellLabel is bordered to help define it's own bounds. To further the
     * usefulness of the display, the 3x3 'blocks' of the grid are displayed
     * with alternating colours (Gray, light gray), using an if statement to
     * determine whether the current cell is within a 'gray' block or otherwise.
     * <br />
     * This is discovered through use of the same nested for loop that is used
     * to create new cellLabels, up to the amount of Sudoku Cells.
     * </p>
     * <p>
     * Once the 'cells' are set up to be displayed, they are added to a 2D array
     * to keep hold of them and their respective locations in parallel to those
     * used in the SudokuModel class.
     * </p>
     */
    public void setupCanvas() {
        this.setBackground(Color.WHITE);
        this.setLayout(new GridLayout(SUDOKU_SPACES, SUDOKU_SPACES));
        for (int i = 0; i < SUDOKU_SPACES; i++) {
            for (int j = 0; j < SUDOKU_SPACES; j++) {
                cellLabel = new JLabel();
                cellLabel.setBorder(new LineBorder(Color.BLACK));
                cellLabel.setOpaque(true);

                colourCell(i, j, cellLabel);

                this.add(cellLabel);
                cells[i][j] = cellLabel;
            }
        }
    }

    /**
     * <p>
     * When passed a file, will create a new instance of the FileHandler class,
     * clear all Cells from the current state of this class and re-setup all for
     * a fresh 'canvas', read in the file and set the correct grid and Cells
     * based on the data read in and returned, calls for the Solver to setup
     * what it must and then requests from the Solver to get a copy of the grid
     * once setup to be displayed to the user. <br />
     * gridLoaded keeps the class aware of if there is currently a grid on
     * display or not, used mainly for the first time the application is opened,
     * to keep all squares blank before a grid is loaded.
     * </p>
     * 
     * @param file
     *            - The file to be opened by the FileHandler.
     */
    public void openFile(File file) {
        fileH = new FileHandler();
        this.removeAll();
        setupCanvas();
        solver = new SudokuSolver();
        solver.setGrid(fileH.readFile(file));
        solver.setup();
        grid = solver.getGrid();
        gridLoaded = true;
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        grid = solver.getGrid();
        drawgrid(g);
    }

    /**
     * <p>
     * Draws/Writes onto the JLabels representing the Cells, contained within a
     * 2D array, based upon the data in the grid of SudokuSolver, to determine
     * if a Cell needs to display a start value, a solved value or a list of
     * current candidates for that Cell.
     * </p>
     * <p>
     * In order to get wrapping text, HTML tags are used that keep the text
     * contained. <br />
     * As the method loops through the 2D array of cells, it checks if the Cell
     * has a value or not. Should the cell have a value, the text is Centred to
     * the JLabel and the text is updated to be just the value of the cell.
     * Should this value be taken on the read in, it is set to display as Blue.
     * If the cell is solved and updated, it is displayed as Red.
     * </p>
     * <p>
     * If there is not a value to the cell, then the text is set to be smaller
     * and aligned to the top left of the JLabel, in order to neatly display the
     * candidates of the cell. <br />
     * In order to display the candidates in a pleasing manner, the method uses
     * a StringBuffer, only taking in the candidates (not the [ and ] of the
     * array], and appending spaces between the candidates.
     * </p>
     * <p>
     * Also gets the latest results from the last successful algorithm
     * operation. If there are any Cells in the affected list, the JLable
     * associated with them has its background set to Yellow, to make it visible
     * to the user which cells were updated. Given more time on this project, I
     * would work on getting a text explanation and description about these
     * updates (and the candidates) in the sidebar.
     * </p>
     * 
     * @param g
     *            - Graphics to be drawn, passed by 'paintComponent'.
     */
    private void drawgrid(Graphics g) {
        if (gridLoaded) {
            for (int i = 0; i < SUDOKU_SPACES; i++) {
                for (int j = 0; j < SUDOKU_SPACES; j++) {
                    colourCell(i, j, cells[i][j]);
                    sb = new StringBuffer();
                    sb.append("<html>");
                    if (grid[i][j].hasValue()) {
                        cells[i][j]
                                .setHorizontalAlignment(SwingConstants.CENTER);
                        cells[i][j].setVerticalAlignment(SwingConstants.CENTER);
                        textValue = Integer.toString(grid[i][j].getValue());
                        cells[i][j].setFont(lFont);
                        if (grid[i][j].firstValue()) {
                            cells[i][j].setForeground(Color.BLUE);
                        } else {
                            cells[i][j].setForeground(Color.RED);
                        }
                    } else {

                        for (int candidate : grid[i][j].returnCandidates()) {
                            sb.append(candidate);
                            sb.append(" ");
                        }
                        textValue = sb.toString();
                        cells[i][j].setVerticalAlignment(SwingConstants.TOP);
                        cells[i][j].setFont(sFont);
                    }
                    sb.append("</html>");
                    cells[i][j].setText(textValue);
                    result = solver.returnResult();
                    if (!solver.isSolved()) {
                        for (Cell rC : result.getAffected()) {
                            cells[rC.getRow()][rC.getColumn()]
                                    .setBackground(Color.YELLOW);
                        }
                    }
                }
            }
        }
    }

    public void colourCell(int i, int j, JLabel cellAtm) {
        if ((j > 2 && j < 6) && (i < 3 || i > 5)
                || ((j < 3 || j > 5) && (i > 2 && i < 6))) {
            cellAtm.setBackground(Color.LIGHT_GRAY);
        } else {
            cellAtm.setBackground(Color.GRAY);
        }
    }

    /**
     * <p>
     * Creates a new instance of StringBuffer to the same location as the one
     * also used for labelling the JLabels. <br />
     * This is used to wrap results from the SudokuSolvers steps taken in HTML.
     * <br />
     * The request to the SudokuSolver to take a step forward is sent and will
     * return a String for using in displaying on the main Frame's sidebar. <br />
     * Repaint is called at the end of the method to ensure all visuals are
     * updated.
     * </p>
     */
    public void takeStep() {
        stepB = new StringBuffer();
        stepB.append("<html>");
        stepB.append(solver.takeStep());
        stepB.append("</html>");
        stepsLabel.setText(stepB.toString());
        this.repaint();
    }

    /**
     * <p>
     * Returns a JLabel of text describing what steps were taken so far, and how
     * many.
     * </p>
     * 
     * @return the Text built up from the 'takeStep()' method for use in the
     *         SudokuFrame.
     */
    public JLabel getSteps() {
        return stepsLabel;
    }

    /**
     * <p>
     * Implementation of the run() method, calling while the puzzle is not
     * solved, continue taking steps and adding them to the Label that takes
     * care of recording what steps have been taken so far.
     * </p>
     * <p>
     * Thread.sleep is used to allow pauses between steps taken in the solving
     * of the puzzle, giving the user time to view the steps taken even with the
     * puzzle is being auto-solved.
     * </p>
     */
    @Override
    public void run() {
        keepSolving = !solver.isSolved();
        while (!solver.isSolved() && keepSolving) {
            stepB = new StringBuffer();
            stepB.append("<html>");
            stepB.append(solver.takeStep());
            stepB.append("</html>");
            stepsLabel.setText(stepB.toString());
            this.repaint();
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                // An interrupted Exception is thrown here if the Solve button
                // is
                // pressed repeatedly without first solving the puzzle.
                // This is due to the thread being interrupted during the
                // 'sleep'
                // Since the thread is correctly stopped and I am aware of this
                // issue, I have not printed it out to the command line or
                // further handled the Exception. I am aware this is not ideal
                // practice, however, given the time constraints, I would deal
                // with this on high priority if given more time to work on the
                // GUI rather than the solving algorithms.
            }
        }
        keepSolving = false;
    }

    /**
     * <p>
     * Helps stopping the program by setting boolean allowing the while loop to
     * continue to false.
     * </p>
     */
    public void pause() {
        keepSolving = false;
    }

    /**
     * @return If puzzle is solved or not
     */
    public boolean isSolved() {
        return solver.isSolved();
    }
}
