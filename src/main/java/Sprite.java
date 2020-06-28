import bagel.*;
import bagel.util.*;

/**
 * An abstract class representing Sprites.
 * Sprites have an Image, Point and Rectangle.
 */
public abstract class Sprite {
    /**
     * Minimum x and/or coordinate value in a Game Window.
     */
    public static final double MIN_DOMAIN = 0;
    /**
     * Multiply by this to reverse direction/sign.
     */
    public static final double REVERSE = -1;
    /**
     * Multiply be this to half the value.
     */
    public static final double HALF = 0.5;

    private Image image;
    private Point point;
    private Rectangle rectangle;


    /**
     * Create a sprite.
     * @param x
     * @param y
     * @param IMAGE_FILE
     */
    public Sprite(double x, double y, String IMAGE_FILE) {
        point = new Point(x, y);
        image = new Image(IMAGE_FILE);
        rectangle = image.getBoundingBoxAt(point);
    }

    /**
     * Return a point.
     * @return
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Return rectangle.
     * @return
     */
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * Set point.
     * @param point
     */
    //Set new point and also update rectangle
    public void setPoint(Point point) {
        this.point = point;
        rectangle = image.getBoundingBoxAt(point);
    }

    /**
     * Set Point
     * @param x
     * @param y
     */
    //Overload of setPoint method
    public void setPoint(double x, double y) {
        point = new Point(x,y);
        rectangle = image.getBoundingBoxAt(point);
    }

    /**
     * Set image.
     * @param image
     */
    public void setImage(Image image) {
        this.image = image;
        rectangle = image.getBoundingBoxAt(point);
    }

    /**
     * display image.
     */
    //Display object's image at its point
    public void render() {
        image.draw(point.x, point.y);
    }

    /**
     * check collisom
     * @param other
     * @return
     */
    //Checks if this drawable has intersected with another specified drawable
    public boolean collidesWith(Sprite other) {
        return this.rectangle.intersects(other.rectangle);
    }

    /**
     * Return the width of the Sprite's Image
     * @return
     */
    //Returns the width of the objects image.
    public double imageWidth() {
        return image.getWidth();
    }

    /**
     * Updates the sprite's Point.
     */
    public abstract void move();
}
