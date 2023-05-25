package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Paddle extends GameObject {
    /**
     * this class responsible for creating the paddle
     */
    private static final float MOVEMENT_SPEED = 300;
    private UserInputListener inputListener;
    private float windowWidth;
    private int minDistanceFromEdge;

    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener inputListener,
                  Vector2 windowDimensions, int minDistanceFromEdge) {
        /**
         * Construct a new Ball instance.
         *
         * @param topLeftCorner Position of the object, in window coordinates (pixels).
         *                      Note that (0,0) is the top-left corner of the window.
         * @param dimensions    Width and height in window coordinates.
         * @param renderable    The renderable representing the object. Can be null, in which case
         * @param inputListener -an object that get input from the user
         * @param windowDimensions - a vector which represent the size of the current screen
         * @param minDistanceFromEdge - the minimum distance from the ages
         */
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowWidth = windowDimensions.x();
        this.minDistanceFromEdge = minDistanceFromEdge;
    }

    @Override
    public void update(float deltaTime) {
        /**
         * this methode update the behavior
         * of this object according to the game flow
         * it moves the paddle according to the user input
         * @param deltaTime the time we called this methode
         */
        super.update(deltaTime);

        Vector2 movementDir = Vector2.ZERO;
        //checks the input and decide the direction accordingly
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }

        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }

        setVelocity(movementDir.mult(MOVEMENT_SPEED));

        //checks if the user is trying to move out of boundaries
        if (getTopLeftCorner().x() < minDistanceFromEdge) {
            transform().setTopLeftCornerX(minDistanceFromEdge);
        } else if (getTopLeftCorner().x() > windowWidth - minDistanceFromEdge - getDimensions().x()) {
            transform().setTopLeftCornerX(windowWidth - minDistanceFromEdge - getDimensions().x());
        }
    }

}
