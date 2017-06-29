package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Nullbot: Calibrate compass", group="Calibration")
public class NullbotCalibrateCompass extends LinearOpMode {

    NullbotHardware robot   = new NullbotHardware();
    private ElapsedTime runtime = new ElapsedTime();

    final double MOTOR_POWER   = 0.4;
    final int HOLD_TIME_MS  = 3000;
    final int WARN_TIME_SEC = 1;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap, this, false);

        for (DcMotor m : robot.motorArr) {
            m.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        telemetry.clearAll();

        telemetry.log().add("Modern robotics compass sensor calibration");
        telemetry.log().add("--------------------------");
        telemetry.log().add("Robot will ROTATE for 20 seconds");
        telemetry.log().add("Place robot in a safe location, or hit STOP!");

        waitForStart();

        robot.compass.setMode(CompassSensor.CompassMode.CALIBRATION_MODE);

        telemetry.log().add("DO NOT TOUCH the robot while it is calibrating!");
        telemetry.update();

        runtime.reset();

        while (opModeIsActive() && runtime.time() < WARN_TIME_SEC) {
            // We warn for WARN_TIME_SEC seconds to prevent the robot from falling off
            // of a table

            telemetry.addData("Time until calibration", WARN_TIME_SEC - (int) runtime.time());
            telemetry.update();
            idle();
        }
        telemetry.clearAll();

        // Rotate clockwise
        robot.frontLeft.setPower(MOTOR_POWER);
        robot.backLeft.setPower(MOTOR_POWER);
        robot.frontRight.setPower(-MOTOR_POWER);
        robot.backRight.setPower(-MOTOR_POWER);

        runtime.reset();
        boolean atZero = true; // Robot starts with gyro at 0
        int rotations = 0; // Start counting rotations

        while (opModeIsActive() && rotations < 2) {
            int h = Math.abs(robot.gyro.getHeading());
            if (h > 5) {
                atZero = false;
            }
            if (!atZero && h <= 5) {
                rotations++;
                atZero = true;
            }

            telemetry
                    .addData("Calibration %", Math.min(100, (50 - (int) (h / 7.2)) + rotations*50));
            telemetry.addData("Rotations", rotations);

            telemetry.update();
            idle();
        }

        for (DcMotor m : robot.motorArr) {
            m.setPower(0);
        }

        robot.compass.setMode(CompassSensor.CompassMode.MEASUREMENT_MODE);

        sleep(HOLD_TIME_MS); // Give robot some time to change modes

        if (robot.compass.calibrationFailed())
            telemetry.log().add("Calibration failed. No settings have been changed.");
        else
            telemetry.log().add("Calibration successful");
        telemetry.update();

        sleep(HOLD_TIME_MS); // Give user time to read dialog
    }
}
