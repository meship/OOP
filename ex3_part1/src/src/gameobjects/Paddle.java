package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 300;
    private UserInputListener inputListener;
    private float windowWidth;
    private int minDistanceFromEdge;

    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener inputListener,
                  danogl.util.Vector2 windowDimensions, int minDistanceFromEdge) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowWidth = windowDimensions.x();
        this.minDistanceFromEdge = minDistanceFromEdge;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }

        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }

        setVelocity(movementDir.mult(MOVEMENT_SPEED));

        if (getTopLeftCorner().x() < minDistanceFromEdge) {
            transform().setTopLeftCornerX(minDistanceFromEdge);
        } else if (getTopLeftCorner().x() > windowWidth - minDistanceFromEdge - getDimensions().x()) {
            transform().setTopLeftCornerX(windowWidth - minDistanceFromEdge - getDimensions().x());
        }
    }

}
