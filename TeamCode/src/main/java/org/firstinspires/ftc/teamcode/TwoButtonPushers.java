package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.List;

public class TwoButtonPushers
{


    private ElapsedTime timer = null;

    DcMotor frontPusher = null;
    DcMotor backPusher = null;
    Gamepad gamepad = null;


    private DcMotor.Direction frontDirection = DcMotor.Direction.REVERSE;
    private DcMotor.Direction backDirection = DcMotor.Direction.FORWARD;

    private double frontPower = 0;  //[0.0, 1.0]
    private double backPower = 0; //[0.0, 1.0]



    public TwoButtonPushers(HardwareMap hardwareMap, Gamepad gamepad)
    {
        this.gamepad = gamepad;

        frontPusher  = hardwareMap.dcMotor.get("left_push");
        backPusher = hardwareMap.dcMotor.get("right_push");

        frontPusher.setPower(frontPower);
        backPusher.setPower(backPower);

        frontPusher.setDirection(frontDirection);
        backPusher.setDirection(backDirection);
        timer = new ElapsedTime();
    }

    public Double getFrontPower()
    {
        return frontPower;
    }

    public Double getBackPower()
    {
        return backPower;
    }

    public void invertDirection()
    {
        if (frontDirection == DcMotor.Direction.FORWARD)
            frontDirection = DcMotor.Direction.REVERSE;
        else
            frontDirection = DcMotor.Direction.FORWARD;

        if (backDirection == DcMotor.Direction.FORWARD)
            backDirection = DcMotor.Direction.REVERSE;
        else
            backDirection = DcMotor.Direction.FORWARD;
    }

    public void setPower(double fPower, double bPower) {
        frontPower = fPower;
        backPower = bPower;

        frontPusher.setPower(frontPower);
        backPusher.setPower(backPower);
    }

    public void stop()
    {
        frontPower = backPower = 0;
        frontPusher.setPower(backPower);
        backPusher.setPower(backPower);
    }
}
