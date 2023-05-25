package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.trees.Leaf;

/**
 * this class represent a block in the game.
 */
public class Block extends GameObject {

    public static final int SIZE = 30;



    /**
     * Constructor.
     * @param topLeftCorner top left corener to create the block.
     * @param renderable renderable for the block img.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);

    }/**
     * this method set the velocity of the object which collide with the block to zero.
     * @param other other obkect which collided with the block.
     * @param collision collision object.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(!(other instanceof Leaf)){
            other.setVelocity(Vector2.ZERO);
        }

    }
}