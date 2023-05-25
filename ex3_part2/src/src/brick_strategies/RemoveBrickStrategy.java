package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

public class RemoveBrickStrategy implements CollisionStrategy{
    /**
     * this class represent the RemoveBrickStrategy its
     * responsible for removing the brick on coalition
     */


    private GameObjectCollection gameObjectCollection;

    public RemoveBrickStrategy(GameObjectCollection gameObjectCollection){
        /**
         * Constructor
         * @param gameObjectCollection - all the objects on the string at the moment
         */

        this.gameObjectCollection = gameObjectCollection;
    }

    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        /**
         * this methode responsible for the behavior
         * of this game object in when it collied
         * with another object
         * @param thisObj the cur object
         * @param otherObj the object that collied whith the current object
         * @param counter a Counter which count the num of collision. it represents
         *                the num of current bricks on the board
         */
        if(gameObjectCollection.removeGameObject(thisObj, Layer.STATIC_OBJECTS))
            counter.decrement();
    }

    @Override
    /**
     * this functions returns all the objects that are on the screen at
     * this moment.
     * @return GameObjectCollection an object that represent all the
     *          objects on the screen at this moment
     */
    public GameObjectCollection getGameObjectCollection() {
        return this.gameObjectCollection;
    }
}
