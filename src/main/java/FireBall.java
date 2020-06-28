import bagel.*;
import bagel.util.*;
import java.util.ArrayList;

/**
 * A class representing a 'fire' Ball.
 * Contains method to destroy every Peg within 70 pixels of a specified Peg.
 */
public class FireBall extends Ball {
    private static final String IMAGE_FILE = "res/fireball.png";
    private static final double NEARBY_THRESHOLD = 70;

    /**
     * Creates a FireBall.
     * @param point coordinate of fireball.
     */
    public FireBall(Point point) {
        super(point);
        setImage(new Image(IMAGE_FILE));
    }

    /**
     * Creates a new FireBall as a Ball with a given point.
     * i.e. Creates a ball with the same underlying class/type as the ball calling this method.
     * @param point given point.
     * @return new FireBall upcasted to a Ball.
     */
    @Override
    public Ball duplicateType(Point point) {
        return new FireBall(point);
    }

    /**
     * Destroys Pegs from a board that are within a 70 pixel radius of a specified Peg.
     * @param centre Peg representing centre of destruction.
     * @param board board of Pegs to destroy from
     * @return new board containing the remaining Pegs
     */
    public ArrayList<Peg> destroyNearbyPegs(Peg centre, ArrayList<Peg> board) {
        for (Peg peg : board) {
            //Destroy pegs where distance between centre and peg is smaller than a certain threshold.
            //Distance = length of (centre vector - peg vector).
            if (centre.getPoint().asVector().sub(peg.getPoint().asVector()).length() <= NEARBY_THRESHOLD) {
                peg.destroy();
            }
        }
        return board;
    }
}
