package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class BasicBot
{
    private DcMotorImplEx driveLeftOne, driveRightOne;

    public BasicBot(HardwareMap hardwareMap)
    {
        driveLeftOne = hardwareMap.get(DcMotorImplEx.class, "driveLeftOne");
        driveRightOne = hardwareMap.get(DcMotorImplEx.class, "driveRightOne");

        driveRightOne.setDirection(DcMotorImplEx.Direction.FORWARD);
        driveLeftOne.setDirection(DcMotorImplEx.Direction.REVERSE);
        driveRightOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveLeftOne.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        resetEncoders();
        driveMotors(0,0);
    }

    public void driveMotors(float lPow, float rPow)
    {
         driveRightOne.setPower(rPow);
         driveLeftOne.setPower(lPow);
    }

    public void goForward(float lPow, float rPow)
    {
        driveMotors(Math.abs(lPow), Math.abs(rPow));
    }

    public void goBackwards(float lPow, float rPow)
    {
        driveMotors(-Math.abs(lPow), -Math.abs(rPow));
    }

    public void stopDriveMotors()
    {
        driveMotors(0,0);
    }

    private void resetEncoders()
    {
        driveRightOne.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        driveLeftOne.setMode(DcMotorImplEx.RunMode.STOP_AND_RESET_ENCODER);
        driveRightOne.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
        driveLeftOne.setMode(DcMotorImplEx.RunMode.RUN_USING_ENCODER);
    }
}
