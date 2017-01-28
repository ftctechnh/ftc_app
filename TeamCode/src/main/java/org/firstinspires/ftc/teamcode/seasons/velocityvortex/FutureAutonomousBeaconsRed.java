package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 1/9/17.
 */
@Disabled
@Autonomous(name = "Red Beacons Autonomous", group = "autonomous programs")
public class FutureAutonomousBeaconsRed extends LinearOpModeBase {

    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();
        // reset drive encoders
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // use encoders
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("color sensor red", getColorSensor().red());
        telemetry.addData("color sensor blue", getColorSensor().blue());
        telemetry.update();

        // wait for initialization
        waitForStart();

        // set target position for initial diagonal drive motion
        getFrontLeftDrive().setTargetPosition(LinearOpModeBase.COUNTS_PER_INCH * 81);
        getBackRightDrive().setTargetPosition(-LinearOpModeBase.COUNTS_PER_INCH * 81);

        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getBackRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        getFrontLeftDrive().setPower(0.5);
        getBackRightDrive().setPower(0.5);

        // wait for the drive motors to stop
        while(opModeIsActive()
                && getFrontLeftDrive().isBusy()
                && getBackRightDrive().isBusy()) {

            telemetry.addData("Path",  "Running at %d :%d",
                    getFrontLeftDrive().getCurrentPosition(),
                    getBackRightDrive().getCurrentPosition());

            telemetry.addData("front left target", getFrontLeftDrive().getTargetPosition());
            telemetry.addData("back right target", getBackRightDrive().getTargetPosition());
            telemetry.update();
            idle();
        }


        getRobotRuntime().reset();
        while (opModeIsActive() && getRobotRuntime().milliseconds() < 250) {
            // reset drive encoders
            setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            idle();
        }

        // run using encoders again
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // use the gyro to get the robot square with the wall
        gyroPivot(0.5, 0);

        // look for the white line leading to the first beacon
        while(opModeIsActive() && getOds3().getRawLightDetected() < 1.5) {
            driveLeft(0.2);
            telemetry.addData("ods3", getOds3().getRawLightDetected());
            telemetry.update();
        }

        // reset drive encoders
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        stopRobot();

        // claim the first beacon
        claimBeaconRed();

        // strafe to the right to second beacon
        encoderStrafe(0.5, 24, 24);

        // line up against wall
        encoderDrive(0.6, 6, 6);

        while(opModeIsActive() && getFrontRange().cmUltrasonic() < 8) {
            driveBackward(0.2);
        }
        stopRobot();

        // pause for 0.25 seconds
        getRobotRuntime().reset();
        while (opModeIsActive() && getRobotRuntime().milliseconds() < 250) {
            // reset drive encoders
            setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            idle();
        }

        // run using encoders again
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // strafe past the white line
        encoderStrafe(0.5, 16, 16);

        // look for the white line leading to the second beacon
        while(opModeIsActive() && getOds3().getRawLightDetected() < 1.5) {
            driveLeft(0.2);
            telemetry.addData("ods3", getOds3().getRawLightDetected());
            telemetry.update();
        }

        // reset drive encoders
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        stopRobot();

        // claim the second beacon
        claimBeaconRed();

        // drive backward eight inches
        encoderDrive(0.5, -6, -6);

        // turn left for launching
        encoderDrive(0.5, -7, 7);

        // drive backward for launching
        encoderDrive(0.25, -14, -14);

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
