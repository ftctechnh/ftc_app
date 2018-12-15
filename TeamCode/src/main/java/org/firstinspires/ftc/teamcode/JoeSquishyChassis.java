package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Temperature;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

//@TeleOp(name="JoeSquishy Chassis", group="JoeSquishy")
public class JoeSquishyChassis extends OpMode {

    //constants from encoder sample
    static final double BACK_COUNTS_PER_MOTOR_REV = 560;    // http://www.revrobotics.com/content/docs/Encoder-Guide.pdf
    static final double BACK_DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double BACK_WHEEL_DIAMETER_INCHES = 7.75;     // For figuring circumference
    static final double BACK_COUNTS_PER_INCH = (BACK_COUNTS_PER_MOTOR_REV * BACK_DRIVE_GEAR_REDUCTION) /
            (BACK_WHEEL_DIAMETER_INCHES * Math.PI);
    static final double FRONT_COUNT_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double FRONT_DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double FRONT_WHEEL_DIAMETER_INCHES = 6.0625;     // For figuring circumference
    static final double FRONT_COUNTS_PER_INCH = (FRONT_COUNT_PER_MOTOR_REV * FRONT_DRIVE_GEAR_REDUCTION) /
            (FRONT_WHEEL_DIAMETER_INCHES * Math.PI);
    //static final double DRIVE_SPEED = 0.6;
    //static final double TURN_SPEED = 0.5;


    // Elapsed time since the opmode started.
    private ElapsedTime runtime = new ElapsedTime();

    // Internal Gyroscope in the Rev Hub.
    private BNO055IMU bosch;

    // Motors connected to the hub.
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;
    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;

    // Arm sub-system
    private DcMotor elbow;
    private DcMotor extender;
    private DcMotor tacvac;

    // Hack stuff.
    private boolean useGyroscope = false;
    private boolean useMotors = true;
    private boolean useArm = true;

    /**
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        // Initialize the gyoroscope.
        if (useGyroscope) {
            boolean boschInit = initGyroscope();
            telemetry.addData("Gyro", "bosch init=" + boschInit);
            telemetry.addData("Gyro", bosch.getCalibrationStatus().toString());
        }

        // Initialize the motors.
        if (useMotors) {
            motorBackLeft = hardwareMap.get(DcMotor.class, "motor0");
            motorBackRight = hardwareMap.get(DcMotor.class, "motor1");
            motorFrontLeft = hardwareMap.get(DcMotor.class, "motor2");
            motorFrontRight = hardwareMap.get(DcMotor.class, "motor3");

            // Most robots need the motor on one side to be reversed to drive forward
            // Reverse the motor that runs backwards when connected directly to the battery
            motorFrontLeft.setDirection(DcMotor.Direction.FORWARD);
            motorBackLeft.setDirection(DcMotor.Direction.FORWARD);
            motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
            motorBackRight.setDirection(DcMotor.Direction.REVERSE);

            motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        if (useArm) {
            elbow = hardwareMap.get(DcMotor.class, "motor7");
            extender = hardwareMap.get(DcMotor.class, "motor6");
            tacvac = hardwareMap.get(DcMotor.class, "motor4");

        }

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /**
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /**
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        // Reset the game timer.
        runtime.reset();
    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if (useGyroscope) {
            reportGyroscope();
        }

        if (useMotors) {
            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = -gamepad1.left_stick_y;
            if (Math.abs(drive) < 0.1)
                drive = 0.0; // Prevent the output from saying "-0.0".
            double turn = gamepad1.right_stick_x;
            double leftPower = Range.clip(drive + turn, -1.0, 1.0);
            double rightPower = Range.clip(drive - turn, -1.0, 1.0);
            motorFrontLeft.setPower(leftPower);
            motorBackLeft.setPower(leftPower);
            motorFrontRight.setPower(rightPower);
            motorBackRight.setPower(rightPower);
            telemetry.addData("Motors", "left:%.0f, right:%.0f, lpos:%d/%d, rpos=%d/%d",
                    leftPower, rightPower, motorFrontLeft.getCurrentPosition(), motorBackLeft.getCurrentPosition(),
                    motorFrontRight.getCurrentPosition(), motorBackRight.getCurrentPosition());
            telemetry.addData("Motors", "drive (%.2f), turn (%.2f)", drive, turn);
        }

        if (useArm) {
            // Control the elbow.
            boolean elbowOut = gamepad1.dpad_up;
            boolean elbowIn = gamepad1.dpad_down;
            double elbowPower = 0.0;
            if (elbowOut) {
                elbowPower = -1.0;
            } else if (elbowIn) {
                elbowPower = 1.0;
            }
            elbow.setPower(elbowPower);

            boolean extendOut = gamepad1.right_bumper;
            boolean extendIn = gamepad1.left_bumper;
            double extendPower = 0.0;
            if (extendOut) {
                extendPower = -1.0;
            } else if (extendIn) {
                extendPower = 1.0;
            }
            extender.setPower(extendPower);

            // Control the tacvac.
            float pullUp = gamepad1.right_trigger;
            float pullDown = gamepad1.left_trigger;
            double pullPower = 0.0;
            if ((pullUp > 0.0) && (pullDown == 0.0)) {
                pullPower = -1.0;
            } else if ((pullDown > 0.0) && (pullUp == 0.0)) {
                pullPower = 1.0;
            }
            tacvac.setPower(pullPower);

            // HACK: If driver presses the secret 'x' key, go forward 12 inches, to test encoder.
            boolean encodertest = gamepad1.x;
            if (encodertest) {
                double speed = 0.5;
                encoderDrive(speed, 24, 24);
            }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "time: " + runtime.toString());
        }
    }

    /******** GYROSCOPE STUFF **********/

