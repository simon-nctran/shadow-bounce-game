import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * An implementation of Shadow Bounce for SWEN20003 Object Oriented Software Development
 * Project 2B, Semester 2, 2019
 *
 * Details of functionality can be found in the Project 2 specification.
 */

public class ShadowBounce extends AbstractGame {
    private static final double INITIAL_BALL_VELOCITY = 10;
    private static final double GRAVITY = 0.15;
    private static final double BALL_INITIAL_X = 512;
    private static final double BALL_INITIAL_Y = 32;
    private static final int NUM_BOARDS = 5;
    private static final int INITIAL_BOARD_INDEX = 0;
    private static final int MAX_NUM_BALLS = 3;
    private static final int MAIN_BALL_INDEX = 0;
    private static final int INITIAL_NUM_SHOTS = 20;
    //Maximum spawn chance bound is 1.0, therefore 0.1 represents 1/10 chance.
    private static final double POWERUP_SPAWN_CHANCE_BOUND = 0.1;
    //One fifth of blue pegs are converted to red pegs.
    private static final double BLUE_TO_RED_PEG_CONVERSION_RATIO = 1.0/5;

    private static final String[] BOARD_FILES = {"res/0.csv", "res/1.csv", "res/2.csv", "res/3.csv", "res/4.csv"};

    private int nextBoard;
    private int numShots;

    private Ball[] balls;
    private Powerup powerup;
    private Bucket bucket;
    private ArrayList<Peg> board;

    /**
     * Creates the Shadow Bounce game with a default window size (1024x768).
     */
    public ShadowBounce() {
        numShots = INITIAL_NUM_SHOTS;
        nextBoard = INITIAL_BOARD_INDEX;

        board = generateBoard(nextBoard);
        nextBoard++;

        bucket = new Bucket();

        newTurn();
    }

    /**
     * The entry point for the program; instantiates and runs the ShadowBounce game.
     * @param args unused
     */
    public static void main(String[] args) {
        ShadowBounce game = new ShadowBounce();
        game.run();
    }

    /**
     * Update the state of the game in 60 frames per second.
     * @param input provides access to input devices (keyboard and mouse).
     */
    @Override
    public void update(Input input) {
        //Instantiate upon left click, render and move balls.
        updateBalls(input);

        //Render, move, and check collision of powerup, bucket and board (pegs).
        updatePowerup();
        updateBucket();
        updateBoard();

        //If all active ball objects are offscreen, destroy them, and then check if game is finished,
        //or board is cleared, and if neither, then start a new turn.
        if (balls != null && ballsOffScreen())  {
            balls = null;
            numShots--;

            //If number of shots is depleted, then end game
            if (numShots == 0) {
                Window.close();
            } else if (boardCleared()) {
                //If nextBoard index exceeds the maximum BOARDS index, then end game
                //i.e. Last board has already been generated
                if (nextBoard > NUM_BOARDS-1) {
                    Window.close();
                } else {
                    board = generateBoard(nextBoard);
                    nextBoard++;
                    newTurn();
                }
            } else {
                newTurn();
            }
        }
    }

