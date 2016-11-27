package kr.ac.kaist.vclab.bubble.environment;

/**
 * Created by mnswpr on 11/23/2016.
 */

// FIXME SG
public class GameEnv {

    // ITEM INFO
    public static int numOfAchievedItems;
    public static int numOfTotalItems;
    public static float radiusOfItem;
    // TIME INFO
    public static long startTime;
    public static float duration;
    // BUBBLE INFO
    public static float radiusOfBubble;
    public static float minRadiusOfBubble;
    public static int levelOfBubble;
    // SUCCESS INFO
    public static boolean isSuccess;
    public static boolean isFailure;
    // BubbleCore INFO
    public int lengthOfTrajectory;

    private static GameEnv ourInstance = new GameEnv();
    public static GameEnv getInstance() {
        return ourInstance;
    }

    private GameEnv() {
        numOfAchievedItems = 0;
        numOfTotalItems = 10;
        startTime = System.currentTimeMillis();
        radiusOfItem = 2.0f;
        isSuccess = false;
        isFailure = false;
        radiusOfBubble = 1.2f;
        minRadiusOfBubble = 0.3f;
        levelOfBubble = 3;
        lengthOfTrajectory = 30;
    }

    public static void update(){
        //UPDATE DURATION
        long currentTime = System.currentTimeMillis();
        duration = (float) (currentTime - startTime);
        duration = duration / 1000.0f;
        Math.ceil(duration);

        //UPDATE SUCCESS STATUS
        if(radiusOfBubble > minRadiusOfBubble && numOfTotalItems - numOfAchievedItems == 0){
            isSuccess = true;
        } else if (radiusOfBubble <= minRadiusOfBubble){
            isFailure = true;
        }
    }
}
