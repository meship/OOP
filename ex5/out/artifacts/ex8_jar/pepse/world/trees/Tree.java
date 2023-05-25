package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.GameConstants;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

/**
 * this class represent the trees in the game.
 */
public class Tree {

    private static final Random random = new Random();
    private final GameObjectCollection gameObjects;
    private final int layer;
    private final float cycleLength;
    private final Function<Float, Float> getHeightTerrain;
    private final int seed;
    private final HashMap<Float, ArrayList<Leaf>> xToleaves;
    private final HashMap<Vector2, GameObject> xToTree;

    /**
     * constructor.
     * @param gameObjects the game's objects.
     * @param layer layer to put the trees in.
     * @param cycleLength tree cycle (mainly for the move of the leaves).
     * @param getHeightTerrain a method reference which returns the height of the terrain in a coordinate.
     * @param seed a seed to determine the randomness of the game.
     */
    public Tree(GameObjectCollection gameObjects, int layer, float cycleLength,
                Function<Float, Float> getHeightTerrain, int seed) {
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.cycleLength = cycleLength;
        this.getHeightTerrain = getHeightTerrain;
        this.seed = seed;
        this.xToleaves = new HashMap<>();
        this.xToTree = new HashMap<>();
    }

    /**
     * this method creates trees in a range.
     * @param minX left bound of the make trees range.
     * @param maxX right bound of the make trees range.
     */
    public void createInRange(int minX, int maxX) {
        minX = (minX / Block.SIZE) * Block.SIZE;
        maxX = (maxX / Block.SIZE) * Block.SIZE;
        for (int i = minX; i < maxX; i += Block.SIZE) {
            random.setSeed(Objects.hash(i, seed));
            if (random.nextInt(GameConstants.PUT_TREE_RANDOM_BOUND) > GameConstants.DO_PUT_TREE_BOUND) {
                create(i, random.nextInt(GameConstants.TREE_ADD_HEIGHT_BOUND)+
                        GameConstants.TREE_HEIGHT_MIN_VALUE);
            }
        }
    }

    /**
     * this method remove trees in a range.
     * @param minX left bound of the remove trees range.
     * @param maxX right bound of the remove trees range.
     */
    public void removeInRange(int minX, int maxX) {
        minX = (minX / Block.SIZE) * Block.SIZE;
        maxX = (maxX / Block.SIZE) * Block.SIZE;
        for (int i = minX; i < maxX; i += Block.SIZE) {
            random.setSeed(Objects.hash(i, seed));
            if (random.nextInt(GameConstants.PUT_TREE_RANDOM_BOUND) > GameConstants.DO_PUT_TREE_BOUND) {
                removeLeaves(xToleaves.get((float)i));
                xToleaves.remove((float)i);
                for (int j=0;j<=GameConstants.TREE_HEIGHT_MIN_VALUE+ GameConstants.TREE_ADD_HEIGHT_BOUND;j++){
                    gameObjects.removeGameObject(xToTree.get(new Vector2(i,j)), layer);
                    xToTree.remove(new Vector2(i,j));
                }
            }
        }
    }

    /**
     * this method creates a single tree in the game, adds him to the gama objects and to the trees'
     * dictionary.
     * @param x x coordinate to make the tree.
     * @param heightTree tree start height.
     */
    private void create(float x, int heightTree) {
        if (xToleaves.containsKey(x)){
            return;
        }
        float heightTerrain = getHeightTerrain.apply(x);
        int normalized = ((int) ((heightTerrain / Block.SIZE))) * Block.SIZE -
                GameConstants.STEAM_NORMALIZE_POSITION;
        for (int i = 0; i < heightTree; i++) {
            RectangleRenderable rectangleRenderable = new RectangleRenderable(
                    ColorSupplier.approximateColor(GameConstants.TREE_STEAM_COLOR));
            Vector2 steamPosition = new Vector2(x, (normalized - ((i+1) * Block.SIZE)));
            GameObject treeBlock = new Block(steamPosition, rectangleRenderable);
            xToTree.put(new Vector2(x,i), treeBlock);
            gameObjects.addGameObject(treeBlock, layer);
            treeBlock.setTag(GameConstants.TREE_TAG);
        }
        xToleaves.put(x ,addLeaves(x, heightTree, heightTerrain));
    }

