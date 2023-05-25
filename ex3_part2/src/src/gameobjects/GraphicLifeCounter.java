package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class GraphicLifeCounter extends GameObject {
    /**
     * this class responsible for creating GraphicLifeCounter objects
     */
    private Counter lifeCounter;
    private GameObjectCollection objects;
    private static int numOfLife;


    public GraphicLifeCounter(Vector2 topLeftCorner, Vector2 dimensions,  Counter lifeCounter, Renderable renderable,
                              GameObjectCollection objects, int numOfLife) {
        /**
         * Construct a new GameObject instance.
         *
         * @param topLeftCorner Position of the object, in window coordinates (pixels).
         *                      Note that (0,0) is the top-left corner of the window.
         * @param dimensions    Width and height in window coordinates.
         * @param renderable    The renderable representing the object. Can be null, in which case
         * @param gameObjectCollection - all the objects on the string at the moment
         * @param numOfLife the number of disqualifications allowed before losing
         */
        super(topLeftCorner, dimensions, renderable);
        this.lifeCounter = lifeCounter;
        this.objects = objects;
        this.numOfLife = numOfLife;
    }

    @Override
    public void update(float deltaTime) {
        /**
         * this methode update the behavior
         * of this object according to the game flow
         * @param deltaTime the time we called this methode
         */
        if(lifeCounter.value()<numOfLife){
            objects.removeGameObject(this, Layer.BACKGROUND);
            numOfLife--;
        }
        super.update(deltaTime);


    }
}
