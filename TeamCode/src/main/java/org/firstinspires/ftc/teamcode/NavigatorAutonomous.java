package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.teamcode.NullbotHardware.clamp;

/**
 * Created by guberti on 10/17/2017.
 */
@Autonomous(name="Test Ultrasonic Nav", group="Demo")
public class NavigatorAutonomous extends NullbotGemOnlyAutonomous {

    final double ACCEPTABLE_HEADING_VARIATION = Math.PI / 45; // 1 degree

    @Override
    public void runOpMode() {

        robot.init(hardwareMap, this, gamepad1, gamepad2);

        for (DcMotor m : robot.motorArr) {
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        robot.closeBlockClaw();
        robot.raiseIntake();
        waitForStart();
        robot.lift.setTargetPosition(-100);
        robot.lift.setPower(.3);
        robot.sleep(200);
        driveUntilUltrasonicDist(-0.10, 0);
        turnToPos(Math.PI/2);
        driveUntilUltrasonicDist(0.159, Math.PI/2);
        turnToPos(0);
        robot.setMotorSpeeds(new double[] {0.5, 0.5, 0.5, 0.5});
        robot.sleep(1000);

    }

    public void driveUntilUltrasonicDist(double dist, double lockedHeading) {
        int successfulReadings = 10;
        ElapsedTime t = new ElapsedTime();
        while (opModeIsActive()) {
            double voltage = robot.frontUltrasonic.getVoltage();
            double difference = -robot.getSignedAngleDifference(lockedHeading, robot.getGyroHeading());
            double turnSpeed = difference / (Math.PI);
            turnSpeed = clamp(turnSpeed);

            double moveSpeed = (voltage - dist) * 10;
            telemetry.addData("Turnspeed: ", turnSpeed);
            telemetry.addData("Movespeed: ", moveSpeed);

            double[] powers = new double[4];

            for (int i = 0; i < powers.length; i++) {
                powers[i] = moveSpeed;
                if (i % 2 == 0) {
                    powers[i] += turnSpeed;
                } else {
                    powers[i] -= turnSpeed;
                }
            }

            robot.setMotorSpeeds(powers);
            telemetry.addData("Front ultrasonic: ", voltage);
            telemetry.update();

            if (Math.abs(voltage - dist) < 0.005 && t.milliseconds() > 500) {
                successfulReadings++;
            } else {
                successfulReadings = 0;
            }

            if (successfulReadings >= 2) {
                stopMoving();
                return;
            }
        }
    }

    public void turnToPos(double pos) {
        double difference = Double.MAX_VALUE;

        while(Math.abs(difference) > ACCEPTABLE_HEADING_VARIATION && opModeIsActive()) {
            robot.updateReadings();
            telemetry.addData("Heading difference", difference);
            double heading = robot.getGyroHeading();

            difference = robot.getSignedAngleDifference(pos, heading);

            double turnSpeed = Math.max(-0.6, Math.min(0.6, difference));

            double[] unscaledMotorPowers = new double[4];
            telemetry.addData("Turnspeed", turnSpeed);

            for (int i = 0; i < unscaledMotorPowers.length; i++) {
                if (i % 2 == 0) {
                    unscaledMotorPowers[i] = turnSpeed;
                } else {
                    unscaledMotorPowers[i] = -turnSpeed;
                }
            }
            telemetry.update();

            robot.setMotorSpeeds(unscaledMotorPowers);
        }
        stopMoving();
    }

    public void stopMoving() {
        for (DcMotor m : robot.motorArr) {
            m.setPower(0);
        }
    }
}