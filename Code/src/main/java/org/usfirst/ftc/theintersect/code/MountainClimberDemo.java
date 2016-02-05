package org.usfirst.ftc.theintersect.code;


import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.robocol.Telemetry;
import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.TelemetryDashboardAndLog;

/**
 * An Autonomous for both teams using the LinearOpMode
 */

@org.swerverobotics.library.interfaces.Autonomous(name = "MountainClimberDemo")
public class MountainClimberDemo extends SynchronousOpMode {

    static String team = "8865";
    static int delay = 0;
    static long endTime;

    static DcMotor rightWheel;
    static DcMotor leftWheel;

    static DcMotor sweeper;

    static Servo tubeTilt;
    static Servo tubeExtender;

    static Servo mountainClimber;
    static Servo mountainClimberRelease;

    static Servo bumper;

    static ColorSensor lineColor;
    static ModernRoboticsI2cGyro gyro;
    static UltrasonicSensor ultrasonicLeft;
    static UltrasonicSensor ultrasonicRight;

    Thread mountainClimberMove;

    @Override
    public void main() throws InterruptedException {

        // initialization
        rightWheel = hardwareMap.dcMotor.get("rightWheel");
        leftWheel = hardwareMap.dcMotor.get("leftWheel");

        sweeper = hardwareMap.dcMotor.get("sweeper");
        lineColor = hardwareMap.colorSensor.get("lineColor");
        gyro = (ModernRoboticsI2cGyro) unthunkedHardwareMap.gyroSensor.get("gyro");

        ultrasonicLeft = hardwareMap.ultrasonicSensor.get("ultrasonicLeft");
//        ultrasonicRight = hardwareMap.ultrasonicSensor.get("ultrasonicRight");

        tubeTilt = hardwareMap.servo.get("tubeTilt");
        tubeExtender = hardwareMap.servo.get("tubeExtender");
        mountainClimber = hardwareMap.servo.get("mountainClimber");
        mountainClimberRelease = hardwareMap.servo.get("mountainClimberRelease");
        bumper = hardwareMap.servo.get("bumper");

        autonomousInit(telemetry);
        telemetry.clearDashboard();

        waitForStart();


        while (opModeIsActive() && !isStopRequested() ) {

            double leftDistance;
            double armPosition;


            leftDistance = getUSDistance(ultrasonicLeft, telemetry);
            // insetPosition, 0: flat backward, 0.45: straight up, 0.9: flat forward
            armPosition = calculateArmPosition(leftDistance, telemetry);
            mountainClimber.setPosition(armPosition);
            telemetry.updateNow();
        }
    }

    //
    // Calculate the position for servo
    // Update basketOffset based on the field setup
    // Set armLength based on robot construction
    //
    public static double calculateArmPosition(double dist, TelemetryDashboardAndLog telemetry) {
        final double basketOffset = 5.0; // offset between wall and basket
        final double armLength = 40.5;   // Length of the mountain climber

        double armDegree;
        double armPosition;

        // Set min and Max distance for angular calculation
        // the distance is the distance of the wall, it needs to be adjusted for
        dist = dist + basketOffset;
        if (dist < 5) { // too close
            dist = 5.0;
        } else if (dist > ( armLength + 10 ) ) { // too far
            dist = 0.0;
        } else if ( dist > armLength ) {
            dist = armLength;
        }
        armDegree = Math.toDegrees(Math.acos(dist / armLength));
        armPosition = (180 - armDegree)/200;
        telemetry.addData("Degree", armDegree);
        return armPosition;
    }

    // Read ultrasound sensor distance 5 times and remove min/max readings
    public static double getUSDistance( UltrasonicSensor ultrasonicLeft, TelemetryDashboardAndLog telemetry ){
        double minDistance = 1000;
        double maxDistance = 0;
        double leftDistance = 0;
        for ( int i = 0; i < 5; i++ ) {
            double dist = ultrasonicLeft.getUltrasonicLevel();
            leftDistance += dist;
            if (dist > maxDistance) {
                maxDistance = dist;
            } else if (dist < minDistance) {
                minDistance = dist;
            }
            Functions.waitFor(50);
        }
        leftDistance = (leftDistance- minDistance - maxDistance ) / 3;
        telemetry.addData("min ", minDistance);
        telemetry.addData("max ", maxDistance);
        telemetry.addData("Distance ", leftDistance);

        return(leftDistance);
    }

    public static void directionInit() {
        rightWheel.setDirection(DcMotor.Direction.FORWARD);
        leftWheel.setDirection(DcMotor.Direction.REVERSE);
        mountainClimber.setDirection(Servo.Direction.FORWARD);
        mountainClimberRelease.setDirection(Servo.Direction.REVERSE);
        bumper.setDirection(Servo.Direction.FORWARD);
        tubeExtender.setDirection(Servo.Direction.REVERSE);
        tubeTilt.setDirection(Servo.Direction.REVERSE);

        rightWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

    }


    public static void gyroInit(TelemetryDashboardAndLog telemetry) {

        gyro.calibrate();
        gyro.setHeadingMode(
                ModernRoboticsI2cGyro.HeadingMode.HEADING_CARTESIAN);
        while (gyro.isCalibrating()) {
            telemetry.addData("gyroInit: ", "Calibrating Gyro");
            telemetry.updateNow();
        }
        telemetry.addData("gyroInit: ", "Calibration Complete");
        telemetry.updateNow();
        initGyroHeading(telemetry);
    }

    // setup gyro so it will have stable reading of heading
    public static void initGyroHeading(TelemetryDashboardAndLog telemetry) {
        int repeatCount = 0;
        int currentPosition;
        int previousPosition;
        int totalCount = 0;

        previousPosition = gyro.getIntegratedZValue();
        Functions.waitFor(400);
        while (repeatCount < 5) {
            currentPosition = gyro.getIntegratedZValue();
            if (currentPosition == previousPosition) {
                repeatCount++;
                telemetry.addData("initGyroHeading CurrentHeading", currentPosition);
                telemetry.updateNow();
            } else {
                previousPosition = currentPosition;
                repeatCount = 0;
            }
            Functions.waitFor(400);
            totalCount++;
        }
        telemetry.addData("initGyroHeading: Done ", totalCount);
        telemetry.updateNow();
    }

    public static void autonomousInit(TelemetryDashboardAndLog telemetry) {
        lineColor.enableLed(true);
//        servoInit();
        directionInit();
        resetEncoders();
        rightWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
//        gyroInit(telemetry);
    }

    public static void servoInit() {
        mountainClimber.setPosition(Functions.mountainClimberInitPosition);
        mountainClimberRelease.setPosition(
                Functions.mountainClimberReleaseClose);
        tubeExtender.setPosition(Functions.tubeExtenderInitPosition);
        tubeTilt.setPosition(Functions.tubeTiltInitPosition);
        bumper.setPosition(Functions.bumperInitPosition);
    }

    public static void resetEncoders() {
        rightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        leftWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        while (rightWheel.getTargetPosition() != 0) {
            Functions.waitFor(50);
        }

        while (leftWheel.getTargetPosition() != 0) {
            Functions.waitFor(50);
        }
    }
}