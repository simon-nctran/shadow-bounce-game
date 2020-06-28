/**
 * A class representing a Grey Peg.
 * Grey Pegs cannot be destroyed.
 */

public class GreyPeg extends Peg {
    private static final String NORMAL_FILE = "res/grey-peg.png";
    private static final String HORIZONTAL_FILE = "res/grey-horizontal-peg.png";
    private static final String VERTICAL_FILE = "res/grey-vertical-peg.png";
    private static final int NORMAL_FILE_LENGTH = 2;
    private static final int SHAPE_INDEX = 2;

    //Determine the shape, and thus the file to obtain image from.
    //Input String is of form "<colour>_peg_<shape>"
    private static String determineShapeFile(String input) {
        if ((input.split("_")).length == NORMAL_FILE_LENGTH) {
            return NORMAL_FILE;
        } else if ((input.split("_")[SHAPE_INDEX].equals("horizontal"))) {
            return HORIZONTAL_FILE;
        } else {
            return VERTICAL_FILE;
        }
    }

    /**
     * Create a Grey Peg.
     * @param configuration used to determine the shape.
     * @param x x-coordinate.
     * @param y y-coordinate.
     */
    public GreyPeg(String configuration, double x, double y) {
        super(x, y, determineShapeFile(configuration));
        setShape(determineShape(configuration));
    }

    //Determine the shape as a string to record for future use
    private String determineShape(String input) {
        if ((input.split("_")).length == NORMAL_FILE_LENGTH) {
            return "Normal";
        } else if ((input.split("_")[SHAPE_INDEX].equals("horizontal"))) {
            return "Horizontal";
        } else {
            return "Vertical";
        }
    }

    /**
     *  Attempts to destroy Grey Peg but fails, as Grey Peg cannot be destroyed.
     */
    @Override
    public void destroy() {

    }
}
