import bagel.*;
import bagel.util.*;

/**
 * Class representing a Bucket which spawns at coordinate (512,744).
 * Buckets move horizontally, 4 pixels at a time, and bounce off horizontal boundaries of a Game Window.
 */
public class Bucket extends Sprite {
    private static final double INITIAL_X = 512;
    private static final double INITIAL_Y = 744;
    private static final double MOVEMENT = 4;
    private static final String IMAGE_FILE = "res/bucket.png";

    private Vector2 velocity;

    /**
     * Creates a Bucket.
     */
    public Bucket() {
        super(INITIAL_X, INITIAL_Y, IMAGE_FILE);
        velocity = Vector2.left.mul(MOVEMENT);
    }

    /**
     * Updates the Point of the Bucket by adding its velocity to its Point.
     * Reverses horizontal velocity if horizontal boundaries of game window are collided with.
     */
    @Override
    public void move() {
        //Determine the expected new point
        Vector2 pointVector = getPoint().asVector();
        setPoint(pointVector.add(velocity).asPoint());

        //Account for new point being beyond horizontal boundaries, and modify accordingly
        if (getRectangle().right() >= Window.getWidth()) {
            setPoint(Window.getWidth() - HALF*imageWidth(), getPoint().y);
            reverseHorizontalVelocity();
        } else if (getRectangle().left() <= MIN_DOMAIN) {
            setPoint(HALF*imageWidth(), getPoint().y);
            reverseHorizontalVelocity();
        }
    }

    private void reverseHorizontalVelocity() {
        velocity = new Vector2 (REVERSE*velocity.x, velocity.y);
    }
}
