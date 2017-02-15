package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.HolonomicRobot;

/**
 * Created by 292486 on 1/31/2017.
 */

@TeleOp
public class ConfigurationTest extends OpMode {

    HolonomicRobot robot = new HolonomicRobot();

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.gyro.calibrate();
    }

    @Override
    public void loop() {
        if(gamepad1.a)
        {
            robot.frontLeft.setPower(.5);
        } else {
            robot.frontLeft.setPower(0);
        }

        if(gamepad1.b)
        {
            robot.frontRight.setPower(.5);
        } else {
            robot.frontRight.setPower(0);
        }

        if(gamepad1.x)
        {
            robot.backLeft.setPower(.5);
        } else {
            robot.backLeft.setPower(0);
        }

        if(gamepad1.y)
        {
            robot.backRight.setPower(.5);
        } else {
            robot.backRight.setPower(0);
        }

        if(gamepad1.left_bumper)
        {
            robot.intake.setPower(.5);
        } else {
            robot.intake.setPower(0);
        }

        if(gamepad1.right_bumper)
        {
            robot.shooterBlue.setPower(.5);
            robot.shooterRed.setPower(.5);
        } else {
            robot.shooterBlue.setPower(0);
            robot.shooterRed.setPower(0);
        }

        ///////////////////////////////////////////////////////

        if(gamepad2.a)
        {
            telemetry.addData("Sensor: ", "Gyro%f %f %f", robot.gyro.heading, robot.gyro.trapezoidalIntegrate(), robot.gyro.getAverage());
            robot.gyro.updateHeading();
        }

        if(gamepad2.b)
        {
            telemetry.addData("Sensor: ", "Lightf%f", robot.lightFloor.getRawLightDetected());
        }

        if(gamepad2.x)
        {
            telemetry.addData("Sensor: ", "Lightb%f", robot.lightBeacon.getRawLightDetected());
        }

        if(gamepad2.y)
        {
            telemetry.addData("Sensor: ", "Sonar%f", robot.sonar.getUltrasonicLevel());
        }

        telemetry.update();
    }
}
