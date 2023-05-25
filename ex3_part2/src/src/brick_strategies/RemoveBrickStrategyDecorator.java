package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public class RemoveBrickStrategyDecorator implements CollisionStrategy{
    /**
     * this class is the decorator of all the
     * objects that implements CollisionStrategy
     * when RemoveBrickStrategy is the base for all
     * the other Strategies
     */
    CollisionStrategy toBeDecorated;

    RemoveBrickStrategyDecorator(CollisionStrategy toBeDecorated){
        /**
         * Constructor
         * @param toBeDecorated another CollisionStrategy to decorate.
         */
        this.toBeDecorated = toBeDecorated;
    }

    @Override
    public GameObjectCollection getGameObjectCollection() {
        /**
         * this functions returns all the objects that are on the screen at
         * this moment.
         * @return GameObjectCollection an object that represent all the
         *          objects on the screen at this moment
         */
        return this.toBeDecorated.getGameObjectCollection();
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
        this.toBeDecorated.onCollision(thisObj, otherObj, counter);
    }
}
