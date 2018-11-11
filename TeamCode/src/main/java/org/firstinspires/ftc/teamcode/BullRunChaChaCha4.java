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
 * In order for localization to work, we need to tell the system where each target we
 * wish to use for navigation resides on the field, and we need to specify where on the robot
 * the phone resides. These specifications are in the form of <em>transformation matrices.</em>
 * Transformation matrices are a central, important concept in the math here involved in localization.
 * See <a href="https://en.wikipedia.org/wiki/Transformation_matrix">Transformation Matrix</a>
 * for detailed information. Commonly, you'll encounter transformation matrices as instances
 * of the {@link OpenGLMatrix} class.
 *
 * For the most part, you don't need to understand the details of the math of how transformation
 * matrices work inside (as fascinating as that is, truly). Just remember these key points:
 * <ol>
 *
 *     <li>You can put two transformations together to produce a third that combines the effect of
 *     both of them. If, for example, you have a rotation transform R and a translation transform T,
 *     then the combined transformation matrix RT which does the rotation first and then the translation
 *     is given by {@code RT = T.multiplied(R)}. That is, the transforms are multiplied in the
 *     <em>reverse</em> of the chronological order in which they applied.</li>
 *
 *     <li>A common way to create useful transforms is to use methods in the {@link OpenGLMatrix}
 *     class and the Orientation class. See, for example, {@link OpenGLMatrix#translation(float,
 *     float, float)}, {@link OpenGLMatrix#rotation(AngleUnit, float, float, float, float)}, and
 *     {@link Orientation#getRotationMatrix(AxesReference, AxesOrder, AngleUnit, float, float, float)}.
 *     Related methods in {@link OpenGLMatrix}, such as {@link OpenGLMatrix#rotated(AngleUnit,
 *     float, float, float, float)}, are syntactic shorthands for creating a new transform and
 *     then immediately multiplying the receiver by it, which can be convenient at times.</li>
 *
 *     <li>If you want to break open the black box of a transformation matrix to understand
 *     what it's doing inside, use {@link MatrixF#getTranslation()} to fetch how much the
 *     transform will move you in x, y, and z, and use {@link Orientation#getOrientation(MatrixF,
 *     AxesReference, AxesOrder, AngleUnit)} to determine the rotational motion that the transform
 *     will impart. See {@link #format(OpenGLMatrix)} below for an example.</li>
 *
 * </ol>
 *
 * This example places the "stones" image on the perimeter wall to the Left
 *  of the Red Driver station wall.  Similar to the Red Beacon Location on the Res-Q
 *
 * This example places the "chips" image on the perimeter wall to the Right
 *  of the Blue Driver station.  Similar to the Blue Beacon Location on the Res-Q
 *
 * See the doc folder of this project for a description of the field Axis conventions.
 *
 * Initially the target is conceptually lying at the origin of the field's coordinate system
 * (the center of the field), facing up.
 *
 * In this configuration, the target's coordinate system aligns with that of the field.
 *
 * In a real situation we'd also account for the vertical (Z) offset of the target,
 * but for simplicity, we ignore that here; for a real robot, you'll want to fix that.
 *
 * To place the Stones Target on the Red Audience wall:
 * - First we rotate it 90 around the field's X axis to flip it upright
 * - Then we rotate it  90 around the field's Z access to face it away from the audience.
 * - Finally, we translate it back along the X axis towards the red audience wall.
 */

@Autonomous(name="10101... BullCHACHACHACHACHACHA44444444!!!!!", group="MonsieurMallah")
public class BullRunChaChaCha4 extends OpMode {

    public static final String TAG = "Vuforia Navigation Sample";

    static final double MAX_POS = 1.0;     // Maximum rotational position
    static final double MIN_POS = 0.0;     // Minimum rotational position

    //constants from encoder sample
    static final double COUNTS_PER_MOTOR_REV = 560;    // http://www.revrobotics.com/content/docs/Encoder-Guide.pdf
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 7.75;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * Math.PI);

    // Elapsed time since the opmode started.
    private ElapsedTime runtime = new ElapsedTime();

    // Motors connected to the hub.
    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;

    // Hack stuff.
    private boolean useMotors = true;
    private boolean useEncoders = true;
    private boolean useNavigation = true;

    private boolean madeTheRun = false;
    /**
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        // Initialize the motors.
        if (useMotors) {
            motorBackLeft = hardwareMap.get(DcMotor.class, "motor0");
            motorBackRight = hardwareMap.get(DcMotor.class, "motor1");
            motorFrontLeft = hardwareMap.get(DcMotor.class, "motor2");
            motorFrontRight = hardwareMap.get(DcMotor.class, "motor3");

            // Most robots need the motor on one side to be reversed to drive forward
            // Reverse the motor that runs backwards when connected directly to the battery
            motorBackLeft.setDirection(DcMotor.Direction.FORWARD);
            motorBackRight.setDirection(DcMotor.Direction.REVERSE);
            motorFrontLeft.setDirection(DcMotor.Direction.FORWARD);
            motorFrontRight.setDirection(DcMotor.Direction.REVERSE);

            if (useEncoders) {
                motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                // NOTE: only use RUN_IUSING_ENCODER when you want to run a certain amount of spins;
                // running it like this made the right motor run slower than the left one when using
                // a power that is less than max.
                /*motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER); */

                motorBackRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorBackLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorFrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        }
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
            encoderDrive(speed, 23, 23);
            turnRight();
            turnRight();
            turnRight();
            turnRight();
            turnRight();
            turnRight();
            encoderDrive(speed, 25, 25);
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
        BackLeftTarget = leftBackCurrent + (int) (leftInches * COUNTS_PER_INCH);
        BackRightTarget = rightBackCurrent + (int) (rightInches * COUNTS_PER_INCH);
        FrontLeftTarget = leftFrontCurrent + (int) (leftInches * COUNTS_PER_INCH);
        FrontRightTarget = rightFrontCurrent + (int) (rightInches * COUNTS_PER_INCH);


        // HACK ALERT
        double hackFactor = 2.0; //2.6;
        BackRightTarget = (int) (BackRightTarget / hackFactor);
        BackLeftTarget = (int) (BackLeftTarget / hackFactor);


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
        //sleep(10000);
    }


    public void encoderDriveOld(double speed,
                             double leftInches, double rightInches) {
        int newLeftTarget;
        int newRightTarget;

        // Determine new target position, and pass to motor controller
        newLeftTarget = motorBackRight.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
        newRightTarget = motorBackLeft.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
        motorBackRight.setTargetPosition(newLeftTarget);
        motorBackLeft.setTargetPosition(newRightTarget);
        motorFrontRight.setTargetPosition(newLeftTarget);
        motorFrontLeft.setTargetPosition(newRightTarget);
        /*motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER); */

        // Turn On RUN_TO_POSITION
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        motorFrontRight.setPower(Math.abs(speed));
        motorFrontLeft.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.
        ElapsedTime motorOnTime = new ElapsedTime();
        while ((motorOnTime.seconds() < 30) &&
                (motorBackRight.isBusy() && motorBackLeft.isBusy() && motorFrontRight.isBusy() && motorFrontLeft.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
            telemetry.addData("Path2", "Running at %7d :%7d",
                    motorBackRight.getCurrentPosition(),
                    motorBackLeft.getCurrentPosition());
            telemetry.update();
            sleep(100);
        }

        // Stop all motion;
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);


        // Turn off RUN_TO_POSITION
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        }
    }

    //tested to turn aprox. ten to twelve degrees! (Flynn did this completely(No poppa))
    void turnLeft() {
        float power = 1.0f;
        motorBackLeft.setPower(-power);
        motorBackRight.setPower(power);
        //motorFrontLeft.setPower(-1.0);
       // motorFrontRight.setPower(1.0);
        sleep(2000 / 45);
    }

    //tested to turn aprox. ten to twelve degrees! (Same here!(no poppa))
    void turnRight() {
        float power = 0.5f;
        //motorBackLeft.setPower(power);
        //motorBackRight.setPower(-power);
        motorFrontLeft.setPower(1.0);
        motorFrontRight.setPower(-1.0);
        //sleep(2000/45);
        sleep(100);
    }
}

