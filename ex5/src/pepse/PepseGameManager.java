package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;
import pepse.world.Bird;

/**
 * The main class of the simulator.
 */
public class PepseGameManager extends GameManager {
    private Avatar gameAvatar;
    private Terrain gameTerrain;
    private Tree gameTree;
    private float prevLeftEdge;
    private float prevRightEdge;
    private WindowController windowController;

    /**
     * Runs the entire simulation.
     *
     * @param args This argument should not be used
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * this method initialize the game.
     *
     * @param imageReader      reads an image from link.
     * @param soundReader      reads sound from link.
     * @param inputListener    reads input from user keyboard.
     * @param windowController object which contains the game window operations and information.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        addCollision();
        createSky();
        createTerrain();
        createNight();
        createSunAndSunHalo();
        createTrees();
        createAvatar(inputListener, imageReader);
        createBird(imageReader);
    }

    /**
     * update every frame.
     *
     * @param deltaTime delta time.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float rightEdge = (int) (this.gameAvatar.getCenter().x() +
                this.windowController.getWindowDimensions().x() / 2) + GameConstants.BUFFER;
        float leftEdge = (int) (this.gameAvatar.getCenter().x() -
                this.windowController.getWindowDimensions().x() / 2) - GameConstants.BUFFER;
        if (prevRightEdge < rightEdge) {
            moveRight(rightEdge, leftEdge);
        }
        if (prevLeftEdge > leftEdge) {
            moveLeft(rightEdge, leftEdge);
        }
    }

    /**
     * this method creates a bird object and adds it to the game.
     * @param imageReader reads an image from link.
     */
    private void createBird(ImageReader imageReader) {
        GameObject bird = Bird.create(gameObjects(), Layer.FOREGROUND,
                Vector2.ZERO, imageReader,
                windowController.getWindowDimensions(), GameConstants.BIRD_CYCLE_LENGTH);
    }

    /**
     * this method add the collisions between layers.
     */
    private void addCollision() {
        gameObjects().layers().shouldLayersCollide(Layer.STATIC_OBJECTS, Layer.STATIC_OBJECTS,
                true);
        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, Layer.STATIC_OBJECTS, true);
    }

    /**
     * this method creates the sky for the game.
     */
    private void createSky() {
        GameObject sky = Sky.create(gameObjects(), windowController.getWindowDimensions(),
                Layer.BACKGROUND);
    }

    /**
     * this method creates the initial terrain for the game.
     */
    private void createTerrain() {
        gameTerrain = new Terrain(gameObjects(), Layer.STATIC_OBJECTS,
                windowController.getWindowDimensions(), GameConstants.SEED);
        gameTerrain.createInRange(-GameConstants.BUFFER,
                (int) windowController.getWindowDimensions().x() + GameConstants.BUFFER);
    }

    /**
     * this method creates the night and day effect for the game.
     */
    private void createNight() {
        GameObject night = Night.create(gameObjects(), Layer.FOREGROUND,
                windowController.getWindowDimensions(), GameConstants.NIGHT_CYCLE);
    }

    /**
     * this method creates the sun and the sun halo.
     */
    private void createSunAndSunHalo() {
        GameObject sun = Sun.create(gameObjects(), Layer.BACKGROUND, windowController.getWindowDimensions(),
                GameConstants.SUN_CYCLE);
        GameObject sunHalo = SunHalo.create(gameObjects(), Layer.BACKGROUND + GameConstants.SUN_LAYER_ADD,
                sun, GameConstants.SUN_COLOR);
    }

    /**
     * this method creates the tree object and initial trees.
     */
    private void createTrees() {
        gameTree = new Tree(gameObjects(), Layer.STATIC_OBJECTS, GameConstants.TREE_CYCLE,
                gameTerrain::groundHeightAt, GameConstants.SEED);
        gameTree.createInRange(-GameConstants.BUFFER,
                (int) windowController.getWindowDimensions().x() + GameConstants.BUFFER);
    }

    /**
     * this method creates the avatar.
     *
     * @param inputListener reads input from user keyboard.
     * @param imageReader   reads an image from link.
     */
    private void createAvatar(UserInputListener inputListener, ImageReader imageReader) {
        // create avatar.
        float xPosition = windowController.getWindowDimensions().x() / 2;
        Vector2 avatarPos = new Vector2(xPosition, gameTerrain.groundHeightAt(xPosition) -
                windowController.getWindowDimensions().y() / GameConstants.START_HEIGHT_FROM_SCREEN);
        gameAvatar = Avatar.create(gameObjects(), Layer.DEFAULT, avatarPos, inputListener, imageReader);
        setCamera(new Camera(gameAvatar,
                windowController.getWindowDimensions().mult(0.5f).add(avatarPos.mult(-1)),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
        this.prevRightEdge = avatarPos.x();
        this.prevLeftEdge = avatarPos.x();
    }

    /**
     * this method update the world when the avatar move left.
     *
     * @param rightEdge new right bound.
     * @param leftEdge  new left bound.
     */
    private void moveLeft(float rightEdge, float leftEdge) {
        gameTerrain.removeInRange((int) rightEdge, (int) prevRightEdge);
        gameTerrain.createInRange((int) leftEdge, (int) prevLeftEdge);
        gameTree.removeInRange((int) rightEdge, (int) prevRightEdge + GameConstants.BUFFER);
        gameTree.createInRange((int) leftEdge - GameConstants.BUFFER, (int) prevRightEdge);
        prevLeftEdge = leftEdge;
        prevRightEdge = rightEdge;
    }

    /**
     * this method update the world when the avatar move right.
     *
     * @param rightEdge new right bound.
     * @param leftEdge  new left bound.
     */
    private void moveRight(float rightEdge, float leftEdge) {
        gameTerrain.removeInRange((int) prevLeftEdge, (int) leftEdge);
        gameTerrain.createInRange((int) prevRightEdge, (int) rightEdge);
        gameTree.removeInRange((int) prevLeftEdge - GameConstants.BUFFER, (int) leftEdge);
        gameTree.createInRange((int) prevRightEdge, (int) rightEdge + GameConstants.BUFFER);
        prevLeftEdge = leftEdge;
        prevRightEdge = rightEdge;
    }

}

