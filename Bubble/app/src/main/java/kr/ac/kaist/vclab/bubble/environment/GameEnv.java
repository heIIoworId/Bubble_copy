package kr.ac.kaist.vclab.bubble.environment;

/**
 * Created by mnswpr on 11/23/2016.
 */

public class GameEnv {
    // FLAG
    public static int collisionFlag;
    // SKYBOX INFO
    public static String imgFolder;
    // GYRO INFO
    public static float gyroScale;

    // MAP INFO
    public static float mapSizeX; // X-size (widthX) of map cube
    public static float mapSizeY; // Y-size (thickness) of map cube
    public static float mapSizeZ; // Z-size (widthZ) of map cube

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

        // GYRO INFO
        gyroScale = 1.5f;

        // SKYBOX INFO
        imgFolder = "sky2";

        // MAP INFO
        mapSizeX = 30f;
        mapSizeY = 3f;
        mapSizeZ= 30f;

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
        initialLocationOfBubble = new float[]{0,15.0f,0};
        radiusOfBubble = 1.2f;
        colorOfBubble = new float[] {0.3f, 0.8f, 0.9f};
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

    public static long getDuration(){
        long duration;
        duration = System.currentTimeMillis() - startTime;
        return duration;
    }

    public static float getScaleOfBubble(){
        if(scaleOfBubble > minScaleOfBubble){
            scaleOfBubble = scaleOfBubble * shrinkRatio;
        }
        return scaleOfBubble;
    }

    // FIXME SG (NOT USED YET)
    public static int getSuccessStatus(){
        if(scaleOfBubble <= minScaleOfBubble){
            successStatus = -1; // FAIL
        } else if(numOfTotalItems - numOfAchievedItems == 0){
            successStatus = 1;
        }
        return successStatus;
    }
}