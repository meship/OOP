package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.GameConstants;
import java.awt.*;

/**
 * this class represent a sun halo.
 */
public class SunHalo {

    /**
     *
     * @param gameObjects the game's objects.
     * @param sun a sun to move around.
     * @param color the sun halo color.
     * @param layer layer to put the sun halo in.
     * @return sun halo object.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, GameObject sun, Color color){
        OvalRenderable ovalRenderable = new OvalRenderable(color);
        GameObject sunHalo = new GameObject(Vector2.ZERO,
                new Vector2(GameConstants.SUN_HALO_SIZE, GameConstants.SUN_HALO_SIZE),
                ovalRenderable);
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(GameConstants.SUN_HALO_TAG);
        gameObjects.addGameObject(sunHalo, layer);
        sunHalo.addComponent((deltaTime -> sunHalo.setCenter(sun.getCenter())));
        return sunHalo;
    }
}
