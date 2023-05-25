package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;
import src.gameobjects.BallCollisionCountdownAgent;
import src.gameobjects.Puck;

public class ChangeCameraStrategy extends RemoveBrickStrategyDecorator{
    /**
     * this class represent the ChangeCameraStrategy its
     * responsible for resetting the camera, so it will follow
     * the ball instead of the player POV
     */

    private static final int NUM_OF_COLLISIONS = 4;
    private WindowController windowController;
    private BrickerGameManager gameManager;
    private GameObject collisionCountdownAgent;

    ChangeCameraStrategy(CollisionStrategy toBeDecorated, WindowController windowController,
                         BrickerGameManager gameManager){
        /**
         * Constructor
         * @param toBeDecorated another CollisionStrategy to decorate.
         * @param gameManager - an object that manage the game and its functionality
         * @param windowController - an object that handle the window functionality
         */
        super(toBeDecorated);
        this.windowController = windowController;
        this.gameManager = gameManager;
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
        if(otherObj instanceof Ball && !(otherObj instanceof Puck) && gameManager.getCamera() == null){
            gameManager.setCamera(new Camera(otherObj, Vector2.ZERO, windowController.getWindowDimensions().mult(1.2f),
                    windowController.getWindowDimensions()));
            this.collisionCountdownAgent = new BallCollisionCountdownAgent((Ball) otherObj,
                    this, NUM_OF_COLLISIONS);
            this.toBeDecorated.getGameObjectCollection().addGameObject(collisionCountdownAgent, Layer.BACKGROUND);
        }



    }

    public void	turnOffCameraChange(){
        /**
         * this methode returns the camera
         * to the player POV
         */
        gameManager.setCamera(null);
        this.toBeDecorated.getGameObjectCollection().removeGameObject(collisionCountdownAgent, Layer.BACKGROUND);

    }
}
