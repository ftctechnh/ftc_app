package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 1/9/17.
 */
@Autonomous(name = "Beacons 1 Red", group = "beacons")
public class AutonomousBeaconsRed extends LinearOpModeBase {

    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();
        // reset drive encoders
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // use encoders
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        autonomousInitLoop();

        // set target position for initial diagonal drive motion
        getFrontLeftDrive().setTargetPosition(LinearOpModeBase.COUNTS_PER_INCH * 63);
        getBackRightDrive().setTargetPosition(-LinearOpModeBase.COUNTS_PER_INCH * 63);

        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getBackRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // run the other motors to drive at a steeper angle
        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        getFrontLeftDrive().setPower(0.5);
        getBackRightDrive().setPower(0.5);

        // wait for the drive motors to stop
        while(opModeIsActive() &&
                (getFrontLeftDrive().isBusy() && getBackRightDrive().isBusy())) {

            // run the other motors to drive at a steeper angle
            getFrontRightDrive().setPower(0.2);
            getBackLeftDrive().setPower(-0.2);

            telemetry.addData("Path",  "Running at %d :%d",
                    getFrontLeftDrive().getCurrentPosition(),
                    getBackRightDrive().getCurrentPosition());

            telemetry.addData("front left target", getFrontLeftDrive().getTargetPosition());
            telemetry.addData("back right target", getBackRightDrive().getTargetPosition());
            telemetry.update();
            idle();
        }
        stopRobot();

        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // drive left to white line
        stopOnLine(0.05, false);

        // reset again before pressing beacon
        gyroPivot(0.8, 0, false);

        // claim the first beacon
        claimBeaconRed();

        // reset again after pressing beacon
        gyroPivot(0.8, 0, false);

        // drive backward to get closer to center vortex
        rangeSensorDrive(20, 0.2);

        // launch the first particle
        launchParticle();

        // open intake door
//        getDoor3().setPosition(0.25);

//        // run the intake
//        getRobotRuntime().reset();
//        while(opModeIsActive() && getRobotRuntime().milliseconds() < 500) {
//            getIntakeMotor().setPower(-1);
//        }
//        getIntakeMotor().setPower(0);
//
//        // launch the second particle
//        launchParticle();

        // strafe right to skip first line
        encoderStrafe(0.5, 5, 5);

        // back up from wall
        //rangeSensorDrive(15, 0.2);
        rangeGyroStrafe(1.0, 0, 6, 16, 16);

        // gyro pivot
        //gyroPivot(0.8, 0, false);

        // strafe past the second beacon line
        //encoderStrafe(0.4, 16, 16);

        // look for the white line leading to the second beacon
        //stopOnLine(0.05, false);

        // reset before pressing beacon
        gyroPivot(0.8, 0, false);

        // claim the second beacon
        claimBeaconRed();

        // pivot to ninety degrees
        gyroPivot(0.8, 80, false);

        // reset the encoders
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // set target position for initial diagonal drive motion
        getFrontRightDrive().setTargetPosition(-LinearOpModeBase.COUNTS_PER_INCH * 55);
        getBackLeftDrive().setTargetPosition(LinearOpModeBase.COUNTS_PER_INCH * 55);

        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        getFrontRightDrive().setPower(0.5);
        getBackLeftDrive().setPower(0.5);

        // wait for the drive motors to stop
        while(opModeIsActive() &&
                (getFrontRightDrive().isBusy() || getBackLeftDrive().isBusy())) {

            telemetry.addData("Path",  "Running at %d :%d",
                    getFrontRightDrive().getCurrentPosition(),
                    getBackLeftDrive().getCurrentPosition());

            telemetry.addData("front left target", getFrontRightDrive().getTargetPosition());
            telemetry.addData("back right target", getBackLeftDrive().getTargetPosition());
            telemetry.update();
            idle();
        }

        // pivot to eighty again
        //gyroPivot(0.8, 80);

        // drive left a foot
        encoderStrafe(1.0, -12, -12);
    }
}
