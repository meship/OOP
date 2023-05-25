package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

public class NumericLifeCounter extends GameObject {
    /**
     * this class responsible for creating NumericLifeCounter objects
     */
    private Counter lifeCounter;
    private GameObjectCollection gameObjectCollection;
    private int curNumOfLife;



    public NumericLifeCounter(Counter lifeCounter, Vector2 topLeftCorner, Vector2 dimensions,
        GameObjectCollection gameObjectCollection) {
        /**
         * Construct a new GameObject instance.
         *
         * @param topLeftCorner Position of the object, in window coordinates (pixels).
         *                      Note that (0,0) is the top-left corner of the window.
         * @param dimensions    Width and height in window coordinates.
         * @param renderable    The renderable representing the object. Can be null, in which case
         * @param gameObjectCollection - all the objects on the string at the moment
         */
        super(topLeftCorner, dimensions, null);
        TextRenderable numOfLife = new TextRenderable(String.format("You have %d life left",
                lifeCounter.value()));
        numOfLife.setColor(Color.WHITE);
        this.renderer().setRenderable(numOfLife);
        this.lifeCounter = lifeCounter;
        this.curNumOfLife = lifeCounter.value();
        this.gameObjectCollection = gameObjectCollection;
    }

    @Override
    public void update(float deltaTime) {
        /**
         * this methode update the behavior
         * of this object according to the game flow
         * @param deltaTime the time we called this methode
         */
        if(lifeCounter.value()<curNumOfLife){
            gameObjectCollection.removeGameObject(this, Layer.BACKGROUND);
            curNumOfLife--;
        }
        super.update(deltaTime);
    }
}
