package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Quicken;
import src.gameobjects.Slower;

public class ChangeTimeScaleStrategy extends RemoveBrickStrategyDecorator{
    /**
     * this class represent the ChangeTimeScaleStrategy its
     * responsible for adjusting the timescale of the game
     * according to the specifier that fell from the current brick
     */

    private WindowController windowController;
    private static final float TIME_CHANGER_MOVEMENT_SPEED = 200;
    private ImageReader imageReader;
    public static boolean isRed;
    private GameObject timeChanger;

    ChangeTimeScaleStrategy(CollisionStrategy toBeDecorated, WindowController windowController,
                            ImageReader imageReader) {
        /**
         * Constructor
         * @param toBeDecorated another CollisionStrategy to decorate.
         * @param imageReader - an object that can read an image, so it can later be shown on the screen.
         * @param windowController - an object that handle the window functionality
         */
        super(toBeDecorated);
        this.windowController = windowController;
        this.imageReader = imageReader;
        isRed = true;
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
        float timeChangerScale = thisObj.getDimensions().x()/2;
        float timeChangerTopLeftCornerX = (float) (thisObj.getTopLeftCorner().x()+ 0.5*thisObj.getDimensions().x());
        if(isRed){
            // if it's the begging or the last specifier that touched the puddle was green it will create a red clock
            ImageRenderable clockRed =  imageReader.readImage("assets/quicken.png", true);
            timeChanger = new Quicken(
                    new Vector2(timeChangerTopLeftCornerX,
                    thisObj.getTopLeftCorner().y()), new Vector2(timeChangerScale,timeChangerScale),
                    clockRed, windowController, this.toBeDecorated.getGameObjectCollection()
            );
        }
        else {
            // if the last specifier that touched the puddle was red it will create a green clock
            ImageRenderable clockRed =  imageReader.readImage("assets/slow.png", true);
            timeChanger = new Slower(new Vector2(timeChangerTopLeftCornerX,
                    thisObj.getTopLeftCorner().y()), new Vector2(timeChangerScale,timeChangerScale),
                    clockRed, windowController, this.toBeDecorated.getGameObjectCollection());
        }


        this.toBeDecorated.getGameObjectCollection().addGameObject(timeChanger);
        this.timeChanger.setVelocity(Vector2.DOWN.mult(TIME_CHANGER_MOVEMENT_SPEED));
    }
}
