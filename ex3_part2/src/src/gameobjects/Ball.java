package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class Ball extends GameObject {
    /**
     * this class responsible for creating a ball object
     */
    private Sound collisionSound;
    private Counter collisionCount;


    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        /**
         * Construct a new Ball instance.
         *
         * @param topLeftCorner Position of the object, in window coordinates (pixels).
         *                      Note that (0,0) is the top-left corner of the window.
         * @param dimensions    Width and height in window coordinates.
         * @param renderable    The renderable representing the object. Can be null, in which case
         * @param collisionSound  an object that cam read sound files, so it can later be played in the game.
         */
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.collisionCount = new Counter(0);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        /**
         *  this methode responsible for the object behavior
         *  when it collied with another object
         * @param other the other object we collied with
         * @param collision information about this collision
         */
        super.onCollisionEnter(other, collision);
        if(other instanceof Quicken || other instanceof Slower){
            return;
        }
       Vector2 setVel = getVelocity().flipped(collision.getNormal());
       setVelocity(setVel);
       collisionSound.play();
       collisionCount.increment();

    }

    public int getCollisionCount(){
        /**
         *  this methode responsible for the object behavior
         *  when it collied with another object
         * @param other the other object we collied with
         * @param collision information about this collision
         */
        return collisionCount.value();
    }
}
