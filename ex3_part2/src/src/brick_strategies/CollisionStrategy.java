package src.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.GameObject;
import danogl.util.Counter;

public interface CollisionStrategy {
    /**
     * this methode responsible for the behavior
     * of this game object in when it collied
     * with another object
     * @param thisObj the cur object
     * @param otherObj the object that collied whith the current object
     * @param counter a Counter which count the num of collision. relevant for the super
     *                methode
     */
    void onCollision(GameObject thisObj, GameObject otherObj, Counter counter);

    /**
     * this functions returns all the objects that are on the screen at
     * this moment.
     * @return GameObjectCollection an object that represent all the
     *          objects on the screen at this moment
     */
    GameObjectCollection getGameObjectCollection();
}
