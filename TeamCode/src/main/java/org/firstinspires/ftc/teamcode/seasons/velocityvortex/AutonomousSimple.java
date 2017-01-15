package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by ftc6347 on 10/30/16.
 */
@Autonomous(name = "Simple autonomous", group = "autonomous")
public class AutonomousSimple extends LinearOpMode {

    private ZoidbergHardware robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new ZoidbergHardware(hardwareMap);

        robot.resetDriveEncoders();

        // use encoders
        robot.getBackLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getBackRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getFrontRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("color sensor red", robot.getColorSensor().red());
        telemetry.addData("color sensor blue", robot.getColorSensor().blue());
        telemetry.update();

        waitForStart();

        // drive backward (since the robot is facing backward)
        encoderDrive(0.5, -12, -12);

        // launch the first (loaded) particle
        robot.launchParticle();

        // open intake door
        robot.getDoor3().setPosition(0.25);

        // run the intake
        robot.getRuntime().reset();
        while(opModeIsActive() && robot.getRuntime().milliseconds() < 500) {
            robot.getIntakeMotor().setPower(-1);
        }
        robot.getIntakeMotor().setPower(0);

        // launch the second particle
        robot.launchParticle();
    }

    private void encoderDrive(double speed, double leftInches, double rightInches) {
        int leftTarget = (int)(leftInches * ZoidbergHardware.COUNTS_PER_INCH);
        int rightTarget = (int)(rightInches * ZoidbergHardware.COUNTS_PER_INCH);

        // set the target position for each motor
        robot.getFrontLeftDrive().setTargetPosition(leftTarget);
        robot.getFrontRightDrive().setTargetPosition(-rightTarget);
        robot.getBackRightDrive().setTargetPosition(-rightTarget);
        robot.getBackLeftDrive().setTargetPosition(leftTarget);

        // set RUN_TO_POSITION for each motor
        robot.getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getFrontRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getBackRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getBackLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // set the power for the left drive motors
        robot.getFrontLeftDrive().setPower(speed);
        robot.getBackLeftDrive().setPower(speed);

        // set the power for the right drive motors
        robot.getFrontRightDrive().setPower(speed);
        robot.getBackRightDrive().setPower(speed);

        while(opModeIsActive() && robot.areDriveMotorsBusy()) {
            idle();
        }

        robot.stopRobot();

        robot.resetDriveEncoders();

        // set RUN_WITHOUT_ENCODER for each motor
        robot.getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.getFrontRightDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.getBackRightDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.getBackLeftDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}