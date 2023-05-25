package src;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.brick_strategies.BrickStrategyFactory;
import src.brick_strategies.CollisionStrategy;
import src.gameobjects.*;

import java.awt.*;
import java.util.Random;

public class BrickerGameManager extends GameManager{
    public static final int BORDER_WIDTH = 15;
    private static final float BOARD_WIDTH = 700;
    private static final float BOARD_HEIGHT = 500;
    private static final Color BORDER_COLOR = Color.WHITE;
    private static final float BALL_MOVEMENT_SPEED = 300;
    private static final float BALL_HEIGHT = 30;
    private static final float BALL_WIDTH = 30;
    private static final int MINIMAL_SPACE = 30;
    private static final float PADDLE_WIDTH = 150;
    private static final float PADDLE_HEIGHT = 20;
    private static final int NUM_OF_BRICKS_PER_ROW = 8;
    private static final int NUM_OF_BRICKS_ROWS = 5;
    private static final int MIN_DISTANCE_FROM_SCREEN_EDGE = 15;
    private static final float SPACE_BETWEEN_BRICKS = 5;
    private static final float SPACE_FROM_BOARDERS = 5;
    private static final float BRICK_HEIGHT = 15;
    private static final float LIFE_SPACE = 2;
    private static final int NUM_OF_LIFE = 4;
    private static final float HEART_WIDTH = 30;
    private static final float HEART_HEIGHT = 30;
    private static final float NUMERIC_HEIGHT = 20;
    private static final float NUMERIC_WIDTH = 200;
    private Vector2 windowDimensions;
    private Ball ball;
    private WindowController windowController;
    private Counter bricksCounter;
    private Counter lifeCounter;
    private ImageRenderable heartImage;
    private BrickStrategyFactory brickStrategyFactory;


    BrickerGameManager(String title, Vector2 window_size){
        super(title, window_size);

    }


    private Ball buildBall(SoundReader soundReader, ImageReader imageReader){
        /**
         * this methode build a ball object and
         * adds it to the game objects
         * @param soundReader - an object that cam read sound files, so it can later be played in the game.
         * @param imageReader - an object that can read an image, so it can later be shown on the screen.
        */
        ImageRenderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        Ball ball = new Ball(Vector2.ZERO, new Vector2(BALL_HEIGHT, BALL_WIDTH), ballImage, collisionSound);
        ball.setVelocity(Vector2.DOWN.mult(BALL_MOVEMENT_SPEED));
        ball.setCenter(windowDimensions.mult((float) 0.5));
        this.gameObjects().addGameObject(ball);
        return ball;
    }

