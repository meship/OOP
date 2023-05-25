package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.brick_strategies.CollisionStrategy;

public class Brick extends GameObject {
    /**
     * this class responsible for creating Brick object
     */

    private CollisionStrategy collisionStrategy;
    private Counter brickCounter;

    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy, Counter brickCounter) {
        /**
         * Construct a new Brick instance.
         *
         * @param topLeftCorner Position of the object, in window coordinates (pixels).
         *                      Note that (0,0) is the top-left corner of the window.
         * @param dimensions    Width and height in window coordinates.
         * @param renderable    The renderable representing the object. Can be null, in which case
         * @param collisionStrategy the collision strategy this brick implement
         * @param  brickCounter a counter that checks how many bricks are in the game at
         *                      a given moment.
         */
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.brickCounter = brickCounter;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        /**
         *  this methode responsible for the object behavior
         *  when it collied with another object
         * @param other the other object we collied with
         * @param collision information about this collision
         */
        if(other instanceof Quicken || other instanceof Slower){
            return;
        }
        collisionStrategy.onCollision(this, other, brickCounter);

    }
}
