package src.gameobjects;
import danogl.GameObject;
import src.brick_strategies.ChangeCameraStrategy;

public class BallCollisionCountdownAgent extends GameObject {
    /**
     * this class responsible for creating
     * BallCollisionCountdownAgent object
     */
    private ChangeCameraStrategy owner;
    private int countDownValue;
    private Ball ball;
    private int collisionTillNow;

    public BallCollisionCountdownAgent(Ball ball, ChangeCameraStrategy owner, int countDownValue){
        /**
         * Construct a new BallCollisionCountdownAgent instance.
         *
         * @param Ball a ball to follow
         * @param owner the brick that called this object.
         * @param countDownValue the num of collision until it sets the
         *                       POV back to normal
         */
        super(ball.getTopLeftCorner(), ball.getDimensions(), null);
        this.owner = owner;
        this.countDownValue = countDownValue;
        this.ball = ball;
        this.collisionTillNow = ball.getCollisionCount();
    }

    @Override
    public void update(float deltaTime) {
        /**
         * this methode update the behavior
         * of this object according to the game flow
         * @param deltaTime the time we called this methode
         */
        super.update(deltaTime);
        if(this.ball.getCollisionCount() >= countDownValue+this.collisionTillNow){
            owner.turnOffCameraChange();
        }

    }


}
