package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Ball;
import src.gameobjects.Puck;


import java.util.Random;


public class PuckStrategy extends RemoveBrickStrategyDecorator{
    /**
     * this class represent the PuckStrategy its
     * responsible for adding three mock balls
     * in the place of the current brick
     */


    private static final int NUM_OF_BALLS = 3;
    private static final float SPACE_BETWEEN_BALLS = 5;
    private static final float BALL_MOVEMENT_SPEED = 300;
    private Ball[] balls;


    private ImageReader imageReader;
    private SoundReader soundReader;

    PuckStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader, SoundReader soundReader){
        /**
         * Constructor
         * @param toBeDecorated another CollisionStrategy to decorate.
         * @param imageReader - an object that can read an image, so it can later be shown on the screen.
         * @param soundReader - an object that cam read sound files, so it can later be played in the game.
         */
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.balls = new Ball[NUM_OF_BALLS];
    }



    private void buildBalls(GameObject thisObj){
        /**
         * this methode build a ball object and
         * adds it to the game objects
         * @param thisObj the brick so we can determine the
         *                scale of the balls
         */
        ImageRenderable ball_image = imageReader.readImage("assets/mockBall.png", true);
        Sound ballCollision = this.soundReader.readSound("assets/blop_cut_silenced.wav");
        float ballScale = thisObj.getDimensions().x()/3;
        for (int i=0;i<NUM_OF_BALLS;++i){
            float ballLocation = (float) (thisObj.getTopLeftCorner().x()+ 0.5*thisObj.getDimensions().x()+
                    SPACE_BETWEEN_BALLS*i);
            this.balls[i] = new Puck( new Vector2( ballLocation, thisObj.getTopLeftCorner().y()),
                    new Vector2(ballScale, ballScale), ball_image, ballCollision);
            this.toBeDecorated.getGameObjectCollection().addGameObject(this.balls[i]);
        }
    }

    public void setBallFirstPlacement(GameObject ball, Vector2 placement){
        /**
         * this methode set the ball first placement
         * and decide its moving direction
         * @param ball the ball to place
         * @param placment the poison
         */
        ball.setCenter(placement);
        float ballVelX = BALL_MOVEMENT_SPEED;
        float ballVelY = BALL_MOVEMENT_SPEED;
        Random random = new Random();
        if (random.nextBoolean()) ballVelX *= -1;
        if (random.nextBoolean()) ballVelY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
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
        buildBalls(thisObj);
        for(int i=0;i<balls.length;++i){
            this.setBallFirstPlacement(balls[i], balls[i].getTopLeftCorner());
        }
    }
}
