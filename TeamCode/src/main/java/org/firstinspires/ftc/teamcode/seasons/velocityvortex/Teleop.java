package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by ftc6347 on 10/16/16.
 */
@TeleOp(name = "TELEOP", group = "tele-op")
public class Teleop extends LinearOpModeBase {

    private static final float JOYSTICK_DEADZONE = 0.2f;

    private float frontLeftPower;
    private float frontRightPower;
    private float backLeftPower;
    private float backRightPower;

    private volatile boolean yButtonPressed = false;
    private boolean driveReversed = false;

    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();

        // run without encoders
        setDriveMotorsMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        gamepad1.setJoystickDeadzone(JOYSTICK_DEADZONE);
        gamepad2.setJoystickDeadzone(JOYSTICK_DEADZONE);

        Thread launcherThread = new Thread() {
            @Override
            public void run() {
                while(opModeIsActive()) {
                    if(gamepad2.y) {
                        launchParticle();
                    }
                }
            }
        };

        waitForStart();

        // start the launcher thread after op-mode is started
        launcherThread.start();

        while(opModeIsActive()) {

            // reset variables
            frontLeftPower = 0;
            frontRightPower = 0;
            backLeftPower = 0;
            backRightPower = 0;

            handleStrafe();
            handlePivot();

            // right trigger divides the speed of every drive motor by four
            if(gamepad1.right_trigger > 0) {
                frontLeftPower /= 4;
                frontRightPower /= 4;
                backLeftPower /= 4;
                backRightPower /= 4;
            }

            // set the actual motor powers
            getFrontLeftDrive().setPower(frontLeftPower);
            getFrontRightDrive().setPower(frontRightPower);
            getBackLeftDrive().setPower(backLeftPower);
            getBackRightDrive().setPower(backRightPower);

            handleIntake();
            handleCapBallMechanism();
            handleTelemetry();

            idle();
        }
    }

    private void handleCapBallMechanism() {
        double spoolMotorSpeed = -gamepad2.left_stick_y;
        getSpoolMotor1().setPower(spoolMotorSpeed);
        getSpoolMotor2().setPower(spoolMotorSpeed);

        if (gamepad1.left_trigger > 0) {
            // reverse all drive motors
            getBackLeftDrive().setDirection(DcMotor.Direction.FORWARD);
            getBackRightDrive().setDirection(DcMotor.Direction.FORWARD);
            getFrontLeftDrive().setDirection(DcMotor.Direction.FORWARD);
            getFrontRightDrive().setDirection(DcMotor.Direction.FORWARD);

            driveReversed = true;
        } else {
            // reverse all drive motors
            getBackLeftDrive().setDirection(DcMotor.Direction.REVERSE);
            getBackRightDrive().setDirection(DcMotor.Direction.REVERSE);
            getFrontLeftDrive().setDirection(DcMotor.Direction.REVERSE);
            getFrontRightDrive().setDirection(DcMotor.Direction.REVERSE);

            driveReversed = false;
        }

        // control to open the cap ball latch
        if(gamepad2.dpad_down) {
            getLatch4().setPosition(1.0);
        }

        // controls for the cap ball
        if(gamepad2.right_trigger > 0) {
            // move up
            getPusher5().setPosition(0.7);
        } else if(gamepad2.left_trigger > 0) {
            // move down
            getPusher5().setPosition(0);
        }
    }

    private void handleIntake() {
        if (gamepad2.b){
            getDoor3().setPosition(0.25);
            getIntakeMotor().setPower(-0.5);
        }
        else if(gamepad2.right_stick_y >= 0.2 || gamepad2.right_stick_y <= -0.2 ){
            getDoor3().setPosition(0.55);
            getIntakeMotor().setPower(gamepad2.right_stick_y);
        }
        else {
            getIntakeMotor().setPower(0);
            getDoor3().setPosition(0.25);
        }
    }

    private void handleTelemetry() {

        telemetry.addData("ods2: ", getDiskOds().getRawLightDetected());
        telemetry.addData("ods3", getOds3().getRawLightDetected());
        telemetry.addData("launcher ods", getLauncherOds().getLightDetected());
        telemetry.addData("front range", getFrontRange().cmUltrasonic());

        telemetry.addData("color sensor red", getColorSensor1().red());
        telemetry.addData("color sensor blue", getColorSensor1().blue());

        telemetry.addData("color sensor red2", getColorSensor2().red());
        telemetry.addData("color sensor blue2", getColorSensor2().blue());

        telemetry.update();
    }

    private void handlePivot() {
        double pivotPower = gamepad1.left_stick_x;

        if(!driveReversed) {
            pivotPower *= -1;
        }

        frontLeftPower += pivotPower;
        frontRightPower += pivotPower;
        backLeftPower += pivotPower;
        backRightPower += pivotPower;
    }

    private void handleStrafe() {
        frontLeftPower += gamepad1.right_stick_y - gamepad1.right_stick_x;
        frontRightPower += -gamepad1.right_stick_y - gamepad1.right_stick_x;
        backLeftPower += gamepad1.right_stick_y + gamepad1.right_stick_x;
        backRightPower += -gamepad1.right_stick_y + gamepad1.right_stick_x;
    }
}