    private GameObject buildPaddle(ImageReader imageReader, UserInputListener inputListener){
        /**
         * this methode build a paddle object and
         * adds it to the game objects
         * @param imageReader - an object that can read an image, so it can later be shown on the screen.
         * @param inputListener -an object that get input from the user
         * @returns GameObject the paddle that was created
         */
        ImageRenderable paddleImage = imageReader.readImage("assets/paddle.png", false);

        GameObject userPaddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage,
                inputListener, windowDimensions, MIN_DISTANCE_FROM_SCREEN_EDGE);
        userPaddle.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y()-MINIMAL_SPACE));
        this.gameObjects().addGameObject(userPaddle);
        return userPaddle;
    }


    private void creatWalls(){
        /**
         * this methode creat the boarders of game
         * and adds it to the game objects
         */
        RectangleRenderable wallRenderer = new RectangleRenderable(BORDER_COLOR);
        gameObjects().addGameObject(new GameObject(Vector2.ZERO,
                new Vector2(BORDER_WIDTH, windowDimensions.y()), wallRenderer));

        gameObjects().addGameObject(new GameObject(new Vector2(windowDimensions.x()-BORDER_WIDTH, 0),
                new Vector2(BORDER_WIDTH, windowDimensions.y()), wallRenderer));

        gameObjects().addGameObject(new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(), BORDER_WIDTH), wallRenderer));

    }

    private void createBackground(ImageReader imageReader){
        /**
         * this methode creat the background of game
         * and adds it to the game objects
         */
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions,
                imageReader.readImage("assets/DARK_BG2_small.jpeg", false));

        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    private void createLifeHearts(){
        /**
         * this methode build a GraphicLifeCounter object and
         * adds it to the game objects
         */
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
        /**
         * this methode build a createNumericLifeCounter object and
         * adds it to the game objects
         */
        GameObjectCollection gameObjectCollection = gameObjects();
        GameObject numericLifeCounter = new NumericLifeCounter(lifeCounter, new Vector2(BORDER_WIDTH+SPACE_FROM_BOARDERS,
                windowDimensions.y()-(BORDER_WIDTH+HEART_HEIGHT+SPACE_FROM_BOARDERS+NUMERIC_HEIGHT+BORDER_WIDTH)),
                new Vector2(NUMERIC_WIDTH, NUMERIC_HEIGHT), gameObjectCollection);
        gameObjects().addGameObject(numericLifeCounter, Layer.BACKGROUND);

    }



    private void buildBrick(ImageReader imageReader){
        /**
         * this methode build a Brick object and
         * adds it to the game objects
         * @param imageReader - an object that can read an image, so it can later be shown on the screen.
         */
        //calculate the wanted width of a brick in relation to the screen width
        float brickWidth = (float) (windowDimensions.x()-(BORDER_WIDTH*2+SPACE_BETWEEN_BRICKS*SPACE_FROM_BOARDERS+
                Math.pow(SPACE_BETWEEN_BRICKS,2))) /NUM_OF_BRICKS_PER_ROW;
        ImageRenderable brickImage = imageReader.readImage("assets/brick.png", false);
        for (int i = 0; i < NUM_OF_BRICKS_ROWS; i++) {
            for (int j=0; j < NUM_OF_BRICKS_PER_ROW; j++){
                bricksCounter.increment();
                GameObjectCollection gameObjectCollection = gameObjects();
                CollisionStrategy collisionStrategy = brickStrategyFactory.getStrategy();
                GameObject brick = new Brick(
                        new Vector2(BORDER_WIDTH+SPACE_FROM_BOARDERS+(brickWidth+SPACE_BETWEEN_BRICKS)*j,
                        (BRICK_HEIGHT+SPACE_BETWEEN_BRICKS)*i+ BORDER_WIDTH+SPACE_FROM_BOARDERS),
                        new Vector2(brickWidth, BRICK_HEIGHT), brickImage, collisionStrategy,
                        bricksCounter);
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);

            }

        }
    }

    public void setBallFirstPlacement(Ball ball){
        /**
         * this methode set the ball first placement
         * and decide its moving direction
         * @param ball the ball to place
         */
        ball.setCenter(this.windowDimensions.mult(0.5f));
        float ballVelX = BALL_MOVEMENT_SPEED;
        float ballVelY = BALL_MOVEMENT_SPEED;
        Random random = new Random();
        if (random.nextBoolean()) ballVelX *= -1;
        if (random.nextBoolean()) ballVelY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        /**
         * this methode initialize all the game objects at
         * the beginning of a new game
         * @param imageReader - an object that can read an image, so it can later be shown on the screen.
         * @param soundReader - an object that cam read sound files, so it can later be played in the game.
         * @param inputListener -an object that get input from the user
         * @param windowController - an object that handle the window functionality
         */
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();
        brickStrategyFactory = new BrickStrategyFactory(gameObjects(), this, imageReader
        , soundReader, inputListener, windowController, windowDimensions);
        bricksCounter = new Counter(0);
        lifeCounter = new Counter(NUM_OF_LIFE);
        windowController.setTargetFramerate(50);
        this.windowController = windowController;
        ball = buildBall(soundReader, imageReader);
        GameObject userPaddle = buildPaddle(imageReader, inputListener);
        creatWalls();
        createBackground(imageReader);
        setBallFirstPlacement(ball);
        heartImage = imageReader.readImage("assets/heart.png", true);
        createLifeHearts();
        createNumericLifeCounter();
        buildBrick(imageReader);
    }

    private void removeObjectsOutOfBound(){
        /**
         * this methode removes all the
         * objects that passed the height of the screen
         */
        for(GameObject object: gameObjects()){
            if(object.getTopLeftCorner().y()>=windowDimensions.y()){
                gameObjects().removeGameObject(object);
            }
        }
    }
    private void handleEndOfGame(){
        /**
         * this methode handle the
         * end of the game. the methode will
         * display a fitting message upon
         * winning or losing, then if the player
         * choose to play again it will start a new game
         * and close the window otherwise
         */
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
                MockPaddle.isInstantiated = false;
                windowController.resetGame();
                return;
            }
            else
                windowController.closeWindow();

        }
    }
    @Override
    public void update(float deltaTime) {
        /**
         * this methode update the behavior
         * of this object according to the game flow
         * @param deltaTime the time we called this methode
         */
        float ballHeight = ball.getCenter().y();
        handleEndOfGame();

        //to prevent the end of the game before the last brick
        //has been broken
        if(bricksCounter.value() == 0){
            bricksCounter.decrement();
        }

        //check if the player has been disqualified
        if(ballHeight > windowDimensions.y()){
            lifeCounter.decrement();
            createNumericLifeCounter();
            setBallFirstPlacement(this.ball);
        }

        removeObjectsOutOfBound();

        super.update(deltaTime);

    }

    public static void main(String[] args){
        new BrickerGameManager("src.BrickerGameManager", new Vector2(BOARD_WIDTH, BOARD_HEIGHT)).run();
    }
}