    private boolean initGyroscope() {
        bosch = hardwareMap.get(BNO055IMU.class, "imu0");
        telemetry.addData("Gyro", "class:" + bosch.getClass().getName());

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        parameters.loggingTag = "bosch";
        //parameters.calibrationDataFile = "MonsieurMallahCalibration.json"; // see the calibration sample opmode
        boolean boschInit = bosch.initialize(parameters);
        return boschInit;
    }

    private void reportGyroscope() {
         /*AngularVelocity velocity = imu.getAngularVelocity(AngleUnit.DEGREES);
        String mesg = "x: " + velocity.xRotationRate + ", y:" + velocity.yRotationRate + ", z:" + velocity.zRotationRate;
        telemetry.addData("Gyro", mesg); */

        Orientation angles = bosch.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        telemetry.addData("Gyro", "angles: " + angles.firstAngle + "," + angles.secondAngle + "," + angles.thirdAngle);
        // Acceleration oa = bosch.getOverallAcceleration();
        //telemetry.addData("Gyro", "oa: " + oa);
        Acceleration la = bosch.getLinearAcceleration();
        double linear_force = Math.sqrt(la.xAccel * la.xAccel
                + la.yAccel * la.yAccel
                + la.zAccel * la.zAccel);
        telemetry.addData("Gyro", "la: " + la + "(" + linear_force + ")");
        Acceleration ga = bosch.getGravity();

        double gravity_force = Math.sqrt(ga.xAccel * ga.xAccel
                + ga.yAccel * ga.yAccel
                + ga.zAccel * ga.zAccel);
        telemetry.addData("Gyro", "ga: " + ga + "(" + gravity_force + ")");
        Position pos = bosch.getPosition();
        telemetry.addData("Gyro", "pos: " + pos);
        Velocity v = bosch.getVelocity();
        telemetry.addData("Gyro", "v: " + pos);
        Acceleration accel = bosch.getAcceleration();
        telemetry.addData("Gyro", "accel: " + accel);
        Temperature temp = bosch.getTemperature();
        telemetry.addData("Gyro", "temp: " + temp.temperature + " " + temp.unit.toString());
        MagneticFlux flux = bosch.getMagneticFieldStrength();
        telemetry.addData("Gyro", "flux: " + flux);
        BNO055IMU.SystemStatus status = bosch.getSystemStatus();
        telemetry.addData("Gyro", "status (" + bosch.isSystemCalibrated() + "): " + status);
        BNO055IMU.CalibrationStatus cstatus = bosch.getCalibrationStatus();
        telemetry.addData("Gyro", "cstatus: " + cstatus);
    }


    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches) {
        int BackLeftTarget = 0;
        int BackRightTarget = 0;
        int FrontLeftTarget = 0;
        int FrontRightTarget = 0;

        // Get the current position.
        int leftBackCurrent = motorBackLeft.getCurrentPosition();
        int rightBackCurrent = motorBackRight.getCurrentPosition();
        int leftFrontCurrent = motorFrontLeft.getCurrentPosition();
        int rightFrontCurrent = motorFrontRight.getCurrentPosition();
        telemetry.addData("encoderDrive", "Starting %7d, %7d, %7d, %7d",
                leftBackCurrent, rightBackCurrent, leftFrontCurrent, rightFrontCurrent);

        // Determine new target position, and pass to motor controller
        BackLeftTarget = leftBackCurrent + (int) (leftInches * BACK_COUNTS_PER_INCH);
        BackRightTarget = rightBackCurrent + (int) (rightInches * BACK_COUNTS_PER_INCH);
        FrontLeftTarget = leftFrontCurrent + (int) (leftInches * FRONT_COUNTS_PER_INCH);
        FrontRightTarget = rightFrontCurrent + (int) (rightInches * FRONT_COUNTS_PER_INCH);
        motorBackRight.setTargetPosition(BackRightTarget);
        motorBackLeft.setTargetPosition(BackLeftTarget);
        //motorFrontRight.setTargetPosition(FrontRightTarget);
        //motorFrontLeft.setTargetPosition(FrontLeftTarget);
        telemetry.addData("encoderDrive", "Target %7d, %7d, %7d, %7d",
                BackLeftTarget, BackRightTarget, FrontLeftTarget, FrontRightTarget);

        // Turn On RUN_TO_POSITION
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Start motion.
        motorBackLeft.setPower(Math.abs(speed));
        motorBackRight.setPower(Math.abs(speed));
        motorFrontLeft.setPower(Math.abs(speed));
        motorFrontRight.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.
        ElapsedTime motorOnTime = new ElapsedTime();
        while ((motorOnTime.seconds() < 30) &&
                (motorBackRight.isBusy() && motorBackLeft.isBusy())) {

            // Display it for the driver.
            telemetry.addData("encoderDrive (BL, BR, FL, FR)", "Running to %7d, %7d, %7d, %7d", BackLeftTarget, BackRightTarget, FrontLeftTarget, FrontRightTarget);
            telemetry.addData("encoderDrive (BL, BR, FL, FR)", "Running at %7d, %7d, %7d, %7d",
                    motorBackLeft.getCurrentPosition(),
                    motorBackRight.getCurrentPosition(),
                    motorFrontLeft.getCurrentPosition(),
                    motorFrontRight.getCurrentPosition());
            telemetry.update();
            sleep(100);
        }

        // Stop all motion;
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);

        // Turn off RUN_TO_POSITION
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        telemetry.addData("encoderDrive", "Started at %7d, %7d, %7d, %7d",
                leftBackCurrent,
                rightBackCurrent,
                leftFrontCurrent,
                rightFrontCurrent);
        telemetry.addData("encoderDrive", "Finished at %7d, %7d, %7d, %7d",
                motorBackLeft.getCurrentPosition(),
                motorBackRight.getCurrentPosition(),
                motorFrontLeft.getCurrentPosition(),
                motorFrontRight.getCurrentPosition());
        telemetry.addData("encoderDrive", "Finish Time at " + motorOnTime.toString());
        sleep(10000);
    }


    public void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
