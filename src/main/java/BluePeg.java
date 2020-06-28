/**
 * A class that represents a Blue Peg.
 */

public class BluePeg extends Peg {
    private static final String NORMAL_FILE = "res/peg.png";
    private static final String HORIZONTAL_FILE = "res/horizontal-peg.png";
    private static final String VERTICAL_FILE = "res/vertical-peg.png";
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
    private static String determineShapeFile(Peg peg) {
        String shape = peg.getShape();
        if (shape.equals("Normal")) {
            return NORMAL_FILE;
        } else if (shape.equals("Horizontal")) {
            return HORIZONTAL_FILE;
        } else {
            return VERTICAL_FILE;
        }
    }

    /**
     * Create a Blue Peg.
     * @param configuration used to determine the shape.
     * @param x x-coordinate.
     * @param y y-coordinate.
     */
    public BluePeg(String configuration, double x, double y) {
        super(x, y, determineShapeFile(configuration));
        setShape(determineShape(configuration));
    }
    /**
     * Create a Blue Peg with the shape and coordinates of an existing peg.
     * @param peg an existing peg to copy shape and coordinates from.
     */
    public BluePeg(Peg peg) {
        super(peg.getPoint().x, peg.getPoint().y, determineShapeFile(peg));
        setShape(peg.getShape());
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
}
