package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.brick_strategies.ChangeTimeScaleStrategy;

public class Slower extends GameObject {
    /**
     * this class responsible for creating Slower objects
     */
    private final WindowController windowController;
    private final GameObjectCollection gameObjectCollection;
    private  final static float DECREMENT_SCALE = 0.9f;


    public Slower(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, WindowController windowController,
                   GameObjectCollection gameObjectCollection) {
        /**
         * Construct a new GameObject instance.
         *
         * @param topLeftCorner Position of the object, in window coordinates (pixels).
         *                      Note that (0,0) is the top-left corner of the window.
         * @param dimensions    Width and height in window coordinates.
         * @param renderable    The renderable representing the object. Can be null, in which case
         * @param gameObjectCollection - all the objects on the string at the moment
         * @param windowController - an object that handle the window functionality
         *
         */
        super(topLeftCorner, dimensions, renderable);
        this.windowController = windowController;
        this.gameObjectCollection = gameObjectCollection;
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
        if(other instanceof Paddle){
            windowController.setTimeScale(DECREMENT_SCALE);
            this.gameObjectCollection.removeGameObject(this);
            ChangeTimeScaleStrategy.isRed = true;
        }
    }
}
