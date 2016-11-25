package kr.ac.kaist.vclab.bubble.environment;

/**
 * Created by mnswpr on 11/23/2016.
 */

// FIXME SG
public class GameEnv {

    public static int numOfCurrentItems;
    public static int numOfTotalItems;
    private static long startTime;
    private static long currentTime;
    private static float duration;

    private static GameEnv ourInstance = new GameEnv();

    public static GameEnv getInstance() {
        return ourInstance;
    }

    private GameEnv() {
        numOfCurrentItems = 0;
        numOfTotalItems = 10;
        startTime = System.currentTimeMillis();
    }

    public static float getDuration(){
        currentTime = System.currentTimeMillis();
        duration = (float) (currentTime - startTime);
        return duration;
    }
}
