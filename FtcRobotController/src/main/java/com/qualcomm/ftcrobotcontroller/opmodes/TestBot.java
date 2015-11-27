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
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }


    @Override
    public void moveStraightEncoders(float inches, float speed)
    {
        int encoderTarget = ((int)(111.1111111*inches+leftMotor.getCurrentPosition()));
        leftMotor.setPower(speed);
        rightMotor.setPower(speed);
        while(leftMotor.getCurrentPosition() < encoderTarget) {}
        leftMotor.setPower(0);
        rightMotor.setPower(0);
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
