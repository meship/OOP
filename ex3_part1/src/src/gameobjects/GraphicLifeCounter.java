package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class GraphicLifeCounter extends GameObject {

    private Counter lifeCounter;
    private GameObjectCollection objects;
    private static int numOfLife;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public GraphicLifeCounter(Vector2 topLeftCorner, Vector2 dimensions,  Counter lifeCounter, Renderable renderable,
                              GameObjectCollection objects, int numOfLife) {
        super(topLeftCorner, dimensions, renderable);
        this.lifeCounter = lifeCounter;
        this.objects = objects;
        this.numOfLife = numOfLife;
    }

    @Override
    public void update(float deltaTime) {
        if(lifeCounter.value()<numOfLife){
            objects.removeGameObject(this, Layer.BACKGROUND);
            numOfLife--;
        }


        super.update(deltaTime);


    }
}
