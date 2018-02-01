package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 2/24/17.
 */
@Disabled
@Autonomous(name = "gyro bug", group = "tests")
public class GyroBug extends LinearOpModeBase {

    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();

        waitForStart();

        getGyroSensor().resetZAxisIntegrator();

//        // set target position for initial diagonal drive motion
//        getFrontRightDrive().setTargetPosition(-LinearOpModeBase.COUNTS_PER_INCH * 63);
//        getBackLeftDrive().setTargetPosition(LinearOpModeBase.COUNTS_PER_INCH * 63);
//
//        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        // set the run mode for the other two motors
//        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        getBackRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        getFrontRightDrive().setPower(0.5);
//        getBackLeftDrive().setPower(0.5);
//
//        // wait for the drive motors to stop
//        while(opModeIsActive()
//                && getFrontRightDrive().isDriveTrainBusy()
//                && getBackLeftDrive().isDriveTrainBusy()) {
//
//            // run the other motors to drive at a steeper angle
//            getFrontLeftDrive().setPower(-0.2);
//            getBackRightDrive().setPower(0.2);
//
//            telemetry.addData("Path",  "Running at %d :%d",
//                    getFrontRightDrive().getCurrentPosition(),
//                    getBackLeftDrive().getCurrentPosition());
//
//            telemetry.addData("front left target", getFrontRightDrive().getTargetPosition());
//            telemetry.addData("back right target", getBackLeftDrive().getTargetPosition());
//            telemetry.update();
//            idle();
//        }
//        stopRobot();

        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // drive left to white line
        while(opModeIsActive() && getLeftOds().getRawLightDetected() < 1.5) {
            driveRight(0.1);

            telemetry.addData("ods3", getLeftOds().getRawLightDetected());
            telemetry.update();
        }
        stopRobot();

        // reset again before pressing beacon
        gyroPivot(0.8, 0, false);

        // claim the first beacon
        claimBeaconRed();
    }
}
