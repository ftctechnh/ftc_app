package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Steve on 6/26/2015.
 */
public class SwerveActionOnMotor implements SwerveAction {

    private double power;
    private DcMotor.Direction direction;
    DcMotor motor;

    //these are used in all SwerveActions...move them up?
    private double startTime;
    private boolean done = false;
    private boolean started = false;
    private String name = "";


    public SwerveActionOnMotor(String debugName, DcMotor targetMotor, DcMotor.Direction targetDirection, double targetPower)
    {
        name = debugName;
        motor = targetMotor;
        power = targetPower;
        direction = targetDirection;
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
        //motor.setDirection(direction);
        //motor.setPower(power);
    }

    public void Update(ElapsedTime currentTime)
    {
        done = true;
    }

    public String ToString()
    {
        String message  = "OnMotor " + name;
        message += (started ? " started " : " not started ");
        return (message);
    }

}
