package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="holonomicDrive Experimental Two Gamepads", group="Testing")
public class holonomicDrive_0_3 extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, gamepad2, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
        waitForStart();

        while (opModeIsActive())
        {
            robot.manualCurvy(gamepad1, gamepad2);

            if(gamepad1.dpad_up)
            {
                robot.setBrake(true);
            }
            else if(gamepad1.dpad_down)
            {
                robot.setBrake(false);
            }

            if(gamepad1.right_stick_x == 0 && gamepad1.left_stick_x == 0 && gamepad1.left_stick_y == 0)
                if(gamepad1.dpad_left )
                {
                    robot.driveEngine.orbit(48,0, -.3);
                }
                else if(gamepad1.dpad_right)
                {
                    robot.driveEngine.orbit(48,0, .3);
                }


            if(gamepad1.left_bumper)
            {
                robot.dropMarker(Bogg.Direction.Left);
            }
            else if(gamepad1.right_bumper)
            {
                robot.dropMarker(Bogg.Direction.Right);
            }


            robot.manualLift();

            // Display the current value
            // Display the current value
            telemetry.addLine("'Pressing A must move the arm down/robot up.'");
            telemetry.addLine("Set brake: d-down. Remove brake: d-up.");
            telemetry.addData("touchBottom", robot.sensors.touchBottomIsPressed());
            telemetry.addData("touchTop", robot.sensors.touchTopIsPressed());
            telemetry.addData("fixed distance", robot.sensors.getFixed());
            telemetry.addData("mobile distance", robot.sensors.getMobile());

            telemetry.update();
            idle();
        }
    }

}

