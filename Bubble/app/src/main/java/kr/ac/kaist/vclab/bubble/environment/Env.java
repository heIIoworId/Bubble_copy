package kr.ac.kaist.vclab.bubble.environment;

import java.text.DecimalFormat;

/**
 * Created by mnswpr on 11/23/2016.
 */
public class Env {

    public static int micStatus;
    public static int dirtyModeStatus;
    public static DecimalFormat printForm;



    private static Env ourInstance = new Env();
    public static Env getInstance() {
        return ourInstance;
    }

    // SETTING GLOBAL ENV VARIABLES
    private Env() {
        micStatus = 1; // 0: OFF, 1: ON
        dirtyModeStatus = 0; // 0: OFF, 1: ON
        printForm = new DecimalFormat("0.3f"); // DEBUG FLOAT FORM
    }
}
