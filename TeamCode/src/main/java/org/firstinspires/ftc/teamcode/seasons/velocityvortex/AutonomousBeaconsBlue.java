package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 1/9/17.
 */
@Autonomous(name = "Beacons 1 BLUE", group = "1 beacons")
public class AutonomousBeaconsBlue extends LinearOpModeBase {

    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();
        // reset drive encoders
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // use encoders
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        autonomousInitLoop();

        // set target position for initial diagonal drive motion
        getFrontRightDrive().setTargetPosition(-LinearOpModeBase.COUNTS_PER_INCH * 58);
        getBackLeftDrive().setTargetPosition(LinearOpModeBase.COUNTS_PER_INCH * 58);

        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // run the other motors to drive at a steeper angle
        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        getFrontRightDrive().setPower(-0.5);
        getBackLeftDrive().setPower(0.5);

        // wait for the drive motors to stop
        while(opModeIsActive() &&
                (getFrontRightDrive().isBusy() && getBackLeftDrive().isBusy())) {

            // run the other motors to drive at a steeper angle
            getFrontLeftDrive().setPower(-0.1);
            getBackRightDrive().setPower(0.1);

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
        stopOnLine(0.05, false);

        // reset again before pressing beacon
        gyroPivot(0.8, 0, true);

        // claim the first beacon
        claimBeaconBlue();

        // back up from wall
        rangeSensorDrive(20, 0.2);

        rangeGyroStrafe(0, 20, -36, -36);

        // drive left to white line
        stopOnLine(0.05, false);

        // reset before pressing beacon
        gyroPivot(0.8, 0, true);

        // claim the second beacon
        claimBeaconBlue();

        // gyro pivot for shooting
        gyroPivot(0.8, -42, true);

        // drive backward for shooting
        encoderDrive(0.5, -10, -10);

        // launch the particle
        launchParticle();

        // pivot to eighty degrees
        gyroPivot(0.8, 85, true);

        // reset the encoders
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // set target position for initial diagonal drive motion
        getBackRightDrive().setTargetPosition(-LinearOpModeBase.COUNTS_PER_INCH * 50);
        getFrontLeftDrive().setTargetPosition(LinearOpModeBase.COUNTS_PER_INCH * 50);

        getBackRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        getBackRightDrive().setPower(0.5);
        getFrontLeftDrive().setPower(0.5);

        // wait for the drive motors to stop
        while(opModeIsActive() &&
                (getFrontLeftDrive().isBusy() && getBackRightDrive().isBusy())) {

            telemetry.addData("Path",  "Running at %d :%d",
                    getFrontLeftDrive().getCurrentPosition(),
                    getBackRightDrive().getCurrentPosition());

            telemetry.addData("front left target", getFrontLeftDrive().getTargetPosition());
            telemetry.addData("back right target", getBackRightDrive().getTargetPosition());
            telemetry.update();
            idle();
        }

        // drive right a foot
        encoderStrafe(1.0, 12, 12);
    }
}
