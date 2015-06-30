package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Steve on 6/26/2015.
 */
public class SwerveActionDelay implements SwerveAction {

    private double durationSeconds;

    //these are used in all SwerveActions...move them up?
    private double startTime;
    private boolean done = false;
    private boolean started = false;
    private String name = "";

    public SwerveActionDelay(String debugName, double targetDurationSeconds)
    {
        durationSeconds = targetDurationSeconds;
        name = debugName;
    }

    public boolean IsDone()
    {
        return done;
    }

    public boolean IsStarted()
    {
        return started;
    }

    public void Start(ElapsedTime currentTime)
    {
        startTime = currentTime.time();
        started = true;
    }

    public void Update(ElapsedTime currentTime)
    {
        if (currentTime.time() > (startTime + durationSeconds))
        {
            done = true;
        }
    }

    public String ToString()
    {
        String message  = "Delay " + name;
        message += (started ? " started " : " not started ");
        if (started)
        {
            message += ("s: " + String.format("%.2f", startTime) + " d:" + String.format("%.2f", durationSeconds));
        }
        return (message);
    }

}
