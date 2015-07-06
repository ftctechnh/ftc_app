package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Steve on 6/26/2015.
 * Run two motors for a specified time
 */
public class SwerveActionTimedDualMotor implements SwerveAction {

    private double leftpower;
    private DcMotor.Direction leftdirection;
    private double rightpower;
    private DcMotor.Direction rightdirection;

    private double durationSeconds;
    DcMotor leftmotor;
    DcMotor rightmotor;

    //these are used in all SwerveActions...move them up?
    private double startTime;
    private boolean done = false;
    private boolean started = false;
    private String name = "";


    public SwerveActionTimedDualMotor(String debugName, DcMotor targetLeftMotor, DcMotor.Direction targetLeftDirection, double targetLeftPower,
                                      DcMotor targetRightMotor, DcMotor.Direction targetRightDirection, double targetRightPower,
                                      double targetDurationSeconds)
    {
        name = debugName;
        leftmotor = targetLeftMotor;
        leftpower = targetLeftPower;
        leftdirection = targetLeftDirection;
        rightmotor = targetRightMotor;
        rightpower = targetRightPower;
        rightdirection = targetRightDirection;
        durationSeconds = targetDurationSeconds;
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
        //set physical motor power and direction here
        //commented out since we don't have motors yet!
        //leftmotor.setDirection(direction);
        //leftmotor.setPower(power);
        //rightmotor.setDirection(direction);
        //rightmotor.setPower(power);
    }

    public void Update(ElapsedTime currentTime)
    {
        if (currentTime.time() > (startTime + durationSeconds))
        {
            done = true;
            //turn off physical motor??
            // this could be another parameter ("brake when done").
        }
    }

    public String ToString()
    {
        String message  = "TimedMotor " + name;
        message += (started ? " started " : " not started ");
        if (started)
        {
            message += ("s: " + String.format("%.2f", startTime) + " d:" + String.format("%.2f", durationSeconds));
        }
        return (message);
    }

}
