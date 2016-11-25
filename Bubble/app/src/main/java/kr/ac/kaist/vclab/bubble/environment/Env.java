package kr.ac.kaist.vclab.bubble.environment;

/**
 * Created by mnswpr on 11/23/2016.
 */
public class Env {

    public static int stateMic;

    private static Env ourInstance = new Env();
    public static Env getInstance() {
        return ourInstance;
    }

    // SETTING GLOBAL ENV VARIABLES
    private Env() {
        stateMic = 0; // 0: turn off, 1: turn on
    }
}
