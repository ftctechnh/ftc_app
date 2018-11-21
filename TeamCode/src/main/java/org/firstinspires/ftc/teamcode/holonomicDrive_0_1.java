package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="holonomicDrive_0_1", group="Testing")
public class holonomicDrive_0_1 extends LinearOpMode
{
    Bogg robot;
    double lastTime;
    double time;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1);
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

            if(gamepad1.left_bumper)
            {
                robot.driveEngine.back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.driveEngine.back.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
            if(gamepad1.right_bumper)
            {
                robot.driveEngine.back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.driveEngine.back.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.driveEngine.back.setTargetPosition(2000);
            }

            robot.manualLift();

            // Display the current value
            double ticksPerRev = 2240.0;
            double inPerRev = Math.PI * 5.0;
            double inPerTicks = inPerRev / ticksPerRev;
            telemetry.addData("back encoder inches", robot.driveEngine.back.getCurrentPosition() * inPerTicks);
            telemetry.addData("back encoder ticks", robot.driveEngine.back.getCurrentPosition());
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

