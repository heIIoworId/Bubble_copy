package kr.ac.kaist.vclab.bubble.environment;

/**
 * Created by mnswpr on 11/23/2016.
 */

public class GameEnv {
    // FLAG
    public static int collisionFlag;
    public static boolean traceFlag;

    // VIEWPORT
    public static float minViewDist;
    public static float maxViewDist;

    // SKYBOX INFO
    public static String imgFolder;
    public static float skySize;

    // GYRO INFO
    public static float gyroScale;
    public static float[] gyroValue;
    public static float gyroLimit;
    public static float gyroAngleLimit;

    // MAP INFO
    public static float mapSizeX; // X-size (widthX) of map cube
    public static float mapSizeY; // Y-size (thickness) of map cube
    public static float mapSizeZ; // Z-size (widthZ) of map cube
    public static float mapUnitLength; // length of the side of a triangle
    public static float mapMaxHeight; // maximum height
    public static float mapMinHeight; // minimum height (>= -mapSizeY) 윗면 기준(0)
    public static float mapComplexity; // complexity (bigger complexity -> more & steeper mountains)

    // LAVA INFOR
    public static float lavaSizeX;
    public static float lavaSizeZ;
    public static float lavaHeight;

    // TIME INFO
    public static long startTime;

    // SUCCESS INFO
    public static int successStatus;

    // ITEM INFO
    public static int numOfAchievedItems;
    public static int numOfTotalItems;
    public static float radiusOfItem;
    public static int levelOfItem;
    public static float[] colorOfItem;

    // BUBBLE INFO
    public static float[] initialLocationOfBubble;
    public static float radiusOfBubble;
    public static float[] colorOfBubble;
    public static float initialScaleOfBubble;
    public static float scaleOfBubble;
    private static float minScaleOfBubble;
    private static float shrinkRatio;
    public static int levelOfBubble;
    public static float dampingOfInnerBubble;
    public static float distOfBubbleAndCamera;
    public static float bubbleDetectionRadius;

    // BUBBLE CORE INFO
    public float dampingOfBubbleCore;
    public int lengthOfTrace;
    public float[] traceColor;
    public int traceOffset;

    // WORLD INFO
    public static float[] gravity = new float[]{0f, -0.0006f, 0f};

    private static GameEnv ourInstance = new GameEnv();

    public static GameEnv getInstance() {
        return ourInstance;
    }

    private GameEnv() {
        // FLAG
        collisionFlag = 0;
        traceFlag = true;

        // VIEWPORT
        minViewDist = 1.0f; // near
        maxViewDist = 400.0f; // far

        // GYRO INFO
        gyroScale = 1.5f;
        gyroValue = new float[]{0, 0, 0};
        gyroLimit = 80f;
        gyroAngleLimit = 0.9f;

        // SKYBOX INFO
        imgFolder = "lake";
        skySize = 200.0f;

        // MAP INFO
        mapSizeX = 80.0f;
        mapSizeY = 20.0f;
        mapSizeZ = 80.0f;
        mapUnitLength = 1.5f;
        mapMaxHeight = 20.0f;
        mapMinHeight = -10.0f;
        mapComplexity = 5.0f;

        // LAVA INFO
        lavaSizeX = 200.0f;
        lavaSizeZ = 200.0f;
        lavaHeight = -80.0f;

        // TIME INFO
        startTime = System.currentTimeMillis();

        // SUCCESS INFO
        successStatus = 0; // -1: FAIL, 0: NOTHING, 1: SUCCESS

        // ITEM INFO
        numOfAchievedItems = 0;
        numOfTotalItems = 30;
        radiusOfItem = 0.5f;
        levelOfItem = 1;
        colorOfItem = new float[]{0f, 0f, 0.9f};

        // BUBBLE INFO
        initialLocationOfBubble = new float[]{0, 15.0f, 0};
        radiusOfBubble = 1.2f;
        colorOfBubble = new float[]{0.3f, 0.8f, 0.9f};
        initialScaleOfBubble = 0.4f;
        scaleOfBubble = initialScaleOfBubble;
        minScaleOfBubble = 0.15f;
        shrinkRatio = 0.9998f;
        levelOfBubble = 3;
        dampingOfInnerBubble = 1.0f;
        distOfBubbleAndCamera = 1.6f;
        bubbleDetectionRadius = 15f;

        // BUBBLE CORE INFO
        lengthOfTrace = 300; // 3의 배수여야함
        traceOffset = 60; // 3의 배수여야함
        dampingOfBubbleCore = 0.96f;
        traceColor = new float[]{0.9f, 0f, 0f};
    }

    public static int getDuration() {
        int duration;
        long temp;
        temp = System.currentTimeMillis() - startTime;
        temp = temp / 1000;
        duration = (int) Math.ceil(temp);
        return duration;
    }

    public static float getScaleOfBubble() {
        if (scaleOfBubble > minScaleOfBubble) {
            scaleOfBubble = scaleOfBubble * shrinkRatio;
        }
        return scaleOfBubble;
    }

    // FIXME SG (NOT USED YET)
    public static int getSuccessStatus() {
        if (scaleOfBubble <= minScaleOfBubble) {
            successStatus = -1; // FAIL
        } else if (numOfTotalItems - numOfAchievedItems == 0) {
            successStatus = 1;
        }
        return successStatus;
    }

    public static int getGameStatus(){
        // -1: GAME OVER, 0: NOTHING, 1: GAME CLEAR
        int status = 0;
        if (collisionFlag == 1){
            status = -1;
            return status;
        }
        if (scaleOfBubble <= minScaleOfBubble) {
            status = -1;
            return status;
        }
        if (numOfTotalItems - numOfAchievedItems == 0){
            status = 1;
            return status;
        }
        return status;
    }
}