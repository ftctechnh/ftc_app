package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 4/21/17.
 */
@Autonomous(name = "Beacons 2 BLUE", group = "2 beacons")
public class AutonomousBeaconsBlue2 extends LinearOpModeBase {
    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();
        // reset drive encoders
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        autonomousInitLoop();

        // set target position for initial diagonal drive motion
        getFrontRightDrive().setTargetPosition(-LinearOpModeBase.COUNTS_PER_INCH * 62); // 68
        getBackLeftDrive().setTargetPosition(LinearOpModeBase.COUNTS_PER_INCH * 62); // 68

        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // run the other motors to drive at a steeper angle
        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        getBackRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        getFrontRightDrive().setPower(-0.5);
        getBackLeftDrive().setPower(0.5);

        // wait for the drive motors to stop
        while(opModeIsActive() &&
                (getFrontRightDrive().isBusy() && getBackLeftDrive().isBusy())) {

            // run the other motors to drive at a steeper angle
            getFrontLeftDrive().setPower(-0.12);
            getBackRightDrive().setPower(0.12);

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

        // reset before driving forward with touch sensor
        gyroPivot(0.8, 0, true);

        touchSensorDrive();

        // drive left to white line
        stopOnLine(0.1, RobotDirection.LEFT);

        // reset again before pressing beacon
        gyroPivot(0.8, 0, true);

        // claim the first beacon
        claimBeaconBlue();

        // back up from wall
        rangeSensorDrive(18, 0.2);

        rangeGyroStrafe(0, 20, 35, RobotDirection.LEFT);

//        rangeSensorDrive(15, 0.2);

        // reset before driving forward with touch sensor
        gyroPivot(0.8, 0, true);

        touchSensorDrive();

        // drive left to white line
        stopOnLine(0.1, RobotDirection.LEFT);

        // reset before pressing beacon
        gyroPivot(0.8, 0, true);

        // claim the second beacon
        claimBeaconBlue();

        // back up for launching
        rangeSensorDrive(20, 0.2);

        // gyro pivot for shooting
        gyroPivot(0.8, -46, true);

        // drive backward for shooting
        encoderDrive(0.5, 10, RobotDirection.BACKWARD);

        // open intake door
        getDoor3().setPosition(0.25);

        // launch the particle
        autoLaunchParticle();

        gyroPivot(0.8, 0, true);

        // wall-follow over to the corner vortex
        rangeGyroStrafe(0, 40, 45, RobotDirection.RIGHT);

        // gyro pivot to face the corner vortex
        gyroPivot(0.8, 45, true);

        // drive onto corner vortex
        encoderDrive(0.5, 18, RobotDirection.FORWARD);
    }
}
