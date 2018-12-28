package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="holonomicDrive Normal", group="Testing")
public class holonomicDrive_0_1 extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {

        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
        waitForStart();

        while (opModeIsActive())
        {

            if(gamepad1.right_stick_x != 0 )
            {
                robot.manualRotate(gamepad1);
            }
            else
            {
                robot.manualDrive(gamepad1);
            }

            if(gamepad1.dpad_up)
            {
                robot.setBrake(true);
            }
            else if(gamepad1.dpad_down)
            {
                robot.setBrake(false);
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

