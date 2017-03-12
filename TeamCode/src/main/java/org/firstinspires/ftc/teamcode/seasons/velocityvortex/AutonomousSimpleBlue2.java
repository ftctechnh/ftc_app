package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 10/30/16.
 */
@Autonomous(name = "Simple 2 BLUE", group = "3 simple")
public class AutonomousSimpleBlue2 extends LinearOpModeBase {

    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();
        // reset drive encoders
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // use encoders
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        autonomousInitLoop();

        // drive backward (since the robot is facing backward)
        encoderDrive(0.25, -10, -10);

        gyroPivot(0.8, 45, true);

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
        getFrontLeftDrive().setTargetPosition(-LinearOpModeBase.COUNTS_PER_INCH * 67);
        getBackRightDrive().setTargetPosition(LinearOpModeBase.COUNTS_PER_INCH * 67);

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

        // drive up ramp
        encoderStrafe(0.5, -12, -12);
    }
}