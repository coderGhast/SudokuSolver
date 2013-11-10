package assignment1;

import java.util.LinkedList;

/**
 * <h2>Result</h2>
 * <p>
 * Rather than simply returning a boolean value of whether a solving algorithm
 * was correct or not, I wished to return information about which Cell(s) was
 * altered too, in order to be demonstrated on the grid. <br />
 * This class holds values of the success, a message about the task completed
 * and any Cells affected.
 * </p>
 * 
 * @author James Euesden - jee22@aber.ac.uk
 * @version 1.0
 */
public class Result {

    private boolean success;
    private String message;
    private LinkedList<Cell> affected;

    /**
     * <p>
     * Setup of Result to stop NullPointers
     * </p>
     */
    public Result() {
        success = false;
        message = " ";
        affected = new LinkedList<Cell>();
    }

    /**
     * @param re
     *            - Set result of operation.
     */
    public void setSuccess(boolean re) {
        success = re;
    }

    /**
     * @return - Return result of operation.
     */
    public boolean getSuccess() {
        return success;
    }

    /**
     * @param msg
     *            - The message associated with the outcome of the operation to
     *            be set.
     */
    public void setMessage(String msg) {
        message = msg;
    }

    /**
     * @return - The message associated with the operation carried out, to be
     *         returned for display to the user.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param cell
     *            - Cell to be added to the list of Cells affected by the
     *            solving algorithm and step this Result is associated with.
     */
    public void addAffected(Cell cell) {
        affected.add(cell);
    }

    /**
     * @return The list of Cells affected by the solving algorithm that created
     *         this Result.
     */
    public LinkedList<Cell> getAffected() {
        return affected;
    }

}
