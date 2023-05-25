package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.GameConstants;

/**
 * this class represent sky object in the game.
 */
public class Sky {

    /**
     * this method creates a sky object and adds it to the game objects.
     * @param gameObjects the game's objects.
     * @param windowDimensions the game window dimensions.
     * @param skyLayer the layer to put the sky object.
     * @return the new sky object.
     */
    public static GameObject create(GameObjectCollection gameObjects, Vector2 windowDimensions, int skyLayer){
        GameObject sky = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(GameConstants.BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sky, skyLayer);
        sky.setTag(GameConstants.SKY_TAG);
        return sky;
    }
}
