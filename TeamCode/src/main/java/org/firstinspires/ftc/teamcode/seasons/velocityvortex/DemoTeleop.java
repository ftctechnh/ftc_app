package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Random;

/**
 * Created by ftc6347 on 10/16/16.
 */
@TeleOp(name = "DEMO TELEOP", group = "tele-op")
public class DemoTeleop extends LinearOpModeBase {

    private static final float JOYSTICK_DEADZONE = 0.2f;

    private double frontLeftPower;
    private double frontRightPower;
    private double backLeftPower;
    private double backRightPower;

    private double motorPowerMultiplier = 0.5;

    private boolean driveReversed = false;

    private ElapsedTime timer;
    private Random random;

    private double randomTime;
    boolean powerUp;

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

        timer = new ElapsedTime();
        random = new Random();

        powerUp = false;

        // wait 15 to 30 seconds before receiving first power up
        randomTime = generateRandomInt(15, 30);

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

            if(timer.seconds() > randomTime) {
                if(powerUp) {
                    motorPowerMultiplier = 0.5;
                    powerUp = false;

                    // wait 20 to 30 seconds for next power up
                    randomTime = generateRandomInt(20, 30);
                } else {
                    motorPowerMultiplier = 1.0;
                    powerUp = true;

                    // allow power up to last for 10 to 20 seconds
                    randomTime = generateRandomInt(10, 20);
                }
                timer.reset();
            }

            // set the actual motor powers
            getFrontLeftDrive().setPower(frontLeftPower * motorPowerMultiplier);
            getFrontRightDrive().setPower(frontRightPower * motorPowerMultiplier);
            getBackLeftDrive().setPower(backLeftPower * motorPowerMultiplier);
            getBackRightDrive().setPower(backRightPower * motorPowerMultiplier);

            handleIntake();
            handleTelemetry();

            idle();
        }
    }

    private int generateRandomInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    private void handleIntake() {
        if (gamepad2.b){
            getDoor3().setPosition(0.25);
            getIntakeMotor().setPower(-1.0);
        }
        else if(gamepad2.right_stick_y >= 0.2 || gamepad2.right_stick_y <= -0.2 ){
            getDoor3().setPosition(0.55);
            getIntakeMotor().setPower(gamepad2.right_stick_y);
        }
        else {
            if(!gamepad2.a)
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

        if(powerUp) {
            telemetry.addData(">",
                    "Your power up will last " + (int)(randomTime - timer.seconds()) + " seconds!");
        }

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
