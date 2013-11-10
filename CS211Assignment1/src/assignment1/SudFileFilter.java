package assignment1;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * <h2>SudFileFilter</h2>
 * <p>
 * A custom <code>FileFilter</code> class to only show a user <code>.sud</code>
 * files when they open <code>JFileChooser</code> to load a file on their
 * system.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
public class SudFileFilter extends FileFilter {

    /**
     * <p>
     * <code>return true</code> is checking each path and file, and if they end
     * with <code>.sud</code> or are a Directory file, they will be displayed.
     * </p>
     */
    @Override
    public boolean accept(final File path) {
        if (path.isDirectory()) { // Returns true and displays Folders
            return true;
        }
        String name = path.getName().toLowerCase();
        if (name.endsWith("sud")) { // Returns true and displays any file ending
                                    // .sud
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
