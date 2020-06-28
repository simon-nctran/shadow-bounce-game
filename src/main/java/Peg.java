/**
 * An abstract class that represents a Peg of a certain Shape.
 * Pegs do not move.
 * Records whether or not it has been destroyed.
 */

public abstract class Peg extends Sprite {
    private boolean destroyed;
    private String shape;

    /**
     * Creates a Peg.
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @param imageFile filename of image.
     */
    public Peg(double x, double y, String imageFile) {
        super(x, y, imageFile);

    }

    /**
     * Sets the peg to a destroyed state.
     */
    public void destroy() {
        destroyed = true;
    }

    /**
     * Determines whether or not the peg is destroyed.
     * @return boolean of whether peg is destroyed.
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Returns the shape of the peg.
     * @return shape as a string
     */
    public String getShape() {
        return shape;
    }

    /**
     * Updates the shape of the peg.
     * @param shape new shape of the peg.
     */
    public void setShape(String shape) {
        this.shape = shape;
    }

    /**
     * Attempts to move peg but does nothing, as pegs cannot move.
     */
    @Override
    public void move() {

    }
}
