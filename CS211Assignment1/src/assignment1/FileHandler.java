package assignment1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * <h2>FileHandler</h2>
 * <p>
 * A custom <code>FileFilter</code> class to only show a user <code>.sud</code>
 * files when they open <code>JFileChooser</code> to load a file on their
 * system. <br />
 * Will still allow the use of 'All files' option, however. Needs reworking.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
public class FileHandler {

    private BufferedReader buffReader;
    private static final int SUDOKU_SPACES = 9;
    private char[][] lines;

    public FileHandler() {
        lines = new char[SUDOKU_SPACES][SUDOKU_SPACES];
    }

    public FileHandler(String fileName) {
        lines = new char[SUDOKU_SPACES][SUDOKU_SPACES];
        lines = readFile(newFile(fileName));
    }

    /**
     * <p>
     * Allows the creating of a new File object based on a file's name, assuming
     * it's location is local to the application source files.
     * </p>
     * 
     * @param fileName
     *            Takes the file name of the File to be made
     * @return returns the reference to the File.
     */
    public File newFile(String fileName) {
        return new File(fileName);
    }

    /**
     * <p>
     * Takes in a File object and opens the file, reading in the data and
     * putting it into first Strings, then getting an array of Characters to be
     * used as the grid for the Sudoku puzzle grid. <br />
     * Contains a number of Exception catches. Should I have had more time on
     * these project, handling of incorrect data types, files and locations
     * would be made more rigorous.
     * </p>
     * 
     * @param file
     * @return
     */
    public char[][] readFile(File file) {
        try {
            buffReader = new BufferedReader(new FileReader(file));

            for (int i = 0; i < SUDOKU_SPACES; i++) {
                String singleLine = buffReader.readLine();
                lines[i] = singleLine.toCharArray();
            }

            buffReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            /*
             * Catches a NullPointerException should the file be an incorrect
             * format - .sud
             */
            e.printStackTrace();
        }
        return lines;
    }

}