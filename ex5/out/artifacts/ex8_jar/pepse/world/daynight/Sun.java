package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.GameConstants;
import java.awt.*;

/**
 * a class representing the sun object.
 */
public class Sun {

    /**
     * the static method creates a sun object.
     * @param windowDimensions the current game window dimensions.
     * @param cycleLength the sun cycle length.
     * @param gameObjects the game's objects.
     * @param layer layer to put the sun in.
     * @return a sun object
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 windowDimensions, float cycleLength){
        OvalRenderable ovalRenderable = new OvalRenderable(Color.YELLOW);
        GameObject sun = new GameObject(new Vector2(windowDimensions.x()/2,
                GameConstants.SUN_INITIAL_Y_POS),
                new Vector2(GameConstants.SUN_SIZE, GameConstants.SUN_SIZE), ovalRenderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(GameConstants.SUN_TAG);
        // sun transition
        Transition<Float> transition = new Transition<Float>(sun, angle->{
            float radius = windowDimensions.y()/2;
            float currX = (float) (windowDimensions.x()/2 +
                    GameConstants.COS_X_RADIUS_RATIO*(Math.cos(Math.toRadians(angle))*radius));
            float currY = (float) (windowDimensions.y()/2 +
                    GameConstants.SIN_Y_RADIUS_RATIO*(Math.sin(Math.toRadians(angle)))*radius);
            sun.setCenter(new Vector2(currX, currY));

            }
            ,GameConstants.SUN_INITIAL_TRANSITION_VALUE, GameConstants.FINAL_TRANSITION_VALUE,
                Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength, Transition.TransitionType.TRANSITION_LOOP,
                null);

        gameObjects.addGameObject(sun, layer);
        return sun;

    }
}
