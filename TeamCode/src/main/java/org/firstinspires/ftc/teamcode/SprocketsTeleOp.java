package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Benla on 10/14/2018.
 */

/*

Controls:

GPAD 1 - Driver
Left Stick Up/Down = left wheel forward/back
Right Stick Up/Down = right wheel forward/back

GPAD 2 - Arms
Left Stick Up/Down = arms up/ down
Right Stick Up/Down = arms extend/retract

 */


@TeleOp (group = "Sprockets", name = "TeleOp")
public class SprocketsTeleOp extends RobotsBase
{
    @Override
    public void DefineOpMode ()
    {
        waitForStart();

        while (opModeIsActive())
        {
            leftDrive.setPower(-gamepad1.left_stick_y/2);
            rightDrive.setPower(-gamepad1.right_stick_y/2);

            leftArm.setPower(-gamepad2.left_stick_y/2);
            rightArm.setPower(-gamepad2.left_stick_y/2);

            armRaiser.setPower(-gamepad2.right_stick_y/4);


            if (LeftClawClosed = false)
            {
                if (gamepad2.left_bumper)
                {
                    leftClaw.setPosition(LeftClawClosedPosition);

                    LeftClawClosed = true;
                }

            } else
            {
                if (gamepad2.left_bumper)
                {
                    leftClaw.setPosition(LeftClawOpenPosition);

                    LeftClawClosed = false;
                }
            }

            if (RightClawClosed = false)
            {
                if (gamepad2.right_bumper)
                {
                    rightClaw.setPosition(RightClawClosedPosition);

                    RightClawClosed = true;
                }

            } else
            {
                    if (gamepad2.right_bumper)
                    {
                        rightClaw.setPosition(RightClawOpenPosition);

                        RightClawClosed = false;
                    }
            }

            telemetry.update();

            idle();
        }
    }
}
