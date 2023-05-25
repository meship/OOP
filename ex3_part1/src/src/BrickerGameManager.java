package src;

import src.brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.*;
import src.gameobjects.GraphicLifeCounter;

import java.awt.*;
import java.util.Random;

public class BrickerGameManager extends GameManager{
    public static final int BORDER_WIDTH = 10;
    private static final Color BORDER_COLOR = Color.WHITE;
    private static final float BALL_MOVEMENT_SPEED = 300;
    private static final float BALL_HEIGHT = 50;
    private static final float BALL_WIDTH = 50;
    private static final int MINIMAL_SPACE = 30;
    private static final float PADDLE_HEIGHT = 200;
    private static final float PADDLE_WIDTH = 20;
    private static final int NUM_OF_BRICKS_PER_ROW = 5;
    private static final int NUM_OF_BRICKS_ROWS = 8;
    private static final int MIN_DISTANCE_FROM_SCREEN_EDGE = 10;
    private static final float SPACE_BETWEEN_BRICKS = 5;
    private static final float SPACE_FROM_BOARDERS = 5;
    private static final float BRICK_HEIGHT = 15;
    private static final float LIFE_SPACE = 2;
    private static final int NUM_OF_LIFE = 5;
    private static final float HEART_WIDTH = 30;
    private static final float HEART_HEIGHT = 30;
    private static final float NUMERIC_HEIGHT = 20;
    private static final float NUMERIC_WIDTH = 200;
    private Vector2 windowDimensions;
    private GameObject ball;
    private WindowController windowController;
    private Counter bricksCounter;
    private Counter lifeCounter;
    private ImageRenderable heartImage;


    BrickerGameManager(String title, Vector2 window_size){
        super(title, window_size);

    }


    private GameObject buildBall(SoundReader soundReader, ImageReader imageReader){
        ImageRenderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        GameObject ball = new Ball(Vector2.ZERO, new Vector2(BALL_HEIGHT, BALL_WIDTH), ballImage, collisionSound);
        ball.setVelocity(Vector2.DOWN.mult(BALL_MOVEMENT_SPEED));
        ball.setCenter(windowDimensions.mult((float) 0.5));
        this.gameObjects().addGameObject(ball);
        return ball;
    }

