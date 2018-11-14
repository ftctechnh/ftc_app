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

/**
 *
 */
@Autonomous(name="MarkerRun", group="MonsieurMallah")
public class TeamMarkerRun extends OpMode {

    // MAGIC NUMBERS for the motor encoders
    static final double COUNTS_PER_MOTOR_REV = 560;    // http://www.revrobotics.com/content/docs/Encoder-Guide.pdf
    // 28 cycles per rotation at the main motor, times 20:1 geared down
    static final double COUNTS_PER_MOTOR_TORKNADO = 1440; // https://asset.pitsco.com/sharedimages/resources/torquenado_dcmotorspecifications.pdf
    // 24 cycles per revolution, times 60:1 geared down.

    static final double WHEEL_DIAMETER_INCHES = 4.9375;     // For figuring circumference
    static final double COUNTS_PER_INCH = COUNTS_PER_MOTOR_TORKNADO / (WHEEL_DIAMETER_INCHES * Math.PI);

    // Elapsed time since the opmode started.
    private ElapsedTime runtime = new ElapsedTime();

    // Motors connected to the hub.
    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;

    //gyroscope built into hub
    private BNO055IMU bosch;

    // Hack stuff.
    private boolean useMotors = true;
    private boolean useFourWheelDrive = false;
    private boolean madeTheRun = false;
    private boolean hackTimeouts = true;

    /**
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        // Initialize the motors.
        if (useMotors) {
            motorBackLeft = hardwareMap.get(DcMotor.class, "motor0");
            motorBackRight = hardwareMap.get(DcMotor.class, "motor1");

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

        //init internal gyroscope of hub
        initGyroscope();

        // This code prevents the OpMode from freaking out if you go to sleep for more than a second.
        if (hackTimeouts) {
            this.msStuckDetectInit = 30000;
            this.msStuckDetectInitLoop = 30000;
            this.msStuckDetectStart = 30000;
            this.msStuckDetectLoop = 30000;
            this.msStuckDetectStop = 30000;
        }
    }

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


    /**
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop () {
    }

    /**
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start () {
        // Reset the game timer.
        runtime.reset();
    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop () {
    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop () {

        if (madeTheRun == false) {
            double speed = 0.5;

            // forward 35 inches, turn 90degrees, forward 40 inches
            encoderDrive(speed, 59, 59);
            turnRight(-90);
            encoderDrive(speed, 22, 22);
            sleep( 4000);
            turnRight(180);
            encoderDrive(speed, 64, 64);
            // encoderDrive(speed, 25, 25);
            madeTheRun = true;

        }

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "time: " + runtime.toString());
        telemetry.addData("Status", "madeTheRun=%b", madeTheRun);
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

        // Get the current position.
        int leftBackStart = motorBackLeft.getCurrentPosition();
        int rightBackStart = motorBackRight.getCurrentPosition();
        telemetry.addData("encoderDrive", "Starting %7d, %7d",
                leftBackStart, rightBackStart);

        // Determine new target position, and pass to motor controller
        int leftBackTarget = leftBackStart + (int) (leftInches * COUNTS_PER_INCH);
        int rightBackTarget = rightBackStart + (int) (rightInches * COUNTS_PER_INCH);

        motorBackLeft.setTargetPosition(leftBackTarget);
        motorBackRight.setTargetPosition(rightBackTarget);
        telemetry.addData("encoderDrive", "Target %7d, %7d",
                leftBackTarget, rightBackTarget);

        // Turn On RUN_TO_POSITION
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setPower(Math.abs(speed));
        motorBackRight.setPower(Math.abs(speed));

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


    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        }
    }

    private float getGyroscopeAngle() {
        Orientation exangles = bosch.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        float gyroAngle = exangles.thirdAngle;
        return CrazyAngle.reverseAngle(gyroAngle);
    }


    void turnRight(float deltaAngle) {
        //determining the destination angle(da) by using the current angle(ca). if da is over 360, get the difference between the two.
        float ca_original = getGyroscopeAngle();
        float ca = ca_original;
        float da = ca + deltaAngle;
        while(da>360) {
            da=da-360;
        }

        boolean reachedDestination = false;
        if(reachedDestination == false) {
            // TODO: fix this so it works over the 360->0 boundary (when it wraps)
            float angleToGo = da - ca;
            // if (angleToGo < 0)
            //     angleToGo = angleToGo + 360;

            while(angleToGo > 0) {

                float oldAngle = getGyroscopeAngle();

                nudgeRight();
                ca = getGyroscopeAngle();

                float justMoved = ca - oldAngle;
                float stillNeed = da - ca;

                telemetry.addData("turnRight", "current = %.0f, destination = %.0f, moved=%.0f, need=%.0f", ca, da, justMoved, stillNeed);
                telemetry.update();

                angleToGo = da - ca;
            }
            reachedDestination = true;
        }

        // turn off power now that we are done turning
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);

        telemetry.addData("turnRight", "ending current = %.0f, destination = %.0f", ca, da);
        telemetry.update();
        sleep(1000);
    }


    //tested to turn aprox. ten to twelve degrees! (Same here!(no poppa))
    void nudgeRight() {
        float power = 0.5f;
        motorBackLeft.setPower(power);
        motorBackRight.setPower(-power);
        sleep(2); // TODO: is 100 milliseconds a good value????????
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
    }
}
