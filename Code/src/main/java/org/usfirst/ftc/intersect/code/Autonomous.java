package org.usfirst.ftc.intersect.code;

import com.qualcomm.hardware.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.robocol.Telemetry;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.TelemetryDashboardAndLog;
import org.swerverobotics.library.internal.I2cDeviceClient;
import org.swerverobotics.library.internal.II2cDevice;

/**
 * An Autonomous.
 */

@org.swerverobotics.library.interfaces.Autonomous(name = "Autonomous")
public class Autonomous extends SynchronousOpMode {
    //Declare hardware
    static DcMotor frontRightWheel;
    static DcMotor frontLeftWheel;
    static DcMotor backRightWheel;
    static DcMotor backLeftWheel;

    static DcMotor sweeper;

    static ColorSensor lineColor;
    static int white;
    static GyroSensor gyro;
    static UltrasonicSensor ultrasonic;

    @Override
    public void main() throws InterruptedException {
        //Initialize hardware
        frontRightWheel = hardwareMap.dcMotor.get("frontRightWheel");
        frontLeftWheel = hardwareMap.dcMotor.get("frontLeftWheel");
        backRightWheel = hardwareMap.dcMotor.get("backRightWheel");
        backLeftWheel = hardwareMap.dcMotor.get("backLeftWheel");


        sweeper = hardwareMap.dcMotor.get("sweeper");

        lineColor = hardwareMap.colorSensor.get("lineColor");
        gyro = hardwareMap.gyroSensor.get("gyro");
        ultrasonic = hardwareMap.ultrasonicSensor.get("ultrasonic");
        //Set motor directions
        frontRightWheel.setDirection(DcMotor.Direction.REVERSE);
        frontLeftWheel.setDirection(DcMotor.Direction.FORWARD);
        backRightWheel.setDirection(DcMotor.Direction.FORWARD);
        backLeftWheel.setDirection(DcMotor.Direction.REVERSE);

        //Calibrate the white
        telemetry.clearDashboard();
        white = lineColor.alpha()*2;
        telemetry.addData("White Line Calibration:", white);

        //Gyro Calibration
        gyro.calibrate();
        while (gyro.isCalibrating()) {
            telemetry.addData("Gyro Calibration", "Calibrating");
            telemetry.update();
        }
        telemetry.addData("Gyro Calibration", "Calibration Done");
        telemetry.update();

        //Autonomous Start
        waitForStart();
        while (opModeIsActive()) {
            telemetry.clearDashboard();
            double ultraVal = ultrasonic.getUltrasonicLevel();
            //stopAtWhite(1, 20000, telemetry);
            telemetry.addData("Ultrasonic", ultraVal);
            //telemetry.addData("Heading", gyro.getHeading());
            //telemetry.update();
            //turnRobotrightDegrees(180, 0.3, 100, telemetry);
            //telemetry.addData("Heading", gyro.getHeading());
            telemetry.update();
            telemetry.addData("Debug", "Done");
            telemetry.update();
        }
        //Autonomous End
    }

    //Functions
    public static void prepareMotors() {
        //Resets the encoders and forces the motors to run to the target position
        frontRightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontRightWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        frontLeftWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontLeftWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        backRightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backRightWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        backLeftWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backLeftWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    public static void moveRobotPower(double power) {
        frontRightWheel.setPower(power);
        frontLeftWheel.setPower(power);
        backRightWheel.setPower(power);
        backLeftWheel.setPower(power);

    }

    public static void moveRobotRotations(double rotations, double power, TelemetryDashboardAndLog telemetry) {
        prepareMotors();
        backRightWheel.setTargetPosition((int) (rotations));
        backLeftWheel.setTargetPosition((int) (rotations));
        backRightWheel.setPower(power);
        backLeftWheel.setPower(power);
        frontLeftWheel.setPower(power);
        frontRightWheel.setPower(power);

        while (!(backRightWheel.getCurrentPosition() >= backRightWheel.getTargetPosition() - Functions.encoderError && backRightWheel.getCurrentPosition() <= backRightWheel.getTargetPosition() + Functions.encoderError) && !(backLeftWheel.getCurrentPosition() >= backLeftWheel.getTargetPosition() - Functions.encoderError && backLeftWheel.getCurrentPosition() <= backLeftWheel.getTargetPosition() + Functions.encoderError)) {
            telemetry.addData("Right: ", backRightWheel.getCurrentPosition());
            telemetry.addData("Left: ", backLeftWheel.getCurrentPosition());
            telemetry.update();
        }
        moveRobotPower(0);
    }

    public static void moveRobotInches(double inches, double power, TelemetryDashboardAndLog telemetry) {
        moveRobotRotations(inches / Functions.backWheelCircumfrence, power, telemetry);
    }

    public static void stopAtWhite(double power, long timeout, TelemetryDashboardAndLog telemetry) {
        moveRobotPower(power);
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() <= endTime) {
            if (lineColor.alpha() >= white * 0.75 && lineColor.alpha() <= white * 1.25) {
                telemetry.update();
                moveRobotPower(0);
                break;
            }

            telemetry.addData("Line Color Alpha", lineColor.alpha());
            telemetry.addData("ARGB", lineColor.argb());
            telemetry.addData("Red", lineColor.red());
            telemetry.addData("Green", lineColor.green());
            telemetry.addData("Blue", lineColor.blue());
            telemetry.update();
        }
        moveRobotPower(0);
    }

    public static void turnRobotrightDegrees(int degrees, double power, int timeout, TelemetryDashboardAndLog telemetry) {
        int start = gyro.getHeading();
        int heading = start;
        long endtime = System.currentTimeMillis() + timeout * 1000;
        int protectedValue = start - 20;
        int target = start + degrees;
        if (target >= 360) {
            target = target - 360;
        }
        if (protectedValue < 0) {
            protectedValue = protectedValue + 360;
        }
        else if (target > start) {
            protectedValue = 360;
        }
        backLeftWheel.setPower(power);
        backRightWheel.setPower(-power);
        while ((heading < target) && (System.currentTimeMillis() < endtime)) {
            heading = gyro.getHeading();
            if (heading > protectedValue) {
                heading = heading - 360;
            }

            telemetry.addData("Start", start);
            telemetry.addData("Heading", gyro.getHeading());
            telemetry.addData("Target", target);
            telemetry.addData("Protect", protectedValue);
            telemetry.update();
        }
        backLeftWheel.setPower(0);
        backRightWheel.setPower(0);
        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("Protect", protectedValue);
        telemetry.update();

    }


}