    private GameObject buildPaddle(SoundReader soundReader, ImageReader imageReader, UserInputListener inputListener){
        ImageRenderable paddleImage = imageReader.readImage("assets/paddle.png", false);

        GameObject userPaddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_HEIGHT, PADDLE_WIDTH), paddleImage,
                inputListener, windowDimensions, MIN_DISTANCE_FROM_SCREEN_EDGE);
        userPaddle.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y()-MINIMAL_SPACE));
        this.gameObjects().addGameObject(userPaddle);
        return userPaddle;
    }


    private void creatWalls(){
        RectangleRenderable wallRenderer = new RectangleRenderable(BORDER_COLOR);
        gameObjects().addGameObject(new GameObject(Vector2.ZERO,
                new Vector2(BORDER_WIDTH, windowDimensions.y()), wallRenderer));

        gameObjects().addGameObject(new GameObject(new Vector2(windowDimensions.x()-BORDER_WIDTH, 0),
                new Vector2(BORDER_WIDTH, windowDimensions.y()), wallRenderer));

        gameObjects().addGameObject(new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(), BORDER_WIDTH), wallRenderer));

    }

    private void createBackground(ImageReader imageReader){
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions,
                imageReader.readImage("assets/DARK_BG2_small.jpeg", false));

        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    private void createLifeHearts(){
        for (int i = NUM_OF_LIFE; i >0; i--) {
            GameObjectCollection gameObjectCollection = gameObjects();
            GameObject lifeHearts = new GraphicLifeCounter(new Vector2(BORDER_WIDTH+SPACE_FROM_BOARDERS+(HEART_WIDTH+LIFE_SPACE)*i,
                    windowDimensions.y()-(BORDER_WIDTH+HEART_WIDTH+SPACE_FROM_BOARDERS)),
                    new Vector2(HEART_WIDTH, HEART_HEIGHT),
                    lifeCounter, heartImage , gameObjectCollection, NUM_OF_LIFE);
            gameObjects().addGameObject(lifeHearts, Layer.BACKGROUND);
        }

        }



    private void createNumericLifeCounter(){
        GameObjectCollection gameObjectCollection = gameObjects();
        GameObject numericLifeCounter = new NumericLifeCounter(lifeCounter, new Vector2(BORDER_WIDTH+SPACE_FROM_BOARDERS,
                windowDimensions.y()-(BORDER_WIDTH+HEART_HEIGHT+SPACE_FROM_BOARDERS+NUMERIC_HEIGHT+10)),
                new Vector2(NUMERIC_WIDTH, NUMERIC_HEIGHT), gameObjectCollection);
        gameObjects().addGameObject(numericLifeCounter, Layer.BACKGROUND);

    }



    private void buildBrick(ImageReader imageReader){
        float brickWidth = (windowDimensions.x()-(BORDER_WIDTH*2+2*SPACE_FROM_BOARDERS+5*SPACE_BETWEEN_BRICKS))
                /NUM_OF_BRICKS_PER_ROW;
        ImageRenderable brickImage = imageReader.readImage("assets/brick.png", false);
        for (int i = 0; i < NUM_OF_BRICKS_ROWS; i++) {
            for (int j=0; j < NUM_OF_BRICKS_PER_ROW; j++){
                bricksCounter.increment();
                GameObjectCollection gameObjectCollection = gameObjects();
                CollisionStrategy collisionStrategy = new CollisionStrategy(gameObjectCollection);
                GameObject brick = new Brick(
                        new Vector2(BORDER_WIDTH+SPACE_FROM_BOARDERS+(brickWidth+SPACE_BETWEEN_BRICKS)*j,
                        (BRICK_HEIGHT+SPACE_BETWEEN_BRICKS)*i+ BORDER_WIDTH+SPACE_FROM_BOARDERS),
                        new Vector2(brickWidth, BRICK_HEIGHT), brickImage, collisionStrategy, bricksCounter);
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);

            }

        }
    }

    public void setBallFirstPlacement(){
        ball.setCenter(windowDimensions.mult((float) 0.5));
        float ballVelX = BALL_MOVEMENT_SPEED;
        float ballVelY = BALL_MOVEMENT_SPEED;
        Random random = new Random();
        if (random.nextBoolean()) ballVelX *= -1;
        if (random.nextBoolean()) ballVelY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        bricksCounter = new Counter(0);
        lifeCounter = new Counter(NUM_OF_LIFE);
        windowController.setTargetFramerate(80);
        windowDimensions = windowController.getWindowDimensions();
        this.windowController = windowController;
        ball = buildBall(soundReader, imageReader);
        GameObject userPaddle = buildPaddle(soundReader, imageReader, inputListener);
        creatWalls();
        createBackground(imageReader);
        setBallFirstPlacement();
        heartImage = imageReader.readImage("assets/heart.png", true);
        createLifeHearts();
        createNumericLifeCounter();
        buildBrick(imageReader);
    }

    @Override
    public void update(float deltaTime) {
        float ballHeight = ball.getCenter().y();
        String prompt = "";

        if(lifeCounter.value() == 0){
            prompt = "You Lost :(";
        }

        if(bricksCounter.value() == -1){
            prompt = "You Won :)";
        }

        if (!prompt.isEmpty()){
            prompt += " Play Again?";
            if(windowController.openYesNoDialog(prompt)) {
//                lifeCounter = new Counter(NUM_OF_LIFE);
//                bricksCounter = new Counter(0);
                windowController.resetGame();
                return;
            }
            else
                windowController.closeWindow();

        }

        if(bricksCounter.value() == 0){
            bricksCounter.decrement();
        }

        if(ballHeight > windowDimensions.y()){
            lifeCounter.decrement();
            createNumericLifeCounter();
            setBallFirstPlacement();
        }

        super.update(deltaTime);



    }

    public static void main(String[] args){
        new BrickerGameManager("src.BrickerGameManager", new Vector2(700, 500)).run();
    }
}
