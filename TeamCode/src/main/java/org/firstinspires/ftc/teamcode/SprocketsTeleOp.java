package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Benla on 10/14/2018.
 */

@TeleOp (group = "Sprockets", name = "TeleOp")
public class SprocketsTeleOp extends RobotsBase
{
    @Override
    public void DefineOpMode ()
    {
        while (opModeIsActive())
        {
            leftDrive.setPower(-gamepad1.left_stick_y/2);
            rightDrive.setPower(-gamepad1.right_stick_y/2);

            idle();
        }
    }
}
