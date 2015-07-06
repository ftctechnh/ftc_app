package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Steve on 6/26/2015.
 * Use a light sensor to follow a line.
 * Assumes that the robot has already hit the line
 */
public class SwerveActionDualMotorLineFollow implements SwerveAction {

    DcMotor leftmotor;
    DcMotor rightmotor;
    LightSensor sensor;

    //these are used in all SwerveActions...move them up?
    private double startTime;
    private boolean done = false;
    private boolean started = false;
    private String name = "";


    public SwerveActionDualMotorLineFollow(String debugName, DcMotor targetLeftMotor, DcMotor targetRightMotor, LightSensor targetSensor)
    {
        name = debugName;
        leftmotor = targetLeftMotor;
        rightmotor = targetRightMotor;
        sensor = targetSensor;
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
        double time = currentTime.time(); //don't want the current time to change during this function

        //implement line following heuristic here
    }

    public String ToString()
    {
        String message  = "LineFollow " + name;
        message += (started ? " started " : " not started ");
        //if (started)
        //{
        //    message += ("current: " + String.format("%.2f", currentpower));
        //}
        return (message);
    }

}
