package assignment1;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * <h2>Main Class</h2>
 * <p>
 * Main class where the program begins.
 * The class sets up what the look and feel for the JFrame
 * should be for the program and opens it in a separate thread.
 * </p>
 * <p>
 * My implementation of the SwingUtilities and use of
 * the LookAndFeel may not be exactly correct. Due to the
 * time scale of this project, I decided to re-use old
 * code from my past project 'Blockmation', from my first
 * year in University. This is an artifact of that code that
 * I remember working.
 * <br />
 * Should there have been more time to work on this assignment,
 * I would have fully investigated these and also worked on making
 * the GUI more user friendly, informative as to what Cell is 
 * which and what Cells had just been updated and how exactly.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
public class Main {

    @SuppressWarnings("unused")
    private static Driver driver;
    
    public static void main(String[] args) {
        

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Class not found!",
                    "Error: Look and Feel - Class Not Found",
                    JOptionPane.ERROR_MESSAGE);
        } catch (InstantiationException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Not able to Instantiate Look and Feel!",
                    "Error: Look and Feel - Unable to Instantiate",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalAccessException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Illegal Access Exception!",
                    "Error: Look and Feel - Illegal Access Exception",
                    JOptionPane.ERROR_MESSAGE);
        } catch (UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Look and Feel "
                    + UIManager.getCrossPlatformLookAndFeelClassName()
                    + " not supported!",
                    "Error: Look and Feel - Unsupported Look and Feel",
                    JOptionPane.ERROR_MESSAGE);
        }
        makeApp();
    }

    /**
     * <p>
     * Creates the application with invokeLater().
     * </p>
     */
    public static void makeApp() {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    build();
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Exception encountered on attempt to build application",
                    "Error - Not able to build application",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * <p>
     * Makes a new instance of <code>SolverFrame</code> that opens the Sudoku Solver
     * window.
     * </p>
     */
    public static void build() {
        driver = new Driver();
    }


        
        

    

}
