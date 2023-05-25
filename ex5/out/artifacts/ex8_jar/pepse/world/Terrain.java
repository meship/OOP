package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.GameConstants;
import pepse.util.ColorSupplier;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Terrain {

    private final HashMap<Float, Float> heightDict = new HashMap<Float, Float>();
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windowDimensions;
    private final Random random;
    private final int seed;
    private final HashMap<Vector2, Block> xToBlock;

    /**
     * constructor.
     * @param gameObjects the game's objects.
     * @param groundLayer layrt to put the terrain.
     * @param windowDimensions the game window dimensions.
     * @param seed the all game seed for random like game.
     */
    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.random = new Random();
        this.seed = seed;
        this.xToBlock = new HashMap<>();
    }

    /**
     * this method returns the ground height at a given x coordinate by a Perlin noise function.
     * @param x x coordinate to find its height.
     * @return height calculated from the function at the given x.
     */
    public float groundHeightAt(float x) {
        if (heightDict.containsKey(x)) {
            return heightDict.get(x);
        }
        float size = windowDimensions.y() * GameConstants.HEIGHT_SIZE_FROM_Y;
        float cycle = GameConstants.HEIGHT_CYCLE;
        float phase = random.nextInt(GameConstants.RANDOM_PHASE_BOUND);
        float returnedValue = windowDimensions.y() -
                (float) Math.abs((Math.sin(cycle * (x + phase))) * size) + GameConstants.BUFFER;
        heightDict.put(x, returnedValue);
        return returnedValue;
    }

    /**
     * this method creates terrain in a range.
     * @param minX left bound of the make trees range.
     * @param maxX right bound of the make trees range.
     */
    public void createInRange(int minX, int maxX) {
        minX = (minX / Block.SIZE) * Block.SIZE;
        maxX = (maxX / Block.SIZE) * Block.SIZE;
        for (int i = minX; i <= maxX; i += Block.SIZE) {
            random.setSeed(Objects.hash(i, seed));
            int currHeight = ((int) (groundHeightAt(i) / Block.SIZE)) * Block.SIZE;
            for (int j = currHeight - GameConstants.HEIGHT_NORMALIZATION;
                 j <= windowDimensions.y()+GameConstants.EXPAND_TERRAIN_VALUE; j += Block.SIZE) {
                // create block
                RectangleRenderable rectangleRenderable =
                        new RectangleRenderable(
                                ColorSupplier.approximateColor(GameConstants.BASE_GROUND_COLOR));
                Block block = new Block(new Vector2(i, j), rectangleRenderable);
                // create tag
                if (j == currHeight -  GameConstants.HEIGHT_NORMALIZATION) {
                    block.setTag(GameConstants.FIRST_LAYER_TAG);
                }
                else {
                    block.setTag(GameConstants.TERRAIN_TAG);
                }
               // check if already created, if not add.
                if (!xToBlock.containsKey(new Vector2(i,j))) {
                    xToBlock.put(new Vector2(i, j), block);
                    this.gameObjects.addGameObject(block, this.groundLayer);
                }
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
        for (int i = minX; i <= maxX; i += Block.SIZE) {
            random.setSeed(Objects.hash(i, seed));
            int currHeight = ((int) (groundHeightAt(i) / Block.SIZE)) * Block.SIZE;
            for (int j = currHeight - GameConstants.HEIGHT_NORMALIZATION;
                 j <= windowDimensions.y()+GameConstants.EXPAND_TERRAIN_VALUE; j += Block.SIZE) {
                this.gameObjects.removeGameObject(this.xToBlock.get(new Vector2(i,j)), this.groundLayer);
                this.xToBlock.remove(new Vector2(i,j));

            }
        }

    }

}
