package kr.ac.kaist.vclab.bubble.environment;

/**
 * Created by mnswpr on 11/23/2016.
 */

// FIXME SG
public class GameEnv {

    // TIME INFO
    private static long startTime;

    // SUCCESS INFO
    public static int successStatus;

    // ITEM INFO
    // FIXME SG (방금 새로운 아이템을 먹었는지 확인하는 BOOL 필요) --> BUBBLE 크기 정상화
    public static int numOfAchievedItems;
    public static int numOfTotalItems;
    public static float radiusOfItem;

    // BUBBLE INFO
    private static float scaleOfBubble;
    private static float minScaleOfBubble;
    private static float shrinkRatio;
    public static float radiusOfBubble;
    public static float minRadiusOfBubble;
    public static int levelOfBubble;
    public int lengthOfTrajectory;
    public float dampingOfInnerBubble;
    public float dampingOfBubbleCore;

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
        numOfTotalItems = 10;
        radiusOfItem = 2.0f;

        // BUBBLE INFO
        scaleOfBubble = 0.4f;
        minScaleOfBubble = 0.15f;
        shrinkRatio = 0.9985f;
        radiusOfBubble = 1.2f;
        minRadiusOfBubble = 0.3f;
        levelOfBubble = 3;
        lengthOfTrajectory = 90;
        dampingOfInnerBubble = 1.0f;
        dampingOfBubbleCore = 0.95f;
    }

    public static long getDuration(){
        long duration;
        duration = System.currentTimeMillis() - startTime;
        return duration;
    }

    private static void updateScaleOfBubble(){
        if(scaleOfBubble > minScaleOfBubble){
            scaleOfBubble = scaleOfBubble * shrinkRatio;
        }
    }

    public static float getScaleOfBubble(){
        updateScaleOfBubble();
        return scaleOfBubble;
    }

    private static void updateSuccessStatus(){
        if(scaleOfBubble <= minScaleOfBubble){
            successStatus = -1; // FAIL
        } else if(numOfTotalItems - numOfAchievedItems == 0){
            successStatus = 1;
        }
    }

    // FIXME SG (NOT USED YET)
    public static int getSuccessStatus(){
        updateSuccessStatus();
        return successStatus;
    }
}