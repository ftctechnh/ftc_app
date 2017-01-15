package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 1/9/17.
 */
@Autonomous(name = "Blue Beacons Autonomous", group = "autonomous programs")
public class AutonomousBeaconsBlue extends LinearOpModeBase {

    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();
        resetDriveEncoders();

        // use encoders
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getBackRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("color sensor red", getColorSensor().red());
        telemetry.addData("color sensor blue", getColorSensor().blue());
        telemetry.update();

        // wait for initialization
        waitForStart();

        // set target position for initial diagonal drive motion
        getFrontRightDrive().setTargetPosition(-LinearOpModeBase.COUNTS_PER_INCH * 79);
        getBackLeftDrive().setTargetPosition(LinearOpModeBase.COUNTS_PER_INCH * 79);

        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        getFrontRightDrive().setPower(0.5);
        getBackLeftDrive().setPower(0.5);

        // wait for the drive motors to stop
        while(opModeIsActive()
                && getFrontRightDrive().isBusy()
                && getBackLeftDrive().isBusy()) {

            telemetry.addData("Path",  "Running at %d :%d",
                    getFrontRightDrive().getCurrentPosition(),
                    getBackLeftDrive().getCurrentPosition());

            telemetry.addData("front left target", getFrontRightDrive().getTargetPosition());
            telemetry.addData("back right target", getBackLeftDrive().getTargetPosition());
            telemetry.update();
            idle();
        }

        // wait for 0.25 seconds and reset drive encoders
        getRobotRuntime().reset();
        while (opModeIsActive() && getRobotRuntime().milliseconds() < 250) {
            resetDriveEncoders();
            idle();
        }

        // run using encoders again
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getBackRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // drive right to white line
        while(opModeIsActive() && getOds3().getRawLightDetected() < 1.5) {
            driveRight(0.2);
            telemetry.addData("ods3", getOds3().getRawLightDetected());
            telemetry.update();
        }

        resetDriveEncoders();
        stopRobot();

        // claim first beacon
        claimBeacon();

        // strafe to the left to second beacon
        encoderStrafe(0.5, -24, -24);

        // line up against wall
        encoderDrive(0.6, 6, 6);

        while(opModeIsActive() && getFrontRange().cmUltrasonic() < 8) {
            driveBackward(0.2);
        }
        stopRobot();

        // pause for 0.25 seconds
        getRobotRuntime().reset();
        while (opModeIsActive() && getRobotRuntime().milliseconds() < 250) {
            resetDriveEncoders();
            idle();
        }

        // run using encoders again
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getBackRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // look for the white line leading to the second beacon
        while(opModeIsActive() && getOds3().getRawLightDetected() < 1.5) {
            driveLeft(0.2);
            telemetry.addData("ods3", getOds3().getRawLightDetected());
            telemetry.update();
        }

        resetDriveEncoders();
        stopRobot();

        // claim the first beacon
        claimBeacon();

        // turn right for launching
        encoderDrive(0.5, 7, -7);

        // drive backward for launching
        encoderDrive(0.5, -12, -12);

        // launch the first (loaded) particle
        launchParticle();

        // open intake door
        getDoor3().setPosition(0.25);

        // run the intake
        getRobotRuntime().reset();
        while(opModeIsActive() && getRobotRuntime().milliseconds() < 500) {
            getIntakeMotor().setPower(-1);
        }
        getIntakeMotor().setPower(0);

        // launch the second particle
        launchParticle();
    }
}
