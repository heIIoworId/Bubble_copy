package kr.ac.kaist.vclab.bubble.environment;

/**
 * Created by mnswpr on 11/23/2016.
 */

public class GameEnv {

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
        // TIME INFO
        startTime = System.currentTimeMillis();

        // SUCCESS INFO
        successStatus = 0; // -1: FAIL, 0: NOTHING, 1: SUCCESS

        // ITEM INFO
        numOfAchievedItems = 0;
        numOfTotalItems = 15;
        radiusOfItem = 0.6f;
        levelOfItem = 1;
        colorOfItem = new float[]{0f, 0f, 0.9f};

        // BUBBLE INFO
        initialLocationOfBubble = new float[]{0,9.0f,0};
        radiusOfBubble = 1.2f;
        colorOfBubble = new float[] {0.3f, 0.8f, 0.9f};
        initialScaleOfBubble = 0.4f;
        scaleOfBubble = initialScaleOfBubble;
        minScaleOfBubble = 0.15f;
        shrinkRatio = 0.9985f;
        levelOfBubble = 3;
        dampingOfInnerBubble = 1.0f;
        distOfBubbleAndCamera = 2.0f;
        bubbleDetectionRadius = 0.5f;

        // BUBBLE CORE INFO
        lengthOfTrace = 300; // 3의 배수여야함
        traceOffset = 75; // 3의 배수여야함
        dampingOfBubbleCore = 0.95f;
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