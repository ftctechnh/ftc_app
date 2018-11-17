package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="holonomicKids", group="Testing")
public class holonomicKids extends LinearOpMode
{
    Bogg robot;
    double lastTime;
    double time;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        robot.alpha *= 5;

        waitForStart();
        double x = .6;

        while (opModeIsActive())
        {

            if(gamepad1.right_stick_x != 0 )
            {
                robot.manualRotate();
            }
            else
            {
                robot.manualDrive();
            }
            if(gamepad1.dpad_up && x < .6)
            {
                x+=.01;
            }

            if(gamepad1.dpad_down && x > .5)
            {
                x-=.01;
            }
            robot.setBrake(x);



            robot.manualLift();

            // Display the current value
            telemetry.addData("Servo x", x);
            telemetry.addData("touch", robot.sensors.touchBottom.isPressed());
            telemetry.addData("fixed distance", robot.sensors.dFixed.getDistance(DistanceUnit.INCH));
            telemetry.addData("mobile distance", robot.sensors.dMobile.getDistance(DistanceUnit.INCH));
            telemetry.addData("camera x, y", robot.camera.getLocation() == null ? "N/A" : robot.camera.getLocation());
            telemetry.addData("camera orientation", robot.camera.getHeading() == null ? "N/A" : robot.camera.getHeading());

            telemetry.update();
            idle();
        }
    }
}

