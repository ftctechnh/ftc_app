package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="holonomicDrive Curvy", group="Testing")
public class holonomicDrive_0_2 extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        waitForStart();
        double x = .6;

        while (opModeIsActive())
        {
            robot.manualCurvy(gamepad1, gamepad1);

            if(gamepad1.dpad_up && x < .6)
            {
                x+=.02;
            }

            if(gamepad1.dpad_down && x > .5)
            {
                x-=.02;
            }
            robot.setBrake(x);


            if(gamepad1.left_bumper)
            {
                robot.push(true);
            }
            else if(gamepad1.right_bumper)
            {
                robot.push(false);
            }


            robot.manualLift();
            // Display the current value
            telemetry.addLine("'Pressing A must move the arm down/robot up.'");
            telemetry.addLine("Set brake: d-down. Remove brake: d-up.");
            telemetry.addData("Gamepad x:", robot.gamepad.left_stick_x);
            telemetry.addData("Gamepad y:", robot.gamepad.left_stick_y);
            telemetry.addData("Drive x:", robot.driveEngine.xOut);
            telemetry.addData("Drive y:", robot.driveEngine.yOut);
            telemetry.addData("back encoder inches", robot.driveEngine.back.getCurrentPosition() * DriveEngine.inPerTicks);
            telemetry.addData("Brake x", x);
            telemetry.addData("touchBottom", robot.sensors.touchBottom.isPressed());
            telemetry.addData("touchTop", robot.sensors.touchTop.isPressed());
            telemetry.addData("fixed distance", robot.sensors.dFixed.getDistance(DistanceUnit.INCH));
            telemetry.addData("mobile distance", robot.sensors.dMobile.getDistance(DistanceUnit.INCH));

            telemetry.update();
            idle();
        }
    }
}

