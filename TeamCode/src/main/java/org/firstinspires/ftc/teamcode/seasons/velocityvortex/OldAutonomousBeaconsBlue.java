package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 1/9/17.
 */
@Disabled
@Autonomous(name = "Blue Beacons Autonomous", group = "autonomous programs")
public class OldAutonomousBeaconsBlue extends LinearOpModeBase {

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
        telemetry.update();

        // enable LEDs for color sensors
        getColorSensor1().enableLed(true);
        getColorSensor2().enableLed(true);

        // wait for initialization
        waitForStart();

        // set target position for initial diagonal drive motion
        getFrontRightDrive().setTargetPosition(LinearOpModeBase.COUNTS_PER_INCH * 83);
        getBackLeftDrive().setTargetPosition(-LinearOpModeBase.COUNTS_PER_INCH * 83);

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

        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // drive left to white line
        while(opModeIsActive() && getLeftOds().getRawLightDetected() < 1.5) {
            driveLeft(0.1);

            telemetry.addData("ods3", getLeftOds().getRawLightDetected());
            telemetry.update();
        }
        stopRobot();

        // reset again before pressing beacon
        gyroPivot(0.8, 0, false);

        // claim the first beacon
        claimBeaconRed();

        // reset again after pressing beacon
        gyroPivot(0.8, 0, false);

        // launch the first particle
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

        // strafe right
        encoderDrive(0.5, 24, RobotDirection.RIGHT);

        // back up from wall
        rangeSensorDrive(15, 0.2);

        // gyro pivot
        gyroPivot(0.8, 0, false);

        // strafe past the second beacon
        encoderDrive(0.4, 20, RobotDirection.RIGHT);

        // reset again after pressing beacon
        gyroPivot(0.8, 0, false);

        // look for the white line leading to the second beacon
        while(opModeIsActive() && getLeftOds().getRawLightDetected() < 1.5) {
            driveLeft(0.2);
            telemetry.addData("ods3", getLeftOds().getRawLightDetected());
            telemetry.update();
        }
        stopRobot();

        // claim the second beacon
        claimBeaconRed();

        // pivot to ninety degrees
        gyroPivot(1.0, 90, false);

        // reset the encoders
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // set target position for initial diagonal drive motion
        getFrontRightDrive().setTargetPosition(-LinearOpModeBase.COUNTS_PER_INCH * 48);
        getBackLeftDrive().setTargetPosition(LinearOpModeBase.COUNTS_PER_INCH * 48);

        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        getFrontRightDrive().setPower(0.5);
        getBackLeftDrive().setPower(0.5);

        // wait for the drive motors to stop
        while(opModeIsActive()
                && getFrontLeftDrive().isBusy()
                && getBackRightDrive().isBusy()) {

            telemetry.addData("Path",  "Running at %d :%d",
                    getFrontRightDrive().getCurrentPosition(),
                    getBackLeftDrive().getCurrentPosition());

            telemetry.addData("front left target", getFrontRightDrive().getTargetPosition());
            telemetry.addData("back right target", getBackLeftDrive().getTargetPosition());
            telemetry.update();
            idle();
        }

        // pivot to ninety again
        gyroPivot(0.8, 0, false);

        // drive back a foot
        encoderDrive(1.0, 12, RobotDirection.BACKWARD);
    }
}
