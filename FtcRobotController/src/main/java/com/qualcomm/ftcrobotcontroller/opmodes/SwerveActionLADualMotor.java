package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Steve on 6/26/2015.
 * Accelerate a motor from a starting power to an ending power over a specified duration
 */
public class SwerveActionLADualMotor implements SwerveAction {

    private double currentpower;
    private double startpower;
    private double endpower;
    private DcMotor.Direction direction;
    private double durationSeconds;
    DcMotor leftmotor;
    DcMotor rightmotor;

    //these are used in all SwerveActions...move them up?
    private double startTime;
    private boolean done = false;
    private boolean started = false;
    private String name = "";


    public SwerveActionLADualMotor(String debugName, DcMotor targetLeftMotor, DcMotor targetRightMotor, DcMotor.Direction targetDirection, double targetStartPower, double targetEndPower, double targetDurationSeconds)
    {
        name = debugName;
        leftmotor = targetLeftMotor;
        rightmotor = targetRightMotor;
        startpower = targetStartPower;
        endpower = targetEndPower;
        currentpower = targetStartPower;
        direction = targetDirection;
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
        double time = currentTime.time(); //don't want the current time to change during this function
        if (time > (startTime + durationSeconds))
        {
            done = true;
            //ensure we have hit the final target power exactly
            //set physical motor power and direction here
            //commented out since we don't have motors yet!
            //leftmotor.setDirection(direction);
            //leftmotor.setPower(power);
            //rightmotor.setDirection(direction);
            //rightmotor.setPower(power);
        }
        else
        {
            //calculate currentpower
            double percent  = (time - startTime) / durationSeconds;
            //System.out.println("percent: " + percent);

            currentpower =  startpower + ((endpower - startpower) * percent);

            //set physical motor power and direction here
            //commented out since we don't have motors yet!
            //leftmotor.setDirection(direction);
            //leftmotor.setPower(power);
            //rightmotor.setDirection(direction);
            //rightmotor.setPower(power);
        }
    }

    public String ToString()
    {
        String message  = "LAMotor " + name;
        message += (started ? " started " : " not started ");
        if (started)
        {
            message += ("current: " + String.format("%.2f", currentpower));
        }
        return (message);
    }

}
