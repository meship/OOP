package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.GameConstants;

/**
 * class representing a bird.
 */
public class Bird{

    private static AnimationRenderable flyAnimation;

    /**
     * this method creates a bird object.
     * @param gameObjects the game's objects.
     * @param layer layer to put the avatar in.
     * @param topLeftCorner top left position of the avatar's start position.
     * @param imageReader image reader.
     * @return an avatar object.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                 ImageReader imageReader, Vector2 windowDimensions, int cycleLength){
        addBirdFlyRenderable(imageReader);
        GameObject bird = new GameObject(topLeftCorner,
                new Vector2(GameConstants.BIRD_SIZE_X,GameConstants.BIRD_SIZE_Y),flyAnimation);
        bird.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        // bird transition
        Transition<Float> transition = new Transition<Float>(bird, x->
                bird.setCenter(new Vector2(x+GameConstants.BIRD_MOVEMENT_SIZE, GameConstants.BIRD_Y))
                , (float) -GameConstants.BIRD_SIZE_X , windowDimensions.x() + GameConstants.BIRD_SIZE_X ,
                Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP, null);
        gameObjects.addGameObject(bird, layer);
        return bird;
    }

    /**
     * this method creates the bird animations.
     * @param imageReader image reader.
     */
    private static void addBirdFlyRenderable(ImageReader imageReader){
        Renderable[] fly  = new Renderable[GameConstants.FLIGHT_IMG_ARRAY_SIZE_DRAGON];
        for (int i = 0; i < fly.length; i++) {
            fly[i] = addImage(imageReader, String.format(GameConstants.FLY_IMG_FORMAT_DRAGON, i+1));
        }
        flyAnimation = new AnimationRenderable(fly, GameConstants.FLY_TIME_BETWEEN_CLIPS_DRAGON);
    }

    /**
     * this method adds an image to a img reader.
     * @param imageReader image reader.
     * @param path path for img.
     * @return renderable with the img.
     */
    private static Renderable addImage(ImageReader imageReader, String path){
        return imageReader.readImage(path, true);
    }

}
