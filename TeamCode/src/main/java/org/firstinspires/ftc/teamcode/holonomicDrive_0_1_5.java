package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="holonomicDrive AutoCorrect", group="Testing")
public class holonomicDrive_0_1_5 extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
        boolean rotating = false;
        waitForStart();

        while (opModeIsActive())
        {
            if(gamepad1.right_stick_x != 0 )
            {
                robot.manualRotate(gamepad1);
                rotating = true;
            }
            else if(rotating){
                rotating = false;
                robot.driveEngine.resetDistances();
            }
            else
            {
                robot.manualDriveAutoCorrect(gamepad1);
            }

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
            telemetry.addLine("'Pressing A must move the arm down/robot up.'");
            telemetry.addLine("Set brake: d-down. Remove brake: d-up.");
            telemetry.addData("back encoder inches", robot.driveEngine.back.getCurrentPosition() * DriveEngine.inPerTicks);
            telemetry.addData("touchBottom", robot.sensors.touchBottom.isPressed());
            telemetry.addData("touchTop", robot.sensors.touchTop.isPressed());
            telemetry.addData("fixed distance", robot.sensors.dFixed.getDistance(DistanceUnit.INCH));
            telemetry.addData("mobile distance", robot.sensors.dMobile.getDistance(DistanceUnit.INCH));

            telemetry.update();
            idle();
        }
    }
}