    private ArrayList<Peg> generateBoard(int boardNum) {
        ArrayList<Peg> board = new ArrayList<>();

        //Read board CSV file, generate a peg from each line, and add to the ArrayList, board.
        try (BufferedReader csv = new BufferedReader(new FileReader(BOARD_FILES[boardNum]))) {
            String line;
            while (((line = csv.readLine()) != null)) {
                board.add(generatePeg(line.split(",")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Compute number of blue pegs
        int blueSize = 0;
        for (Peg peg: board) {
            if (peg instanceof BluePeg) {
                blueSize++;
            }
        }

        //Randomly convert 1/5 blue pegs into red pegs
        int randomIndex;
        for (int i=0; i<blueSize*BLUE_TO_RED_PEG_CONVERSION_RATIO; i++) {
            randomIndex = (int) (Math.random()*(board.size()-1));
            //Ensure randomly chosen peg is a blue peg, otherwise try another random peg
            while (!(board.get(randomIndex) instanceof BluePeg)) {
                randomIndex = (int) (Math.random()*(board.size()-1));
            }
            //Replace existing blue peg with new red peg
            board.set(randomIndex, new RedPeg(board.get(randomIndex)));
        }
        return board;
    }

    //Generate peg from csv line input in array of form: [type,x,y]
    private Peg generatePeg(String[] configuration) {
        String type = configuration[0];

        //The first word of every 'type' is the colour of the peg
        //Words in 'type' are split by an underscore (_)
        String colour = (type.split("_"))[0];
        double x = Double.parseDouble(configuration[1]);
        double y = Double.parseDouble(configuration[2]);

        //Only blue and grey pegs are produced by csv input
        if (colour.equals("blue")) {
            //XXXPeg Constructor takes 'type' from csv input as an argument
            return new BluePeg(type, x, y);
        } else {
            return new GreyPeg(type, x, y);
        }

    }

    //Remove GreenPeg and Powerup from previous turn, and attempt to generate them again for new round
    private void newTurn() {
        //Return old green peg back to blue peg
        for (int i=0;i<board.size();i++) {
            if (board.get(i) instanceof GreenPeg && !(board.get(i).isDestroyed())) {
                board.set(i, new BluePeg(board.get(i)));
            }
        }
        //Convert a random blue peg into a green peg
        //Obtain a random peg index and ensure it is a blue peg, otherwise try another random peg
        int randomIndex = (int) (Math.random()*(board.size()-1));
        while (!(board.get(randomIndex) instanceof BluePeg)) {
            randomIndex = (int) (Math.random()*(board.size()-1));
        }
        //Replace the chosen blue peg, with green peg
        board.set(randomIndex, new GreenPeg(board.get(randomIndex)));

        //Generate a powerup at a 1/10 chance
        if (Math.random() <= POWERUP_SPAWN_CHANCE_BOUND) {
            powerup = new Powerup();
        } else {
            powerup = null;
        }
    }

    //Render, increase velocity, and update the points of the balls.
    private void updateBalls(Input input) {
        //Instantiate the balls array and the main ball with a left click, given they don't already exist.
        //Main ball won't appear until a left click occurs.
        //balls only equals null when there are no balls on screen.
        if (balls == null && input.wasPressed(MouseButtons.LEFT)) {
            //Instantiate and render the main ball
            balls = new Ball[MAX_NUM_BALLS];
            balls[MAIN_BALL_INDEX] = new Ball(BALL_INITIAL_X, BALL_INITIAL_Y);

            //Set initial velocity of main ball
            balls[MAIN_BALL_INDEX].setVelocity(input.directionToMouse(balls[MAIN_BALL_INDEX].getPoint()).mul(INITIAL_BALL_VELOCITY));

        } else if (balls != null) {
            for (int i=0;i<MAX_NUM_BALLS;i++) {
                if (balls[i] != null) {
                    balls[i].render();

                    //Increase velocity
                    balls[i].addVelocity(0, GRAVITY);
                    //Account for the ball's velocity in advance. Changes render in the next frame.
                    balls[i].move();
                }
            }
        }
    }

    //If powerup is active, then render it, determine its next position, and check if it collides with any balls
    private void updatePowerup() {
        boolean collided = false;
        if (powerup != null) {
            powerup.render();
            powerup.move();

            //Check if powerup collides with any balls
            if (balls != null) {
                for (int i=0;i<MAX_NUM_BALLS;i++) {
                    if (balls[i] != null && powerup.collidesWith(balls[i])) {
                        //Activate powerup if there's a collision with a ball
                        balls[i] = powerup.activate(balls[i]);
                        collided = true;
                    }
                }
            }
        }
        //Destroy powerup after checking possible collisions, rather than during, prevents NullPointerExceptions
        //when multiple balls collide with the powerup
        if (collided) {
            powerup = null;
        }
    }

    //Render bucket, determine new point, and check if any balls collide with it whilst going offscreen
    private void updateBucket() {
        bucket.render();
        bucket.move();
        if (balls != null) {
            for (Ball ball: balls) {
                if (ball != null && ball.isOffScreen() && ball.collidesWith(bucket)) {
                    numShots++;
                }
            }
        }
    }

    //Destroy collided pegs and render the remaining.
    private void updateBoard() {
        //Destroy all pegs that have collided with any ball, and account for GreenPegs and FireBalls
        if (balls != null) {
            for (Peg peg: board) {
                if (!(peg.isDestroyed())) {
                    for (Ball ball: balls) {
                        if (ball != null && ball.collidesWith(peg)) {
                            peg.destroy();
                            if (peg instanceof GreenPeg) {
                                balls = ((GreenPeg) peg).generateBalls(ball);
                            }
                            if (ball instanceof FireBall) {
                                //Note: Fireball effect may destroy GreenPegs but will not activate
                                //      GreenPeg effect, because no ball directly "strike[d]" the GreenPeg
                                board = ((FireBall) ball).destroyNearbyPegs(peg, board);
                            }
                        }
                    }
                }
            }
        }
        //Render all remaining pegs
        for (Peg peg: board) {
            if (!(peg.isDestroyed())) {
                peg.render();
            }
        }
    }

    //Check that all the balls are off screen
    private boolean ballsOffScreen() {
        for (Ball ball: balls) {
            if (ball != null && !ball.isOffScreen()) {
                return false;
            }
        }
        return true;
    }

    //Return true if all red pegs have been destroyed
    private boolean boardCleared() {
        for (Peg peg: board) {
            if  ((peg instanceof RedPeg) && !(peg.isDestroyed())) {
                return false;
            }
        }
        return true;
    }
}
