package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Peter on 11/12/2015.
 */
public class TestBot implements DriverInterface
{

    static DcMotor leftMotor;
    static DcMotor rightMotor;
    private static final String leftMotorName = "leftMotor";
    private static final String rightMotorName = "rightMotor";

    public TestBot(HardwareMap hardwareMap)
    {
        leftMotor = hardwareMap.dcMotor.get(leftMotorName);
        rightMotor = hardwareMap.dcMotor.get(rightMotorName);
        leftMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rightMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
    }


    @Override
    public void moveStraightEncoders(float inches, float speed)
    {
        leftMotor.setTargetPosition((int)(111.11111*inches));
        rightMotor.setTargetPosition((int)(111.11111 * inches));
        leftMotor.setPower(speed);
        rightMotor.setPower(speed);
    }

    @Override
    public void pivotTurn(float degrees, float speed, boolean isLeft)
    {

    }

    @Override
    public void spinOnCenter(float degrees, float speed, boolean isLeft)
    {

    }

    @Override
    public void stop()
    {
        leftMotor.setPower(0.0f);
        rightMotor.setPower(0.0f);
    }

}
