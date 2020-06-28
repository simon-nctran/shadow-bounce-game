import bagel.*;
import bagel.util.*;

/**
 * A class representing a Powerup which spawns at a random location within a Game Window.
 * Powerups move 3 pixels at a time towards a randomly chosen location, and upon reaching said location,
 * moves towards a new randomly chosen location.
 * Contains methods to transform a Ball into a FireBall.
 */

public class Powerup extends Sprite {
    private static final double SPEED = 3;
    private static final double DISTANCE_THRESHOLD = 5;
    private static final String IMAGE_FILE = "res/powerup.png";

    private Point targetPoint;

    /**
     * Creates a Powerup with a random Point.
     */
    public Powerup() {
        super(Math.random()*Window.getWidth(), Math.random()*Window.getHeight(), IMAGE_FILE);
        targetPoint = new Point(Math.random()*Window.getWidth(),Math.random()*Window.getHeight());
    }

    /**
     * Moves the Powerup 3 pixels closer to its target point, and sets a new target if necessary.
     */
    public void move() {
        //Determine new point
        Vector2 pointVector = getPoint().asVector();
        Vector2 directionVector = targetPoint.asVector().sub(pointVector).normalised();
        setPoint(pointVector.add(directionVector.mul(SPEED)).asPoint());

        //Sets a new target point if distance threshold has been reached
        attemptNewTarget();
    }

    private void attemptNewTarget() {
        if (getPoint().asVector().sub(targetPoint.asVector()).length() <= DISTANCE_THRESHOLD) {
            targetPoint = new Point(Math.random()*Window.getWidth(),Math.random()*Window.getHeight());
        }
    }

    /**
     * Transforms a Ball into a FireBall.
     * @param ball Ball to be transformed.
     * @return new FireBall with the same attribute values as the given Ball.
     */
    public Ball activate(Ball ball) {
        Ball fireball = new FireBall(ball.getPoint());
        fireball.setVelocity(ball.getVelocity());

        return fireball;
    }
}
