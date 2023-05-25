package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.MockPaddle;

public class AddPaddleStrategy extends RemoveBrickStrategyDecorator{
    /**
     * this class represent the AddPaddleStrategy its
     * responsible for adding another paddle in the middle
     * of the screen when a brick decorated with this
     * strategy breaks
     */

    private static final float PADDLE_WIDTH = 150;
    private static final float PADDLE_HEIGHT = 20;
    private static final int MIN_DISTANCE_FROM_SCREEN_EDGE = 15;
    private static final int NUM_OF_COLLISION = 3;
    private ImageReader imageReader;
    private UserInputListener inputListener;
    private Vector2 windowDimensions;

    AddPaddleStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader, UserInputListener inputListener,
                      Vector2 windowDimensions){
        /**
         * Construct a new AddPaddleStrategy instance.
         *
         * @param toBeDecorated another CollisionStrategy to decorate.
         * @param imageReader    an object that can read an image, so it can later be shown on the screen .
         * @param inputListener    an object that get input from the user
         * @param windowDimensions a vector which represent the size of the current screen
         */
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    private void buildPaddle(){
        /**
         * a helper methode
         * this methode build the mock puddle
         * and adds it to the game
         */
        ImageRenderable paddleImage = imageReader.readImage("assets/paddle.png", false);
        GameObject userPaddle = new MockPaddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                inputListener, windowDimensions, this.toBeDecorated.getGameObjectCollection(),
                MIN_DISTANCE_FROM_SCREEN_EDGE, NUM_OF_COLLISION);
        userPaddle.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y()/2));
        this.toBeDecorated.getGameObjectCollection().addGameObject(userPaddle);
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        /**
         * this methode responsible for the behavior
         * of this game object in when it collied
         * with another object
         * @param thisObj the cur object
         * @param otherObj the object that collied whith the current object
         * @param counter a Counter which count the num of collision. relevant for the super
         *                methode
         */
        super.onCollision(thisObj, otherObj, counter);
        if(!MockPaddle.isInstantiated){
            buildPaddle();
        }

    }
}
