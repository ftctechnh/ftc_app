package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.LinkedList;

/**
 * Created by Steve on 6/29/2015.
 */
public class SwerveMotor {

    String name;
    private DcMotor motor;
    public LinkedList<SwerveAction> queue = new LinkedList<SwerveAction>();

    int actioncounter = 0;//for generating unique debugging names for actions

    public SwerveMotor(String debugName, DcMotor targetMotor)
    {
        name = debugName;
        motor = targetMotor;
    }

    public void AddAcceleration(DcMotor.Direction targetDirection, double targetStartPower, double targetEndPower, double targetDurationSeconds)
    {
        queue.add( new SwerveActionLAMotor(Integer.toString(actioncounter++), motor, targetDirection, targetStartPower, targetEndPower, targetDurationSeconds));
    }

    public void AddTimedMotion(DcMotor.Direction targetDirection, double targetPower, double targetDurationSeconds)
    {
        queue.add( new SwerveActionTimedMotor(Integer.toString(actioncounter++), motor, targetDirection, targetPower, targetDurationSeconds));
    }

    public void AddDelay(double targetDurationSeconds)
    {
        queue.add( new SwerveActionDelay(Integer.toString(actioncounter++), targetDurationSeconds));
    }

}
