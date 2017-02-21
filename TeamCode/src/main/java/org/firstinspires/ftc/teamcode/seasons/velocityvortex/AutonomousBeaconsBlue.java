package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 1/9/17.
 */
@Autonomous(name = "Blue Beacons Autonomous", group = "autonomous programs")
public class AutonomousBeaconsBlue extends LinearOpModeBase {

    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();
        // reset drive encoders
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // use encoders
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // print color sensor values
        telemetry.addData("Left color sensor", "red: %d, blue: %d",
                getColorSensor1().red(), getColorSensor1().blue());
        telemetry.addData("Right color sensor", "red: %d, blue: %d",
                getColorSensor2().red(), getColorSensor2().blue());

        // wait for initialization
        telemetry.addData("status","waiting");
        telemetry.update();
        waitForStart();

//        disableColorSensors();

        // reset gyro heading
        getGyroSensor().resetZAxisIntegrator();

        // set target position for initial diagonal drive motion
        getFrontRightDrive().setTargetPosition(-LinearOpModeBase.COUNTS_PER_INCH * 63);
        getBackLeftDrive().setTargetPosition(LinearOpModeBase.COUNTS_PER_INCH * 63);

        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // set the run mode for the other two motors
        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getBackRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        getFrontRightDrive().setPower(0.5);
        getBackLeftDrive().setPower(0.5);

        // wait for the drive motors to stop
        while(opModeIsActive()
                && getFrontRightDrive().isBusy()
                && getBackLeftDrive().isBusy()) {

            // run the other motors to drive at a steeper angle
            getFrontLeftDrive().setPower(-0.2);
            getBackRightDrive().setPower(0.2);

            telemetry.addData("Path",  "Running at %d :%d",
                    getFrontRightDrive().getCurrentPosition(),
                    getBackLeftDrive().getCurrentPosition());

            telemetry.addData("front left target", getFrontRightDrive().getTargetPosition());
            telemetry.addData("back right target", getBackLeftDrive().getTargetPosition());
            telemetry.update();
            idle();
        }
        stopRobot();

        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // drive left to white line
        while(opModeIsActive() && getOds3().getRawLightDetected() < 1.5) {
            driveRight(0.1);

            telemetry.addData("ods3", getOds3().getRawLightDetected());
            telemetry.update();
        }
        stopRobot();

        // reset again before pressing beacon
        gyroPivot(0.8, 0, false);

        // claim the first beacon
        claimBeaconBlue();

        // reset again after pressing beacon
        gyroPivot(0.8, 0, false);

        // drive backward to get closer to center vortex
        rangeSensorDrive(20, 0.2);

        // launch the first particle
        launchParticle();

//        // open intake door
//        getDoor3().setPosition(0.25);
//
//        // run the intake
//        getRobotRuntime().reset();
//        while(opModeIsActive() && getRobotRuntime().milliseconds() < 500) {
//            getIntakeMotor().setPower(-1);
//        }
//        getIntakeMotor().setPower(0);
//
//        // launch the second particle
//        launchParticle();

        // strafe left
        encoderStrafe(0.5, -5, -5);

        // back up from wall
        //rangeSensorDrive(15, 0.2);

        rangeSensorStrafe(-0.2);

        // gyro pivot
        gyroPivot(0.8, 0, false);

        // strafe past the second beacon line
        //encoderStrafe(0.4, 16, 16);

        // look for the white line leading to the second beacon
        while(opModeIsActive() && getOds3().getRawLightDetected() < 1.5) {
            driveRight(0.1);
            telemetry.addData("ods3", getOds3().getRawLightDetected());
            telemetry.update();
        }
        stopRobot();

        // reset before pressing beacon
        gyroPivot(0.8, 0, false);

        // claim the second beacon
        claimBeaconBlue();

        // pivot to eighty degrees
        gyroPivot(0.8, -80, false);

        // reset the encoders
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // set target position for initial diagonal drive motion
        getBackRightDrive().setTargetPosition(-LinearOpModeBase.COUNTS_PER_INCH * 55);
        getFrontLeftDrive().setTargetPosition(LinearOpModeBase.COUNTS_PER_INCH * 55);

        getBackRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        getBackRightDrive().setPower(0.5);
        getFrontLeftDrive().setPower(0.5);

        // wait for the drive motors to stop
        while(opModeIsActive()
                && getBackRightDrive().isBusy()
                && getFrontLeftDrive().isBusy()) {

            telemetry.addData("Path",  "Running at %d :%d",
                    getBackRightDrive().getCurrentPosition(),
                    getFrontLeftDrive().getCurrentPosition());

            telemetry.addData("front left target", getBackRightDrive().getTargetPosition());
            telemetry.addData("back right target", getFrontLeftDrive().getTargetPosition());
            telemetry.update();
            idle();
        }

        // pivot to ninety again
        //gyroPivot(0.8, 80);

        // drive right a foot
        encoderStrafe(1.0, 12, 12);
    }
}
