package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 10/16/16.
 */
@TeleOp(name = "TELEOP-single-user", group = "tele-op")
public class SingleUserTeleop extends LinearOpModeBase {

    private static final float JOYSTICK_DEADZONE = 0.2f;

    private float frontLeftPower;
    private float frontRightPower;
    private float backLeftPower;
    private float backRightPower;

    private boolean driveReversed = false;

    @Override
    public void runOpMode() throws InterruptedException {
        initializeHardware();

        // run without encoders
        setDriveMotorsMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        gamepad1.setJoystickDeadzone(JOYSTICK_DEADZONE);

        Thread launcherThread = new Thread() {
            @Override
            public void run() {
                while(opModeIsActive()) {
                    if(gamepad1.y) {
                        launchParticle();
                    } else if(gamepad1.right_trigger > 0.1) {
                        if(getLauncherChamberColorSensor().alpha()
                                > LAUNCHER_CHAMBER_COLOR_SENSOR_THRESHOLD) {
                            getIntakeMotor().setPower(0);

                            // run launcher motor for an entire rotation
                            getRobotRuntime().reset();
                            while(opModeIsActive() && getRobotRuntime().milliseconds() < 900) {
                                getLauncherMotor().setPower(1.0);
                            }
                            getLauncherMotor().setPower(0);

                            // run the intake before finding the black line
                            getIntakeMotor().setPower(-1.0);

                            // look for the black line
                            while(opModeIsActive() && getDiskOds().getRawLightDetected() > 1) {
                                getLauncherMotor().setPower(0.3);
                            }
                            getLauncherMotor().setPower(0);
                        } else {
                            // otherwise, run the intake
                            getIntakeMotor().setPower(-1.0);
                        }
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

            // control for button pusher servo motors
            /*if(gamepad1.right_bumper) {
                // move beacons servos out
                getBeaconsServo1().setPosition(0.6);
                getBeaconsServo2().setPosition(0.4);
            } else if(gamepad1.left_bumper) {
                // move beacons servos in
                getBeaconsServo1().setPosition(0);
                getBeaconsServo2().setPosition(1);
            }*/

            idle();
        }
    }

    private void handleCapBallMechanism() {
        double spoolMotorSpeed = gamepad2.left_stick_y;
        getSpoolMotor1().setPower(spoolMotorSpeed);
        getSpoolMotor2().setPower(spoolMotorSpeed);

        getBackLeftDrive().setDirection(DcMotor.Direction.FORWARD);
        getBackRightDrive().setDirection(DcMotor.Direction.FORWARD);
        getFrontLeftDrive().setDirection(DcMotor.Direction.FORWARD);
        getFrontRightDrive().setDirection(DcMotor.Direction.FORWARD);

        /*if (gamepad1.left_trigger < .5) {
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
        }*/




    }

    private void handleIntake() {
        if (gamepad1.left_trigger > 0.1){
            getDoor3().setPosition(0.25);
            getIntakeMotor().setPower(-1.0);
        }
        if (gamepad1.right_bumper){
            getIntakeMotor().setPower(1.0);
        }
        else {
            if(gamepad1.right_trigger < 0.1 && gamepad1.left_trigger < 0.1 && !gamepad1.right_bumper)
                getIntakeMotor().setPower(0);

            getDoor3().setPosition(0.25);
        }
    }

    private void handleTelemetry() {

        telemetry.addData("ods2: ", getDiskOds().getRawLightDetected());
        telemetry.addData("ods3", getLeftOds().getRawLightDetected());
        telemetry.addData("front range", getFrontRange().cmUltrasonic());

        telemetry.addData("color sensor red", getColorSensor1().red());
        telemetry.addData("color sensor blue", getColorSensor1().blue());

        telemetry.addData("color sensor red2", getColorSensor2().red());
        telemetry.addData("color sensor blue2", getColorSensor2().blue());

        telemetry.addData("launcher chamber cs", getLauncherChamberColorSensor().alpha());

        telemetry.addData("Touch sensor pressed", getTouchSensor().isPressed());

        telemetry.addData("left ods", getLeftOds().getRawLightDetected());
        telemetry.addData("right ods", getRightOds().getRawLightDetected());

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
