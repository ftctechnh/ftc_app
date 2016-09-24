package org.firstinspires.ftc.teamcode.Util.PID;

/**
 * Created by hsunx on 9/23/2016.
 */
public class PidController {

    // Variables for pid
    protected float kp, ki, kd;

    // To find kd
    protected float previousError;

    protected float accumulatedError;
    public float GetAccumulatedError () {
        return accumulatedError;
    }

    public PidController (float kp, float ki, float kd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
    }

    public float NextOutput (float error) {
        float output = 0f;
        output = kp * error + ki * accumulatedError + kd * (error - previousError);
        accumulatedError += error;
        previousError = error;
        return output;
    }


}
