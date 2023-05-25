package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.GameConstants;

import java.util.Random;

/**
 * this class represent a leaf in the game.
 */
public class Leaf extends GameObject {

    private final float cycleLength;
    private final Random random;
    Transition transitionBackAndForth;
    Transition transitionLeafAngle;
    Transition transitionLefDimensions;

    /**
     * Construct a new leaf  GameObject instance.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, float cycleLength,
                Random random) {
        super(topLeftCorner, dimensions, renderable);
        this.cycleLength = cycleLength;
        this.random = random;
    }

    /**
     * this method invoked when the leaf is colliding with other object.
     * @param other other object the leaf collided with.
     * @param collision collision object.
     */
    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        super.onCollisionStay(other, collision);
        if(other.getTag().equals(GameConstants.FIRST_LAYER_TAG)){
            this.setVelocity(Vector2.ZERO);
            this.removeComponent(this.transitionBackAndForth);
            this.removeComponent(this.transitionLeafAngle);
            this.removeComponent(this.transitionLefDimensions);
        }
    }

    /**
     * this method change the leaf angels to simulate the effect of moving in the wind.
     */
    void leavesAngles(){
        transitionLeafAngle = new Transition<Float>(
                this,
                this.renderer()::setRenderableAngle, GameConstants.LEAF_ANGLE_INIT_VALUE,
                GameConstants.LEAF_ANGLE_FINAL_VALUE, Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    /**
     * this method change the leaf dimensions to simulate the effect of moving in the wind.
     */
    void leavesDimensions(){
        Vector2 dim = this.getDimensions().mult(GameConstants.LEAF_INITIAL_DIM_EXPAND);
        Vector2 shrinkDim = this.getDimensions().mult(GameConstants.LEAF_SHRINK_DIM);
        transitionLefDimensions = new Transition<Vector2>(
                this,
                this::setDimensions, dim, shrinkDim,
                Transition.LINEAR_INTERPOLATOR_VECTOR, GameConstants.LEAF_SHRINK_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }


    /**
     * this method make the leaf shake in random time.
     */
    void changeLeafTime(){
        ScheduledTask task = new ScheduledTask(this, random.nextFloat(),false,
                this::leavesAngles);
    }
}
