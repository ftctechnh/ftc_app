package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="holonomicDrive Normal", group="Testing")
public class holonomicDrive_0_1 extends LinearOpMode
{
    Bogg robot;
    private double pushIn = -.4;
    private double pushOut = .6;

    @Override
    public void runOpMode()
    {

        robot = new Bogg(hardwareMap, gamepad1);
        robot.driveEngine.driveAtAngle(Math.PI);
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
                x+=.02;
            }

            if(gamepad1.dpad_down && x > .5)
            {
                x-=.02;
            }
            robot.setBrake(x);


            if(gamepad1.right_trigger > .2)
            {
                if (gamepad1.left_bumper && pushOut < 1)
                    pushOut += .02;
                else if (gamepad1.right_bumper && pushOut > -1)
                    pushOut -= .02;
            }
            else if(gamepad1.left_trigger > .2)
            {
                if (gamepad1.left_bumper && pushIn < 1)
                    pushIn += .02;
                else if (gamepad1.right_bumper && pushIn > -1)
                    pushIn -= .02;
            }
            else if(gamepad1.left_bumper)
            {
                robot.push.setPosition(pushIn);
            }
            else if(gamepad1.right_bumper)
            {
                robot.push.setPosition(pushOut);
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
            telemetry.addData("Push in", pushIn);
            telemetry.addData("Push out", pushOut);
            telemetry.addData("touchBottom", robot.sensors.touchBottom.isPressed());
            telemetry.addData("touchTop", robot.sensors.touchTop.isPressed());
            telemetry.addData("fixed distance", robot.sensors.dFixed.getDistance(DistanceUnit.INCH));
            telemetry.addData("mobile distance", robot.sensors.dMobile.getDistance(DistanceUnit.INCH));

            telemetry.update();
            idle();
        }
    }
}

