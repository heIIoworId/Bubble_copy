package kr.ac.kaist.vclab.bubble.environment;

/**
 * Created by mnswpr on 11/23/2016.
 */

// FIXME SG
public class GameEnv {

    public static int numOfCurrentItems;
    public static int numOfTotalItems;
    public static long startTime;
    public static float radiusOfItem;

    private static GameEnv ourInstance = new GameEnv();
    public static GameEnv getInstance() {
        return ourInstance;
    }

    private GameEnv() {
        numOfCurrentItems = 0;
        numOfTotalItems = 10;
        startTime = System.currentTimeMillis();
        radiusOfItem = 2.0f;

    }

    public static float getDuration(){
        long currentTime = System.currentTimeMillis();
        float duration = (float) (currentTime - startTime);
        duration = duration / 1000.0f;
        Math.ceil(duration);
        return duration;
    }
}
