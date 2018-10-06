package org.firstinspires.ftc.teamcode;

/* A set of data that should be common across all autonomous op modes
 * Created by Howard on 12/28/2017.
 */

class Structures {
    static final int CLM = 0;
    static final int MOV = 2;
    static final int LFT = 3;
    static final int WT = 4;

    int[] mode;
    int[] clamp;
    int[] vLift;
    int[] heading;
    float[] vFwd;
    float[] vCrab;
    float[] vPwr;
    long[] dur;
    long currTime;
    float leftX;
    float leftY;
    float rightX;
    int status;
}