    /**
     * this method creates the death and revive effect of a leaf.
     * @param leaf a leaf to creates death and revive effect of a leaf
     * @param leafCenter the center position to set the leaf to.
     * @param leafDimensions the size of the leaf.
     */
    private void deathAndRevive(GameObject leaf, Vector2 leafCenter, Vector2 leafDimensions){
        leaf.setCenter(leafCenter);
        leaf.setVelocity(Vector2.ZERO);
        leaf.renderer().setRenderable(null);
        leaf.renderer().fadeIn(random.nextInt(GameConstants.RANDOM_FADE_IN_BOUND), ()->{
            gameObjects.removeGameObject(leaf, layer);
            RectangleRenderable rectangleRenderable = new RectangleRenderable(GameConstants.LEAVES_COLOR);
            Leaf newLeaf = addLeaf(Vector2.ZERO, new Vector2(Block.SIZE, Block.SIZE), rectangleRenderable);
            newLeaf.setCenter(leafCenter);
            leafLifeTime(newLeaf);
        });
    }

    /**
     * this method creates the life cycle of a given leaf.
     * @param leaf a leaf to create his life cycle.
     */
    private void leafLifeTime(Leaf leaf){
        Vector2 topLeftCorner = leaf.getCenter();
        Vector2 leafDimensions = leaf.getDimensions();
        ScheduledTask task = new ScheduledTask(leaf, random.nextInt(GameConstants.LEAF_TIME_LIFE_BOUND),
                false, ()->{
            leaf.transform().setVelocity(Vector2.DOWN.mult(GameConstants.LEAF_VELOCITY));
            leaf.transitionBackAndForth = new Transition<Float>(
                    leaf, leaf.transform()::setVelocityX,
                    Vector2.LEFT.x()*GameConstants.LEAF_VELOCITY,
                    Vector2.RIGHT.x()*GameConstants.LEAF_VELOCITY,
                    Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                    Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
            leaf.renderer().fadeOut(GameConstants.FADEOUT_TIME, ()->{deathAndRevive(leaf,
                    topLeftCorner, leafDimensions);});
        });
    }

    /**
     * this method creates a leaf and adds it to the game.
     * @param currPosition position to create the leaf.
     * @param leaveBlockSize leaf dimensions.
     * @param leafleRenderable leaf renderable.
     * @return the leaf which we created.
     */
    private Leaf addLeaf(Vector2 currPosition, Vector2 leaveBlockSize, RectangleRenderable leafleRenderable){
        Leaf leaf = new Leaf(currPosition, leaveBlockSize, leafleRenderable, cycleLength, random);
        gameObjects.addGameObject(leaf, layer);
        leaf.setTag(GameConstants.LEAF_TAG);
        leaf.leavesDimensions();
        leaf.changeLeafTime();
        return leaf;
    }

    /**
     * this method adds leaves to a steam, by his position.
     * @param x steam x position.
     * @param heightTree steam height.
     * @param heightTerrain the height of the terrain in the x coordinate.
     * @return list of the creates leaves.
     */
    private ArrayList<Leaf> addLeaves(float x, int heightTree, float heightTerrain) {
        RectangleRenderable rectangleRenderable;
        Vector2 steamPosition = new Vector2(x,-GameConstants.STEAM_NORMALIZE_POSITION+
                (((int) (heightTerrain / Block.SIZE)) * Block.SIZE - heightTree * Block.SIZE));
        Vector2 leaveBlockSize = new Vector2(Block.SIZE, Block.SIZE);
        Vector2 firstLeavePosition = steamPosition.add(
                new Vector2(GameConstants.FIRST_LEAVE_POS, GameConstants.FIRST_LEAVE_POS));
        ArrayList<Leaf> leaves = new ArrayList<Leaf>();
        for (int i = 0; i < GameConstants.LEAVES_IN_ROW; i++) {
            for (int j = 0; j < GameConstants.LEAVES_IN_ROW; j++) {
                if (random.nextInt(GameConstants.PUT_LEAF_RANDOM_BOUND) < GameConstants.DO_PUT_LEAF_BOUND) {
                    rectangleRenderable = new RectangleRenderable(
                            ColorSupplier.approximateColor(GameConstants.LEAVES_COLOR));
                    Vector2 currPosition = firstLeavePosition.add(new Vector2(Block.SIZE * i,
                            Block.SIZE * j));
                    Leaf leaf = addLeaf(currPosition, leaveBlockSize, rectangleRenderable);
                    leaves.add(leaf);
                    leafLifeTime(leaf);
                }
            }
        }
        return leaves;
    }

    /**
     * this method removes the list of input's leaves from the game objects.
     * @param leaves leaves to remove from the game objects.
     */
    private void removeLeaves(ArrayList<Leaf> leaves) {
        if(leaves == null){return;}
        for (Leaf leaf: leaves) {
            gameObjects.removeGameObject(leaf, layer);

        }
    }


}


