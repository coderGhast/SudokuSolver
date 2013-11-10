package assignment1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

/**
 * <h2>SolverFrame</h2>
 * <p>
 * The main visual displayer for this application.<br />
 * One Frame to rule them all, One Frame to find them,<br />
 * One Frame to bring them all and in the darkness bind them. <br />
 * This class holds reference to the Model and is used by the Controller
 * (Driver). Most actions by the user are caught and passed to the Model from
 * here.
 * </p>
 * <p>
 * From the frame, the user can open files, close the application, take 'steps'
 * through a Sudoku's solving process and see a quick solve. The steps taken
 * towards the solution are also displayed, although where they were executed is
 * not. <br />
 * The GUI is very simple. Were there more time alloted for this project, I
 * would expand into firstly using JTextArea and carats to get the Solution
 * steps to autoscroll to the bottom of the steps. <br />
 * Next I would get a log of not just what steps were taken but -where- each
 * step was taken in the grid puzzle. Right now the user must see by eye what
 * has taken place. Once this is implemented, I would work towards allowing the
 * user to step take a step backwards through the puzzle. <br />
 * Were much more time alloted, I would expand into allowing the user to click
 * buttons in attempts to solve the puzzle by themselves, either through given
 * methods or directly allowing them attempts at solving the puzzle with the
 * methods just used to confirm correct choices.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
@SuppressWarnings("serial")
public class SolverFrame extends JFrame implements ActionListener {

    private SolverCanvas canvas;
    private boolean gridLoaded = false;
    private boolean threadRunning = false;
    private Thread runSolver;
    private String appTitle = "Sudoku Solver - jee22";

    // === Menu Items ===
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem openItem;
    private JMenuItem exitItem;
    private JFileChooser fileChooser;

    // === Sidebar Items ===
    private JPanel sidebar;
    private JButton button;
    private JButton solveButton;
    private JPanel stepsPanel;
    private JScrollPane scroll;
    private JLabel status;
    private JLabel stText;

    /**
     * <p>
     * The constructor sets it's own components and prepares the canvas where
     * the Sudoku grid is displayed to be ready for use too.
     * </p>
     * 
     * @param solver
     *            - reference to an opening SudokuSolver to get the application
     *            running.
     */
    public SolverFrame(SudokuSolver solver) {
        canvas = new SolverCanvas(solver);
        setupFrameProperties();
        setupMenu();
        menuBar.add(fileMenu);

        setupSidebar();
        this.add(sidebar, BorderLayout.EAST);
        this.getContentPane().add(canvas, BorderLayout.CENTER);
        this.setJMenuBar(menuBar);
    }

    /**
     * <p>
     * Assigns the properties of this Frame and how it should be displayed to a
     * user.
     * </p>
     * <p>
     * In particular, the grid is set to a specific size and is unsizable in
     * order to keep the Sudoku grid displaying correctly with uniformly sized
     * cells. I felt this was the best choice for such a simple GUI and
     * application at this point in time. <br />
     * The Frame is created in the centre of the users screen and set to 'grey',
     * the default of most OS general application colours.
     * </p>
     */
    public void setupFrameProperties() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(700, 550));
        this.pack();
        this.setResizable(false);
        this.setTitle(appTitle);
        this.setBackground(Color.gray);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * <p>
     * Establishes the attributes of the top bar menu on the frame, allowing the
     * user to open files and exit the application.
     * </p>
     * <p>
     * Each button the menu bar is given a quick key shortcut for ease of use
     * and also a command for use with the ActionListener that listens for uses
     * by the user. <br />
     * A small description of each action is given for accessibility sake of
     * sight impaired users.
     * </p>
     */
    public void setupMenu() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        exitItem = new JMenuItem("Exit");

        openItem = new JMenuItem("Open", KeyEvent.VK_O);
        openItem.setActionCommand("open");
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask()));
        openItem.getAccessibleContext().setAccessibleDescription(
                "Opens a Sudoku .sud file");
        fileMenu.add(openItem);

        exitItem = new JMenuItem("Exit");
        exitItem.setActionCommand("exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask()));
        exitItem.getAccessibleContext().setAccessibleDescription(
                "Exit Sudoku Solver");
        fileMenu.add(exitItem);

        fileMenu.add(openItem);
        fileMenu.add(exitItem);

        openItem.addActionListener(this);
        exitItem.addActionListener(this);
    }

    /**
     * <p>
     * Sets the sidebar up for use on the Frame.<br />
     * Strict dimensions are set to ensure it doesn't tamper with the Sudoku
     * grid display.
     * </p>
     * <p>
     * To give the user some help with the application, there are labels that
     * display the currently open file, buttons to either take steps in solving
     * the puzzle or a button to auto solve and pause the puzzle. <br />
     * As previously stated,were there more time I would work on making a better
     * JScrollBar implementation.
     * </p>
     */
    public void setupSidebar() {
        sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 500));

        stText = new JLabel("File Open: ");
        stText.setPreferredSize(new Dimension(55, 10));
        status = new JLabel("No file Open");
        status.setPreferredSize(new Dimension(135, 10));

        sidebar.add(stText);
        sidebar.add(status);

        button = new JButton("Take Step");
        button.addActionListener(this);
        button.setActionCommand("step");
        sidebar.add(button);

        solveButton = new JButton("Solve Puzzle");
        solveButton.addActionListener(this);
        solveButton.setActionCommand("solve");
        sidebar.add(solveButton);

        stepsPanel = new JPanel();
        stepsPanel.setLayout(new BorderLayout());

        stepsPanel.setBackground(Color.WHITE);
        stepsPanel.setBorder(new LineBorder(Color.BLACK));

        scroll = new JScrollPane(stepsPanel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(180, 400));

        sidebar.add(scroll);

    }

    /**
     * <p>
     * Commands sent by the user from menus and buttons.
     * </p>
     * <p>
     * The open command will stop any running Thread to ensure that if a puzzle
     * is being solved, the user can't open a new puzzle and have the solving
     * continue on the new puzzle too.
     * </p>
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("open")) {
            if (threadRunning) {
                threadHandler();
            }
            if (openFile()) {
                stepsPanel.removeAll();
                repaint();
                gridLoaded = true;

            }

        }
        if (command.equals("exit")) {
            exit();
        }
        if (command.equals("step")) {
            if (gridLoaded) {
                if (threadRunning) {
                    threadHandler();
                }
                canvas.takeStep();
                stepsPanel.add(canvas.getSteps());
                this.repaint();
            } else {
                // Pop up box to say no sudoku loaded - Future implementation
            }

        }
        /**
         * <p>
         * If the user wishes the solve the puzzle, it is first checked if there
         * is a loaded grid, then if the puzzle is being solved, and if so stops
         * it, then starts a new Thread and begins running a new solve loop in
         * SudokuCanvas. <br />
         * The Solve button is also changed to 'Stop Solving', useful for
         * pausing a solve mid way through.
         * </p>
         */
        if (command.equals("solve")) {
            if (gridLoaded) {
                if (threadRunning) {
                    this.threadHandler();
                } else {
                    if (!canvas.isSolved()) {
                        runSolver = new Thread(canvas);
                        threadRunning = true;
                        runSolver.start();
                        stepsPanel.add(canvas.getSteps());
                        this.repaint();
                        solveButton.setText("Stop Solving");
                    }
                }
            } else {
                // Pop up box to say no sudoku loaded
            }
        }
    }

    /**
     * <p>
     * If there is a thread running, often methods that would be otherwise
     * affected by it's continuation will call this function. <br />
     * This method calls to the SudokuCanvas to set a boolean value to false in
     * order to stop a while loop in the canvas, then interrupts the Thread from
     * here. <br />
     * Once done, the boolean keeping track of if a Thread is running is set to
     * false and the button stopping/starting the Thread/solver displays a
     * relevant message.
     * </p>
     */
    public void threadHandler() {
        canvas.pause();
        runSolver.interrupt();
        threadRunning = false;
        solveButton.setText("Solve Puzzle");
    }

    /**
     * <p>
     * Opens a file on the users system to solve
     * </p>
     * <p>
     * Uses <code>JFileChooser</code> to allow the user to select any file in
     * their system.
     * </p>
     * <p>
     * In order to filter out invalid files, I have created a customer
     * <code>FileFilter</code> that only displays <code>.sud</code> files.
     * </p>
     * <p>
     * Once a users has selected a file, it passes it through onto the canvas to
     * open and be displayed.
     * </p>
     */
    private boolean openFile() {
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new SudFileFilter());
        int chosen = fileChooser.showOpenDialog(this);
        if (chosen == JFileChooser.APPROVE_OPTION) {
            status.setText(fileChooser.getName(fileChooser.getSelectedFile()));
            canvas.openFile(fileChooser.getSelectedFile());
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>
     * Exits the program.
     * </p>
     */
    private void exit() {
        System.exit(0);

    }
}
