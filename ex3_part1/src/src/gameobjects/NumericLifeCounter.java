package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

public class NumericLifeCounter extends GameObject {

    private Counter lifeCounter;
    private GameObjectCollection gameObjectCollection;
    private int curNumOfLife;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     */
    public NumericLifeCounter(Counter lifeCounter, Vector2 topLeftCorner, Vector2 dimensions,
        GameObjectCollection gameObjectCollection) {
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
        if(lifeCounter.value()<curNumOfLife){
            gameObjectCollection.removeGameObject(this, Layer.BACKGROUND);
            curNumOfLife--;
        }
        super.update(deltaTime);
    }
}
