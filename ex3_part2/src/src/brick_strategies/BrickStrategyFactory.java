package src.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import java.util.Random;

public class BrickStrategyFactory {
    /**
     * this class responsible for creating the
     * different Strategies of the bricks in the game
     */
    private static final int NUM_OF_STRATEGIES = 6;
    private static final int PUCK = 1;
    private static final int CHANGE_TIMESCALE = 2;
    private static final int ADD_PADDLE = 3;
    private static final int CHANGE_CAMERA = 4;
    private static final int DOUBLE_BEHAVIOR = 5;
    GameObjectCollection gameObjectCollection;
    private BrickerGameManager gameManager;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private Vector2 windowDimensions;
    private Counter brickCounter;
    private Random rnd;

    public BrickStrategyFactory(GameObjectCollection gameObjectCollection, BrickerGameManager gameManager,
                                ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener,
                                WindowController windowController, Vector2 windowDimensions){
        /**
         * Constructor
         * @param gameObjectCollection - all the objects on the string at the moment
         * @param gameManager - an object that manage the game and its functionality
         * @param imageReader - an object that can read an image, so it can later be shown on the screen.
         * @param soundReader - an object that cam read sound files, so it can later be played in the game.
         * @param inputListener -an object that get input from the user
         * @param windowController - an object that handle the window functionality
         * @param windowDimensions - a vector which represent the size of the current screen
         */

        this.gameObjectCollection = gameObjectCollection;
        this.gameManager = gameManager;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowDimensions;
        brickCounter = new Counter(0);
        rnd = new Random();
    }

    private CollisionStrategy helperGetStrategy(CollisionStrategy decorate, int min, int max) {
        /**
         * a helper methode to the getStrategy methode
         * it gets a CollisionStrategy to decorate and
         * choose a random strategy to decorate it with
         * @param decorate the CollisionStrategy to decorate
         * @param min the lower bound of the range of the pool
         * @param max the upper bound of the range of the pool
         */
        int choice = rnd.nextInt(max - min) + min;
        switch (choice) {
            case PUCK:
                return new PuckStrategy(decorate, imageReader, soundReader);
            case CHANGE_TIMESCALE:
                return new ChangeTimeScaleStrategy(decorate, windowController, imageReader);
            case ADD_PADDLE:
                return new AddPaddleStrategy(decorate, imageReader, inputListener, windowDimensions);
            case CHANGE_CAMERA:
                return new ChangeCameraStrategy(decorate, windowController, gameManager);
            case DOUBLE_BEHAVIOR:
                CollisionStrategy toDecorate = this.helperGetStrategy(decorate, 1, max);
                return this.helperGetStrategy(toDecorate, 1, max-1);

            default:
                return decorate;
        }
    }


    public CollisionStrategy getStrategy(){
        /**
         * this function create a CollisionStrategy and returns it to the
         * main game.
         * @returns CollisionStrategy one of the six
         *  CollisionStrategies that entitled in the helperGetStrategy
         *  function
         */
        brickCounter.increment();
        CollisionStrategy decorate = new RemoveBrickStrategy(this.gameObjectCollection);
        return helperGetStrategy(decorate, 0,NUM_OF_STRATEGIES);


    }
}
