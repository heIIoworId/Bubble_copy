package kr.ac.kaist.vclab.bubble.environment;

/**
 * Created by mnswpr on 11/23/2016.
 */

// FIXME SG
public class GameEnv {

    // TIME INFO
    public static long startTime = System.currentTimeMillis();
    public static float duration;

    // SUCCESS INFO
    public static boolean isSuccess = false;
    public static boolean isFailure = false;

    // ITEM INFO
    public static int numOfAchievedItems = 0;
    public static int numOfTotalItems = 10;
    public static float radiusOfItem = 2.0f;

    // BUBBLE INFO
    public static float radiusOfBubble = 1.2f;
    public static float minRadiusOfBubble = 0.3f;
    public static int levelOfBubble = 3;
    public int lengthOfTrajectory = 40;
    public float dampingOfInnerBubble = 1.0f;
    public float dampingOfBubbleCore = 0.95f;

    // WORLD INFO
    public static float[] gravity = new float[]{0f, -0.0006f, 0f};

    private static GameEnv ourInstance = new GameEnv();
    public static GameEnv getInstance() {
        return ourInstance;
    }

    private GameEnv() {
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