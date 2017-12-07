package org.firstinspires.ftc.teamcode.Deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Alliance;
import org.firstinspires.ftc.teamcode.NullbotHardware;

import static org.firstinspires.ftc.teamcode.NullbotHardware.getAngleDifference;

/**
 * Created by guberti on 10/17/2017.
 */
@Autonomous(name="FRONT BLUE diagonal block", group="Autonomous")
@Disabled
public class DiagonalBlockPlace extends LinearOpMode {

    NullbotHardware robot = new NullbotHardware();
    double ACCEPTABLE_HEADING_VARIATION = 0.05;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap, this, gamepad1, gamepad2);

        robot.color = Alliance.BLUE;

        robot.crunchBlockClaw();

        telemetry.clearAll();

        telemetry.log().add("Complete autonomous mode");
        telemetry.log().add("Be prepared for robot to MOVE");
        telemetry.log().add("Robot's current alliance is " + robot.color);
        telemetry.log().add("--------------------------");

        waitForStart();

        log("Lifting lift");
        robot.setLiftMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setTargetPosition(-500);
        robot.lift.setPower(0.5);
        robot.sleep(1000);

        log("Driving off pad");
        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        for (DcMotor m : robot.motorArr) {
            m.setPower(0.4);
            m.setTargetPosition(m.getCurrentPosition() + 2400);
        }

        waitUntilMovementsComplete();
        log("Driving sideways");

        for (DcMotor m : robot.motorArr) {
            m.setPower(0.4);
        }

        /*
        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() - 500);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() + 500);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() + 500);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() - 500);*/

        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() + 250);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() - 250);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() - 250);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() + 250);

        waitUntilMovementsComplete();

        log("Turning to position");
        turnToPos(-Math.PI/6);
        robot.sleep(1000);

        log("Driving at our correct angle");
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.setMotorSpeeds(robot.getDrivePowersFromAngle(0));
        robot.sleep(1000);

        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        for (DcMotor m : robot.motorArr) {
            m.setPower(0.4);
        }

        robot.frontLeft.setTargetPosition(robot.frontLeft.getCurrentPosition() - 200);
        robot.backLeft.setTargetPosition(robot.backLeft.getCurrentPosition() - 200);
        robot.frontRight.setTargetPosition(robot.frontRight.getCurrentPosition() - 200);
        robot.backRight.setTargetPosition(robot.backRight.getCurrentPosition() - 200);
        waitUntilMovementsComplete();
        stopMoving();

        robot.lift.setTargetPosition(0);
        robot.sleep(1000);
        robot.openBlockClaw();
        robot.sleep(1000);

    }

    public void turnToPos(double pos) {
        robot.setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double difference = Double.MAX_VALUE;
        ElapsedTime timeHeadingAcceptable = new ElapsedTime();

        while(Math.abs(difference) > ACCEPTABLE_HEADING_VARIATION && timeHeadingAcceptable.milliseconds() < 500 && opModeIsActive()) {
            robot.updateReadings();
            telemetry.addData("Heading difference", difference);
            double heading = robot.getGyroHeading();

            difference = getAngleDifference(pos, heading);
            double turnSpeed = difference;
            turnSpeed = Math.max(-0.75, Math.min(0.75, turnSpeed));

            // Don't go below 0.2

            if (Math.abs(turnSpeed) < 0.2) {
                turnSpeed = 0.2 * Math.signum(turnSpeed);
            }

            double[] unscaledMotorPowers = new double[4];
            telemetry.addData("Turnspeed", turnSpeed);

            for (int i = 0; i < unscaledMotorPowers.length; i++) {
                if (i % 2 == 0) {
                    unscaledMotorPowers[i] = turnSpeed;
                } else {
                    unscaledMotorPowers[i] = -turnSpeed;
                }
            }
            telemetry.addData("M1", unscaledMotorPowers[0]);
            telemetry.addData("M2", unscaledMotorPowers[1]);
            telemetry.addData("M3", unscaledMotorPowers[2]);
            telemetry.addData("M4", unscaledMotorPowers[3]);

            telemetry.update();

            robot.motorArr[0].setPower(unscaledMotorPowers[0]);
            robot.motorArr[1].setPower(unscaledMotorPowers[1]);
            robot.motorArr[2].setPower(unscaledMotorPowers[2]);
            robot.motorArr[3].setPower(unscaledMotorPowers[3]);

            if (Math.abs(difference) > ACCEPTABLE_HEADING_VARIATION) {
                timeHeadingAcceptable.reset();
            }
            //robot.setMotorSpeeds(unscaledMotorPowers);
        }

        robot.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        stopMoving();
    }

    public void stopMoving() {
        for (DcMotor m : robot.motorArr) {
            m.setPower(0);
        }
    }
    public void log(Object s) {
        telemetry.log().add(s.toString());
    }
    public void waitUntilMovementsComplete() {
        boolean done = false;

        while (!done) {
            done = true;
            for (DcMotor m : robot.motorArr) {
                if (Math.abs(m.getTargetPosition() - m.getCurrentPosition()) > 75) {
                    telemetry.addData(m.getDeviceName(), Math.abs(m.getTargetPosition() - m.getCurrentPosition()));
                    done = false;
                    break;
                }
            }
            telemetry.update();

        }
        stopMoving();
        robot.sleep(500);
    }
}