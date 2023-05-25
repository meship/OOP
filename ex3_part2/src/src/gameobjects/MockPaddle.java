package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class MockPaddle extends Paddle {
    /**
     * this class responsible for creating MockPaddle objects
     */

    private GameObjectCollection gameObjectCollection;
    private int numCollisionsToDisappear;
    public static boolean isInstantiated;
    private Counter collisionCounter;

    public MockPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener inputListener,
                      Vector2 windowDimensions, GameObjectCollection gameObjectCollection,
                      int minDistanceFromEdge, int numCollisionsToDisappear){
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
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistanceFromEdge);
        this.gameObjectCollection = gameObjectCollection;
        this.numCollisionsToDisappear = numCollisionsToDisappear;
        this.collisionCounter = new Counter(0);
        isInstantiated = true;
    }

    @Override
    /**
     * this methode update the behavior
     * of this object according to the game flow
     * @param deltaTime the time we called this methode
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        /**
         *  this methode responsible for the object behavior
         *  when it collied with another object
         * @param other the other object we collied with
         * @param collision information about this collision
         */
        super.onCollisionEnter(other, collision);
        collisionCounter.increment();
        if(collisionCounter.value() == numCollisionsToDisappear){
            gameObjectCollection.removeGameObject(this);
            isInstantiated = false;
        }
    }
}
