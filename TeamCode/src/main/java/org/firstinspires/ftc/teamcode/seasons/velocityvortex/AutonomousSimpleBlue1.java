package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 3/12/17.
 */
@Autonomous(name = "Simple 1 BLUE", group = "2 simple")
public class AutonomousSimpleBlue1 extends LinearOpModeBase {
    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();
        // reset drive encoders
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        autonomousInitLoop();

        // drive backward (since the robot is facing backward)
        encoderDrive(0.25, -14, -14);

        gyroPivot(0.8, 45, true);

        //drive backward to align before launch
        encoderDrive(0.25, -3 , -3);

        // launch the first (loaded) particle
        launchParticle();

        // open intake door
        getDoor3().setPosition(0.25);

        // run the intake
        getRobotRuntime().reset();
        while(opModeIsActive() && getRobotRuntime().milliseconds() < 750) {
            getIntakeMotor().setPower(-1);
        }
        getIntakeMotor().setPower(0);

        // launch the second particle
        launchParticle();

        // set target position for initial diagonal drive motion
        getFrontLeftDrive().setTargetPosition(-LinearOpModeBase.COUNTS_PER_INCH * 50);
        getBackRightDrive().setTargetPosition(LinearOpModeBase.COUNTS_PER_INCH * 50);

        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getBackRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        getFrontLeftDrive().setPower(0.5);
        getBackRightDrive().setPower(0.5);

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
        stopRobot();

        gyroPivot(0.5, 45, true);

        // set target position for initial diagonal drive motion
        getFrontRightDrive().setTargetPosition(LinearOpModeBase.COUNTS_PER_INCH * 50);
        getBackLeftDrive().setTargetPosition(-LinearOpModeBase.COUNTS_PER_INCH * 50);

        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        getFrontRightDrive().setPower(0.5);
        getBackLeftDrive().setPower(0.5);

        // wait for the drive motors to stop
        while(opModeIsActive() &&
                (getFrontRightDrive().isBusy() && getBackLeftDrive().isBusy())) {

            telemetry.addData("Path",  "Running at %d :%d",
                    getFrontRightDrive().getCurrentPosition(),
                    getBackLeftDrive().getCurrentPosition());

            telemetry.addData("front left target", getFrontRightDrive().getTargetPosition());
            telemetry.addData("back right target", getBackLeftDrive().getTargetPosition());
            telemetry.update();
            idle();
        }

        gyroPivot(0.5, 5, true);

        // set target position for initial diagonal drive motion
        getFrontLeftDrive().setTargetPosition(LinearOpModeBase.COUNTS_PER_INCH * 24);
        getBackRightDrive().setTargetPosition(-LinearOpModeBase.COUNTS_PER_INCH * 24);

        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getBackRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        getFrontLeftDrive().setPower(0.5);
        getBackRightDrive().setPower(0.5);

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

        encoderStrafe(0.5, 20, 20);
    }
}
