package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 1/9/17.
 */
@Autonomous(name = "Red Beacons Autonomous", group = "autonomous programs")
public class AutonomousBeaconsRed extends LinearOpMode {

    private ZoidbergHardware robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new ZoidbergHardware(hardwareMap);

        robot.resetDriveEncoders();

        // use encoders
        robot.getBackLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getBackRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getFrontRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("color sensor red", robot.getColorSensor().red());
        telemetry.addData("color sensor blue", robot.getColorSensor().blue());
        telemetry.update();

        // wait for initialization
        waitForStart();

        // set target position for initial diagonal drive motion
        robot.getFrontLeftDrive().setTargetPosition(ZoidbergHardware.COUNTS_PER_INCH * 79);
        robot.getBackRightDrive().setTargetPosition(-ZoidbergHardware.COUNTS_PER_INCH * 79);

        robot.getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getBackRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.getFrontLeftDrive().setPower(0.5);
        robot.getBackRightDrive().setPower(0.5);

        // wait for the drive motors to stop
        while(opModeIsActive()
                && robot.getFrontLeftDrive().isBusy()
                && robot.getBackRightDrive().isBusy()) {

            telemetry.addData("Path",  "Running at %7d :%7d",
                    robot.getFrontLeftDrive().getCurrentPosition(),
                    robot.getBackRightDrive().getCurrentPosition());

            telemetry.addData("front left target", robot.getFrontLeftDrive().getTargetPosition());
            telemetry.addData("back right target", robot.getBackRightDrive().getTargetPosition());
            telemetry.update();
            idle();
        }

        robot.getRuntime().reset();
        while (robot.getRuntime().milliseconds() < 250) {
            robot.resetDriveEncoders();
            idle();
        }

        // run using encoders again
        robot.getBackLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getBackRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getFrontRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while(opModeIsActive() && robot.getOds3().getRawLightDetected() < 1.5) {
            robot.driveLeft(0.2);
            telemetry.addData("ods3", robot.getOds3().getRawLightDetected());
            telemetry.update();
        }

        robot.resetDriveEncoders();
        robot.stopRobot();

        claimBeacon();

        // launch the first (loaded) particle
//        robot.launchParticle();

        // open intake door
//        robot.getDoor3().setPosition(0.25);

        // run the intake
//        robot.getRuntime().reset();
//        while(opModeIsActive() && robot.getRuntime().milliseconds() < 500) {
//            robot.getIntakeMotor().setPower(-1);
//        }
//        robot.getIntakeMotor().setPower(0);

        // launch the second particle
//        robot.launchParticle();

        // strafe to the right to second beacon
        encoderStrafe(0.5, 24, 24);

        // line up against wall
        encoderDrive(0.6, 6, 6);

        while(opModeIsActive() && robot.getFrontRange().cmUltrasonic() < 8) {
            robot.driveBackward(0.2);
        }
        robot.stopRobot();

        // pause for 0.25 seconds
        robot.getRuntime().reset();
        while (robot.getRuntime().milliseconds() < 250) {
            robot.resetDriveEncoders();
            idle();
        }

        // run using encoders again
        robot.getBackLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getBackRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getFrontRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // look for the white line leading to the second beacon
        while(opModeIsActive() && robot.getOds3().getRawLightDetected() < 1.5) {
            robot.driveRight(0.2);
            telemetry.addData("ods3", robot.getOds3().getRawLightDetected());
            telemetry.update();
        }

        robot.resetDriveEncoders();
        robot.stopRobot();

        // claim the second beacon
        claimBeacon();

        // turn left for launching
        encoderDrive(0.5, -8, 8);

        // drive backward for launching
        encoderDrive(0.5, -12, -12);

        // launch the first (loaded) particle
        robot.launchParticle();

        // open intake door
        robot.getDoor3().setPosition(0.25);

