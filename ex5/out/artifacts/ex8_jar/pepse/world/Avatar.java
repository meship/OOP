package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.GameConstants;
import java.awt.event.KeyEvent;

public class Avatar extends GameObject {

    private float energy = GameConstants.AVATAR_INITIAL_ENERGY;
    private boolean isFlaying = false;
    private static AnimationRenderable flyAnimation;
    private static AnimationRenderable walkAnimation;
    private static Renderable standStill;
    private final GameObject text;
    private final UserInputListener inputListener;
    private float prevY;


    /**
     * Construct a new Avatar GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null.
     */
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  GameObjectCollection gameObjects, UserInputListener inputListener) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        Renderable textRender = new TextRenderable(String.format(GameConstants.ENERGY_FORMAT,
                (int)this.energy));
        text = new GameObject(new Vector2(GameConstants.ENERGY_POSITION,GameConstants.ENERGY_POSITION),
                new Vector2(GameConstants.ENERGY_X,GameConstants.ENERGY_Y), textRender);
        text.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(text);
    }

    /**
     * this method creates an avatar object.
     * @param gameObjects the game's objects.
     * @param layer layer to put the avatar in.
     * @param topLeftCorner top left position of the avatar's start position.
     * @param inputListener input reader from the keyboard.
     * @param imageReader image reader.
     * @return an avatar object.
     */
    public static Avatar create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener, ImageReader imageReader){
        standStill = addImage(imageReader, GameConstants.STAND_IMG_FORMAT);
        addFlyRenderable(imageReader);
        addWalkRenderable(imageReader);
        Avatar avatar = new Avatar(topLeftCorner,
                new Vector2(GameConstants.AVATAR_SIZE,GameConstants.AVATAR_SIZE),standStill, gameObjects,
                inputListener);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }

    /**
     * this method updates the avatar position in every frame.
     * @param deltaTime delta time.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        this.transform().setAccelerationY(GameConstants.GRAVITY);
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel = handleRightLeft(xVel, true);
        }
        else if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel = handleRightLeft(xVel, false);
        }
        else if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                inputListener.isKeyPressed(KeyEvent.VK_SHIFT) && energy >0) {
            handleFlight();
        }
        else if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0){
            handleJump();
        }
        else {
            handleStandStill();
        }
        transform().setVelocityX(xVel);
        if(this.transform().getVelocity().y() > GameConstants.GRAVITY){
            this.transform().setVelocity(new Vector2(this.transform().getVelocity().x(),
                    GameConstants.GRAVITY));
        }
        Renderable textRender = new TextRenderable(String.format(GameConstants.ENERGY_FORMAT,
                (int) this.energy));
        text.renderer().setRenderable(textRender);
    }

    /**
     * this method runs upon collision of the avatar with other objects, we want to make him stand.
     * @param other other objects of the collision.
     * @param collision collision object.
     */
    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Block && Math.abs(this.getCenter().x() - other.getCenter().x()) <
                GameConstants.MIN_DIST ){
            isFlaying = false;
            if(energy<GameConstants.AVATAR_INITIAL_ENERGY){
                energy += GameConstants.ENERGY_CHANGE;
            }

        }
    }

    /**
     * this method move the avatar right and left.
     * @param xVel current x velocity.
     * @param flip do we want to flip the avatar img (basically true upon left)
     * @return the x velocity after change.
     */
    private float handleRightLeft(float xVel, boolean flip){
        if (flip) {xVel -= GameConstants.VELOCITY_X;}
        else{xVel += GameConstants.VELOCITY_X;}
        this.renderer().setIsFlippedHorizontally(flip);
        if (!isFlaying)
            this.renderer().setRenderable(walkAnimation);
        else if (energy >0)
            energy -= GameConstants.ENERGY_CHANGE;
        return xVel;
    }

    /**
     * this method handle the avatar flight.
     */
    private void handleFlight(){
        transform().setVelocityY(GameConstants.VELOCITY_Y);
        this.renderer().setRenderable(flyAnimation);
        isFlaying = true;
        energy -= GameConstants.ENERGY_CHANGE;
    }

    /**
     * this method handle the avatar jump.
     */
    private void handleJump(){
        transform().setVelocityY(GameConstants.VELOCITY_Y);
    }

    /**
     * this method handle the avatar stand still mood.
     */
    private void handleStandStill(){
        this.renderer().setRenderable(standStill);
        isFlaying = false;
    }

    /**
     * this method creates the flight animations.
     * @param imageReader image reader.
     */
    private static void addFlyRenderable(ImageReader imageReader){
        Renderable[] fly  = new Renderable[GameConstants.FLIGHT_IMG_ARRAY_SIZE];
        for (int i = 0; i < fly.length; i++) {
            fly[i] = addImage(imageReader, String.format(GameConstants.FLY_IMG_FORMAT, i+1));
        }
        flyAnimation = new AnimationRenderable(fly, GameConstants.FLY_TIME_BETWEEN_CLIPS);
    }

    /**
     * this method creates the walk animations.
     * @param imageReader image reader.
     */
    private static void addWalkRenderable(ImageReader imageReader){
        Renderable[] walk  = new Renderable[GameConstants.WALK_IMG_ARRAY_SIZE];
        for (int i = 0; i < walk.length; i++) {
            walk[i] = addImage(imageReader, String.format(GameConstants.WALK_IMG_FORMAT, i+1));
        }
        walkAnimation = new AnimationRenderable(walk, GameConstants.WALK_TIME_BETWEEN_CLIPS);
    }

    /**
     * this method adds the img to the renewable.
     * @param imageReader img reader.
     * @param path img path to read.
     * @return the img reader animation.
     */
    private static Renderable addImage(ImageReader imageReader, String path){
        return imageReader.readImage(path, true);
    }

}
