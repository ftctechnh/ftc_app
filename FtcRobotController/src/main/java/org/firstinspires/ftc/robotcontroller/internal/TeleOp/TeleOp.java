package org.firstinspires.ftc.robotcontroller.internal.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcontroller.internal.Devices.DriveSystem;

/**
 * Created by abnaveed on 10/13/2016.
 */


public class TeleOp extends OpMode
{
    // TeleOp
    public DriveSystem driveSystem;
    private static final float deadBand = .05f;

    @Override
    public void init()
    {
        driveSystem = new DriveSystem(hardwareMap);
    }

    @Override
    public void loop()
    {
        // Getting joystick values
        double leftJoystick = gamepad1.left_stick_y;
        double rightJoystick = gamepad1.right_stick_y;

        // Uses deadBand to prevent robot from constantly moving.
        if(leftJoystick > deadBand)
        {
            driveSystem.setLeft(leftJoystick);
        }
        else
        {
            driveSystem.setLeft(0);
        }

        if(rightJoystick > deadBand)
        {
            driveSystem.setRight(rightJoystick);
        }
        else
        {
            driveSystem.setRight(0);
        }
    }
}
