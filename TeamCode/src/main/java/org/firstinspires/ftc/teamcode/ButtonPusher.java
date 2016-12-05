package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class ButtonPusher
{
    private ElapsedTime timer = null;

    DcMotor frontPusher = null;
    Gamepad gamepad = null;
    Sensors sensors = null;

    private DcMotor.Direction frontDirection = DcMotor.Direction.REVERSE;

    private double frontPower = 0;  //[0.0, 1.0]



    public ButtonPusher(HardwareMap hardwareMap, Gamepad gamepad, Sensors sensors)
    {
        this.gamepad = gamepad;

        this.sensors = sensors;

        frontPusher  = hardwareMap.dcMotor.get("front_push");

        frontPusher.setPower(frontPower);

        frontPusher.setDirection(frontDirection);
        timer = new ElapsedTime();
    }

    public Double getFrontPower()
    {
        return frontPower;
    }

    public void invertDirection()
    {
        if (frontDirection == DcMotor.Direction.FORWARD)
            frontDirection = DcMotor.Direction.REVERSE;
        else
            frontDirection = DcMotor.Direction.FORWARD;
    }

    public void setPower(double fPower) {
        frontPower = fPower;

        frontPusher.setPower(frontPower);
    }

    public void stop()
    {
        frontPower = 0;
        frontPusher.setPower(frontPower);
    }
}
