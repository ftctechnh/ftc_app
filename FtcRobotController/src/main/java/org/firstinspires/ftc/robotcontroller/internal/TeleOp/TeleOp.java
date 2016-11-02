package org.firstinspires.ftc.robotcontroller.internal.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeMeta;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwareK9bot;
import org.firstinspires.ftc.robotcontroller.internal.Devices.FlyWheelMechanic;

/**
 * Created by abnaveed on 10/13/2016.
 */


public class TeleOp extends OpMode
{
    // TeleOp
    public DcMotor leftMotor = null;
    public DcMotor rightMotor = null;
    public FlyWheelMechanic flymotor;
    private static final float deadBand = .05f;

    @Override
    public void init()
    {
        flymotor = new FlyWheelMechanic(hardwareMap);
    }

    @Override
    public void loop()
    {
        // Getting joystick values
        double leftJoystick = gamepad1.left_stick_y;
        double rightJoystick = gamepad1.right_stick_y;
        // Converting joystick values to motor power values
        rightMotor.setPower(rightJoystick);
        leftMotor.setPower(leftJoystick);

        if(leftJoystick > deadBand)
        {
            leftMotor.setPower(leftJoystick);
        }
        else
        {
            leftMotor.setPower(0);
        }

        if(rightJoystick > deadBand)
        {
            rightMotor.setPower(rightJoystick);
        }
        else
        {
            leftMotor.setPower(0);
        }


        /// flywheel
        boolean flyWheelPressed = gamepad2.right_bumper;
    }

}