        // run the intake
        robot.getRuntime().reset();
        while(opModeIsActive() && robot.getRuntime().milliseconds() < 500) {
            robot.getIntakeMotor().setPower(-1);
        }
        robot.getIntakeMotor().setPower(0);

        // launch the second particle
        robot.launchParticle();
    }

    private void claimBeacon() {
        // first push
        while(opModeIsActive() && robot.getFrontRange().cmUltrasonic() >= 7) {
            // run without encoders again
            robot.getBackLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.getBackRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.getFrontRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.driveForward(0.2);
        }
        robot.stopRobot();

        // pause for the beacon to change color
        robot.getRuntime().reset();
        while(opModeIsActive() && robot.getRuntime().milliseconds() < 500) {
            idle();
        }

        robot.resetDriveEncoders();

        // drive backward
        encoderDrive(0.5, -2, -2);

        // check for blue
        if(robot.getColorSensor().blue() > 0) {
            robot.getRuntime().reset();
            while(opModeIsActive() && robot.getRuntime().milliseconds() < 5000) {
                idle();
            }

            // second push
            while(opModeIsActive() && robot.getFrontRange().cmUltrasonic() >= 5) {
                robot.driveForward(0.2);
            }

            robot.resetDriveEncoders();

            // second drive backward
            encoderDrive(0.5, -2, -2);
        }

        robot.stopRobot();
    }

    private void encoderDrive(double speed, double leftInches, double rightInches) {
        int leftTarget = (int)(leftInches * ZoidbergHardware.COUNTS_PER_INCH);
        int rightTarget = (int)(rightInches * ZoidbergHardware.COUNTS_PER_INCH);

        // set the target position for each motor
        robot.getFrontLeftDrive().setTargetPosition(leftTarget);
        robot.getFrontRightDrive().setTargetPosition(-rightTarget);
        robot.getBackRightDrive().setTargetPosition(-rightTarget);
        robot.getBackLeftDrive().setTargetPosition(leftTarget);

        // set RUN_TO_POSITION for each motor
        robot.getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getFrontRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getBackRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getBackLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // set the power for the left drive motors
        robot.getFrontLeftDrive().setPower(speed);
        robot.getBackLeftDrive().setPower(speed);

        // set the power for the right drive motors
        robot.getFrontRightDrive().setPower(speed);
        robot.getBackRightDrive().setPower(speed);

        while(opModeIsActive() && robot.areDriveMotorsBusy()) {
            idle();
        }

        robot.stopRobot();

        robot.resetDriveEncoders();

        // set RUN_WITHOUT_ENCODER for each motor
        robot.getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.getFrontRightDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.getBackRightDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.getBackLeftDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void encoderStrafe(double speed, double frontInches, double backInches) {
        int frontTarget = (int)(frontInches * ZoidbergHardware.COUNTS_PER_INCH);
        int backTarget = (int)(backInches * ZoidbergHardware.COUNTS_PER_INCH);

        // set the target position for each motor
        robot.getFrontLeftDrive().setTargetPosition(frontTarget);
        robot.getFrontRightDrive().setTargetPosition(frontTarget);

        robot.getBackRightDrive().setTargetPosition(-backTarget);
        robot.getBackLeftDrive().setTargetPosition(-backTarget);

        // set RUN_TO_POSITION for each motor
        robot.getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getFrontRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getBackRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getBackLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // set the power for the left drive motors
        robot.getFrontLeftDrive().setPower(speed);
        robot.getBackLeftDrive().setPower(speed);

        // set the power for the right drive motors
        robot.getFrontRightDrive().setPower(speed);
        robot.getBackRightDrive().setPower(speed);

        while(opModeIsActive() && robot.areDriveMotorsBusy()) {
            idle();
        }

        robot.stopRobot();

        robot.resetDriveEncoders();

        // set RUN_WITHOUT_ENCODER for each motor
        robot.getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.getFrontRightDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.getBackRightDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.getBackLeftDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
