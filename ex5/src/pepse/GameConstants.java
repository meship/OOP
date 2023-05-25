package pepse;

import pepse.world.Block;
import pepse.world.Sky;

import java.awt.*;
import java.util.Objects;

/**
 * the game constants.
 */
public final class GameConstants {

    // PepseGameManager
    public static final int SEED = 5;
    public static final int BUFFER = 60;
    public static final int NIGHT_CYCLE = 30;
    public static final int SUN_CYCLE = 30;
    public static final int SUN_LAYER_ADD = 10;
    public static final int TREE_CYCLE = 1;
    public static final int START_HEIGHT_FROM_SCREEN = 6;
    public static final Color SUN_COLOR = new Color(255, 255, 0, 20);
    public static final int BIRD_CYCLE_LENGTH = 10;
    public static final int BIRD_SIZE_X = 170;
    public static  final int BIRD_SIZE_Y = 120;


    // Night
    public static final Float MIDNIGHT_OPACITY = 0.5f;
    public static final String NIGHT_TAG = "night";
    public static final float INITIAL_TRANSITION_VALUE = 0;

    //Sun
    public static final int SUN_SIZE = 100;
    public static final String SUN_TAG = "night";
    public static final int SUN_INITIAL_Y_POS = 10;
    public static final float COS_X_RADIUS_RATIO = 1.5f;
    public static final float SIN_Y_RADIUS_RATIO = 1.0f;
    public static final float SUN_INITIAL_TRANSITION_VALUE = 270;
    public static final float FINAL_TRANSITION_VALUE = 630;

    // Sun halo
    public static final int SUN_HALO_SIZE = 200;
    public static final String SUN_HALO_TAG = "sunHalo";

    // Avatar
    public static final float VELOCITY_X = 300;
    public static final float VELOCITY_Y = -300;
    public static final float GRAVITY = 500;
    public static final float AVATAR_INITIAL_ENERGY = 100;
    public static final String ENERGY_FORMAT = "Energy is %d";
    public static final float ENERGY_POSITION = 20;
    public static final float ENERGY_X = 100;
    public static final float ENERGY_Y = 50;
    public static final int FLIGHT_IMG_ARRAY_SIZE = 9;
    public static final int WALK_IMG_ARRAY_SIZE = 6;
    public static final String FLY_IMG_FORMAT = "pepse/assets/Avatar/fly%s.png";
    public static final String WALK_IMG_FORMAT = "pepse/assets/Avatar/walk%s.png";
    public static final String STAND_IMG_FORMAT = "pepse/assets/Avatar/standStill.png";
    public static final int AVATAR_SIZE = 60;
    public static final float ENERGY_CHANGE = 0.5f;
    public static final float FLY_TIME_BETWEEN_CLIPS = 0.5f;
    public static final float WALK_TIME_BETWEEN_CLIPS = 0.3f;
    public static final float MIN_DIST = (float)AVATAR_SIZE/2 + (float)Block.SIZE/2 -5;

    //Sky color code
    public static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    public static final String SKY_TAG = "sky";


    // Terrain
    public static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    public static final String FIRST_LAYER_TAG = "FirstGroundLayer";
    public static final String TERRAIN_TAG = "Terrain";
    public static final float HEIGHT_SIZE_FROM_Y = (1 / 6f);
    public static final float HEIGHT_CYCLE = 0.1f;
    public static final int RANDOM_PHASE_BOUND = 3;
    public static final int HEIGHT_NORMALIZATION = 120;
    public static final int EXPAND_TERRAIN_VALUE = 180;

    // Leaf
    public static final float LEAF_ANGLE_INIT_VALUE = 85f;
    public static final float LEAF_ANGLE_FINAL_VALUE = 95f;
    public static final float LEAF_INITIAL_DIM_EXPAND = 100/99f;
    public static final float LEAF_SHRINK_DIM = 6/7f;
    public static final int LEAF_SHRINK_TIME = 20;

    // Tree
    public static final Color TREE_STEAM_COLOR = new Color(100, 50, 20);
    public static final Color LEAVES_COLOR = new Color(50, 200, 30);
    public static final int LEAVES_IN_ROW = 7;
    public static final float FADEOUT_TIME = 20;
    public static final String TREE_TAG = "treeBlock";
    public static final int RANDOM_FADE_IN_BOUND = 20;
    public static final int LEAF_TIME_LIFE_BOUND = 100;
    public static final int LEAF_VELOCITY = 20;
    public static final String LEAF_TAG = "leaf";
    public static final int STEAM_NORMALIZE_POSITION = 120;
    public static final float FIRST_LEAVE_POS = (float)(-1 * GameConstants.LEAVES_IN_ROW / 2 * Block.SIZE);
    public static final int PUT_LEAF_RANDOM_BOUND = 11;
    public static final int DO_PUT_LEAF_BOUND = 8;
    public static final int PUT_TREE_RANDOM_BOUND = 11;
    public static final int DO_PUT_TREE_BOUND = 9;
    public static final int TREE_HEIGHT_MIN_VALUE = 6;
    public static final int TREE_ADD_HEIGHT_BOUND = 5;

    // Bird
    public static final int BIRD_Y = 60;
    public static final int BIRD_MOVEMENT_SIZE = 5;
    public static final int FLIGHT_IMG_ARRAY_SIZE_DRAGON = 8;
    public static final float FLY_TIME_BETWEEN_CLIPS_DRAGON = 0.1f;
    public static final String FLY_IMG_FORMAT_DRAGON = "pepse/assets/bird/fly%s.png";
}
