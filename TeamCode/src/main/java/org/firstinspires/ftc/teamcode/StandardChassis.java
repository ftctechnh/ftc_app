package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Temperature;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;


public abstract class StandardChassis extends OpMode {


    //static final double COUNTS_PER_INCH = COUNTS_PER_MOTOR_TORQUENADO / (WHEEL_DIAMETER_INCHES * Math.PI);

    protected ChassisConfig config;

    // Elapsed time since the opmode started.
    protected ElapsedTime runtime = new ElapsedTime();
    protected ElapsedTime dropTime = new ElapsedTime();

    // Motors connected to the hub.
    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;

    protected Servo flagHolder;
    protected double angleHand;

    //gyroscope built into hub
    private BNO055IMU bosch;

    // Hack stuff.
    private boolean useMotors = true;
    private boolean useFourWheelDrive = false;
    private boolean hackTimeouts = true;

    protected StandardChassis(ChassisConfig config) {
        this.config = config;
    }

    protected void initMotors() {

        // Initialize the motors.
        if (useMotors) {
            motorBackLeft = hardwareMap.get(DcMotor.class, "motor0");
            motorBackRight = hardwareMap.get(DcMotor.class, "motor1");

            flagHolder = hardwareMap.get(Servo.class, "servo1");
            angleHand = 0.75;
            flagHolder.setPosition(angleHand);

            // Most robots need the motor on one side to be reversed to drive forward
            // Reverse the motor that runs backwards when connected directly to the battery
            motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
            motorBackRight.setDirection(DcMotor.Direction.FORWARD);

            // initilize the encoder
            motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            //tell the encoder what mode to run in
            motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            if (useFourWheelDrive) {
                motorFrontLeft = hardwareMap.get(DcMotor.class, "motor2");
                motorFrontRight = hardwareMap.get(DcMotor.class, "motor3");

                motorBackLeft.setDirection(DcMotor.Direction.FORWARD);
                motorBackRight.setDirection(DcMotor.Direction.REVERSE);
                motorFrontLeft.setDirection(DcMotor.Direction.FORWARD);
                motorFrontRight.setDirection(DcMotor.Direction.REVERSE);

                motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        }

    }

    protected void initTimeouts() {
        // This code prevents the OpMode from freaking out if you go to sleep for more than a second.
        if (hackTimeouts) {
            this.msStuckDetectInit = 30000;
            this.msStuckDetectInitLoop = 30000;
            this.msStuckDetectStart = 30000;
            this.msStuckDetectLoop = 30000;
            this.msStuckDetectStop = 30000;
        }
    }

    protected boolean initGyroscope() {
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



    public void dropFlag() {
        angleHand = 0;
        flagHolder.setPosition(angleHand);
    }


    protected void encoderDrive(double speed, double leftInches, double rightInches) {

        double countsPerInch = config.getRearWheelSpeed() / (config.getRearWheelDiameter() * Math.PI);

        // Get the current position.
        int leftBackStart = motorBackLeft.getCurrentPosition();
        int rightBackStart = motorBackRight.getCurrentPosition();
        telemetry.addData("encoderDrive", "Starting %7d, %7d",
                leftBackStart, rightBackStart);

        // Determine new target position, and pass to motor controller
        int leftBackTarget = leftBackStart + (int) (leftInches * countsPerInch);
        int rightBackTarget = rightBackStart + (int) (rightInches * countsPerInch);

        motorBackLeft.setTargetPosition(leftBackTarget);
        motorBackRight.setTargetPosition(rightBackTarget);
        telemetry.addData("encoderDrive", "Target %7d, %7d", leftBackTarget, rightBackTarget);

        // Turn On RUN_TO_POSITION
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setPower(Math.abs(speed));
        motorBackRight.setPower(Math.abs(speed));
        if (useFourWheelDrive) {
            motorFrontLeft.setPower(Math.abs(speed));
            motorFrontRight.setPower(Math.abs(speed));
        }

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
            // telemetry.addData("encoderDrive", "Running to %7d, %7d", motorBackLeft, motorBackRight);
            telemetry.addData("encoderDrive", "Running at %7d, %7d",
                    motorBackLeft.getCurrentPosition(),
                    motorBackRight.getCurrentPosition());
            telemetry.update();
            sleep(100);
        }

        // Turn off RUN_TO_POSITION
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        if (useFourWheelDrive) {
            motorFrontLeft.setPower(0);
            motorFrontRight.setPower(0);
        }

        telemetry.addData("encoderDrive", "Finished (%s) at %7d, %7d to [%7d, %7d] (%7d, %7d)",
                motorOnTime.toString(),
                leftBackStart,
                rightBackStart,
                motorBackLeft.getCurrentPosition(),
                motorBackRight.getCurrentPosition(),
                leftBackTarget,
                rightBackTarget);
        sleep(1000);
    }


    protected void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        }
    }


    // Always returns a number from 0-359.9999
    protected float getGyroscopeAngle() {
        Orientation exangles = bosch.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        float gyroAngle = exangles.thirdAngle;
        return CrazyAngle.normalizeAngle(CrazyAngle.reverseAngle(gyroAngle));
    }



    /**
     *
     * @param deltaAngle
     */
    protected void turnLeft(float deltaAngle) {
        assert(deltaAngle > 0.0);
        assert(deltaAngle <= 360.0);

        // does it wrap at all? (go around the zero mark?)
        float currentAngle = getGyroscopeAngle();
        float destinationAngle = currentAngle - deltaAngle;
        boolean doesItWrapAtAll = (destinationAngle < 0.0);
        destinationAngle = CrazyAngle.normalizeAngle(destinationAngle);

        // Get it past the zero mark.
        if (doesItWrapAtAll) {
            boolean keepGoing = true;
            while (keepGoing) {
                float oldAngle = currentAngle;
                nudgeLeft();
                currentAngle = getGyroscopeAngle();

                float justMoved = oldAngle - currentAngle;
                float stillNeed = currentAngle;
                telemetry.addData("turnLeft1", "current=%.0f, old=%.0f, dst=%.0f, moved=%.0f, need=%.0f", currentAngle, oldAngle, destinationAngle, justMoved, stillNeed);
                telemetry.update();

                keepGoing = (justMoved > -50.0);
            }
        }

        // turn the last part
        while ((currentAngle - destinationAngle) > 5.0) {

            float oldAngle = currentAngle;
            nudgeLeft();
            currentAngle = getGyroscopeAngle();

            float justMoved = oldAngle - currentAngle;
            float stillNeed = currentAngle - destinationAngle;
            telemetry.addData("turnLeft2", "current = %.0f, destination = %.0f, moved=%.0f, need=%.0f", currentAngle, destinationAngle, justMoved, stillNeed);
            telemetry.update();
        }
    }

    /**
     *
     * @param deltaAngle
     */
    protected void turnRight(float deltaAngle) {
        assert(deltaAngle > 0.0);
        assert(deltaAngle <= 360.0);

        // does it wrap at all?
        float currentAngle = getGyroscopeAngle();
        float destinationAngle = currentAngle + deltaAngle;
        boolean doesItWrapAtAll = (destinationAngle > 360.0);
        destinationAngle = CrazyAngle.normalizeAngle(destinationAngle);

        // Get it past the zero mark.
        if (doesItWrapAtAll) {
            boolean keepGoing = true;
            while (keepGoing) {
                float oldAngle = currentAngle;
                nudgeRight();
                currentAngle = getGyroscopeAngle();

                float justMoved = currentAngle - oldAngle;
                float stillNeed = 360.0f - currentAngle;
                telemetry.addData("turRight1", "current=%.0f, old=%.0f, dst=%.0f, moved=%.0f, need=%.0f", currentAngle, oldAngle, destinationAngle, justMoved, stillNeed);
                telemetry.update();

                keepGoing = (justMoved > -50.0);
            }
        }

        // turn the last part
        while ((destinationAngle - currentAngle) > 5.0) {

            float oldAngle = currentAngle;
            nudgeRight();
            currentAngle = getGyroscopeAngle();

            float justMoved = currentAngle - oldAngle;
            float stillNeed = destinationAngle - currentAngle;
            telemetry.addData("turnRight2", "current = %.0f, destination = %.0f, moved=%.0f, need=%.0f", currentAngle, destinationAngle, justMoved, stillNeed);
            telemetry.update();
        }
    }

    // This nudges over about 2 degrees.
    protected void nudgeRight() {
        float power = 0.5f;
        motorBackLeft.setPower(power);
        motorBackRight.setPower(-power);
        sleep(2); // TODO: is 100 milliseconds a good value????????
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
    }

    // This nudges over about 2 degrees.
    protected void nudgeLeft() {
        float power = 0.5f;
        motorBackLeft.setPower(-power);
        motorBackRight.setPower(power);
        sleep(2); // TODO: is 100 milliseconds a good value????????
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
    }
}
