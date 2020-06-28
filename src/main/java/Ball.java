import bagel.*;
import bagel.util.*;

/**
 * A class representing a ball that bounces off Pegs and Horizontal Boundaries of a Game Window.
 */

public class Ball extends Sprite {
    private static final String IMAGE_FILE = "res/ball.png";

    private Vector2 velocity = new Vector2();

    /**
     * Create a ball.
     * @param x x-coordinate.
     * @param y y-coordinate.
     */
    public Ball(double x, double y) {
        super(x, y, IMAGE_FILE);
    }

    /**
     * Create a ball.
     * @param point point of ball.
     */
    public Ball(Point point) {
        super(point.x, point.y, IMAGE_FILE);
    }

    /**
     * Updates the velocity of the ball.
     * @param velocity new velocity of the ball.
     */
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    /**
     * Returns the velocity of the ball.
     * @return velocity of the ball.
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     * Add a given x-value and y-value to the ball's velocity.
     * @param x x-value
     * @param y y-value
     */
    public void addVelocity(double x, double y) {
        this.velocity = this.velocity.add(new Vector2(x, y));
    }

    private void reverseHorizontalVelocity() {
        velocity = new Vector2 (REVERSE*velocity.x, velocity.y);
    }
    private void reverseVerticalVelocity() {
        velocity = new Vector2(velocity.x, REVERSE*velocity.y);
    }

    /**
     * Updates the point of the ball by adding its velocity to its point.
     * Reverses horizontal velocity if horizontal boundaries of game window are collided with.
     */
    @Override
    public void move() {
        //Ball doesn't move if it has "no" velocity
        if (velocity == null) {
            return;
        }

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

    /**
     * Checks whether the ball is permanently off screen; this occurs when its Point has gone the game window's height.
     * @return boolean of whether ball is off screen.
     */
    public boolean isOffScreen() {
        return getPoint().y >= Window.getHeight();
    }

    /**
     * Returns a new Ball object with a given point.
     * @param point given point.
     * @return new Ball
     */
    // Create a ball of the same class as the ball calling this function.
    //'Normal' Ball will use this method. Subclasses should override this method.
    public Ball duplicateType(Point point) {
        return new Ball(point);
    }

    /**
     * Check if ball collides with a given peg, and also bounce off the peg if it does.
     * @param peg Peg assumed to be collided with.
     * @return boolean of whether collision occurred
     */
    //Overload of collidesWith for when ball collides with peg
    public boolean collidesWith(Peg peg) {
        Side side = (peg.getRectangle().intersectedAt(getPoint(),velocity));
        if (side.equals(Side.NONE)) {
            return false;
        }
        if (side.equals(Side.LEFT) || side.equals(Side.RIGHT)) {
            reverseHorizontalVelocity();
        } else if (side.equals(Side.TOP) || side.equals(Side.BOTTOM)) {
            reverseVerticalVelocity();
        }
        return true;
     }
}