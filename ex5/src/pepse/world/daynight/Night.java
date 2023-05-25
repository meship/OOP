package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.GameConstants;
import java.awt.*;

/**
 * class representing the day night effect of the game.
 */
public class Night {

    /**
     * this method creates a night game object.
     * @param gameObjects the game's objects.
     * @param layer layer to put the night in.
     * @param windowDimensions the current window dimensions.
     * @param cycleLength the night day cycle length.
     * @return a night game object.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 windowDimensions,
                                    float cycleLength){

        RectangleRenderable rectangleRenderable = new RectangleRenderable(Color.BLACK);
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, rectangleRenderable);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night, layer);
        night.setTag(GameConstants.NIGHT_TAG);
        Transition<Float> transition = new Transition<Float>(
                night, night.renderer()::setOpaqueness,
                GameConstants.INITIAL_TRANSITION_VALUE, GameConstants.MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength/2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return night;
    }
}
