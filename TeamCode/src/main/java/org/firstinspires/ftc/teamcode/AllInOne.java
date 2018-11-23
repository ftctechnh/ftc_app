package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


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

@TeleOp(name="All in One", group="MonsieurMallah")
public class AllInOne extends OpMode {

    public static final String TAG = "Vuforia Navigation Sample";

    static final double MAX_POS = 1.0;     // Maximum rotational position
    static final double MIN_POS = 0.0;     // Minimum rotational position

    //constants from encoder sample
    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.9375;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    // Elapsed time since the opmode started.
    private ElapsedTime runtime = new ElapsedTime();

    // Motors connected to the hub.
    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;
    private DcMotor extender;
    private DcMotor tacVac;
    private DcMotor shoulder;

    // Hack stuff.
    private boolean useMotors = true;
    private boolean useEncoders = true;
    private boolean useNavigation = true;

    //tested to turn aprox. ten to twelve degrees! (Flynn did this completely(No poppa))
        /*void turnLeft() {
           motorBackLeft.setPower(-1.0);
           motorBackRight.setPower(1.0);
           motorFrontRight.setPower(1.0);
           motorFrontLeft.setPower(-1.0);
        }

       // tested to turn aprox. ten to twelve degrees! (Same here!(no poppa))
        void turnRight() {
            motorBackLeft.setPower(1.0);
            motorBackRight.setPower(-1.0);
            motorFrontRight.setPower(-1.0);
            motorFrontLeft.setPower(1.0);
        }*/


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
            extender = hardwareMap.get(DcMotor.class, "motor6");
            tacVac = hardwareMap.get(DcMotor.class, "motor4");
            shoulder = hardwareMap.get(DcMotor.class, "motor7");

            // Most robots need the motor on one side to be reversed to drive forward
            // Reverse the motor that runs backwards when connected directly to the battery
            motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
            motorBackRight.setDirection(DcMotor.Direction.FORWARD);
            motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
            motorFrontRight.setDirection(DcMotor.Direction.FORWARD);

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
            }
            motorFrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

//package com.qualcomm.robotcore.hardware;

    /**
     * Instances of DcMotorSimple interface provide a most basic motor-like functionality
     */
    /*public interface DcMotorSimple extends HardwareDevice {


        void setPower(double power);

        double getPower();
    } */


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

    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

        if (useNavigation) {
            /** Stop tracking the data sets we care about. */
        }
    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if (useMotors) {
            // Control the wheel motors.
            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = gamepad1.left_stick_y;
            if (Math.abs(drive) < 0.1)
                drive = 0.0; // Prevent the output from saying "-0.0".

            double turn = -gamepad1.right_stick_x;
            double leftPower = Range.clip(drive + turn, -0.8, 0.8);
            double rightPower = Range.clip(drive - turn, -0.8, 0.8);
            double halfLeftPower = Range.clip(drive + turn, -0.25, 0.25);
            double halfRightPower = Range.clip(drive - turn, -0.25, 0.25);

            boolean halfSpeed = gamepad1.left_bumper && gamepad1.right_bumper;

         /*   void turnRight() {
                motorBackLeft.setPower(1.0);
                motorBackRight.setPower(-1.0);
                motorFrontRight.setPower(-1.0);
                motorFrontLeft.setPower(1.0);
            }
            void turnLeft() {
                motorBackLeft.setPower(-1.0);
                motorBackRight.setPower(1.0);
                motorFrontRight.setPower(1.0);
                motorFrontLeft.setPower(-1.0);
            }*/
            if (halfSpeed) {
                motorBackLeft.setPower(halfLeftPower);
                motorBackRight.setPower(halfRightPower);
                motorFrontLeft.setPower(halfLeftPower);
                motorFrontRight.setPower(halfRightPower);
            } else {
                motorBackLeft.setPower(leftPower);
                motorBackRight.setPower(rightPower);
                motorFrontLeft.setPower(leftPower);
                motorFrontRight.setPower(rightPower);
            }


            // Report motor telemetry: note that the left motor is mounted upside down compared to the first one,
            // so it counts in reverse. We multiply it by -1 to change the sign, and so both motors count the same way.

            if (halfSpeed){
                boolean pullUp = gamepad1.dpad_down;
                boolean pullOut = gamepad1.dpad_up;
                double pullPower = 0.0;
                if (pullUp) {
                    pullPower = -0.5;
                } else if (pullOut) {
                    pullPower = 0.5;
                }
                shoulder.setPower(pullPower);

                boolean pullIn = gamepad1.a;
                boolean pushOut = gamepad1.y;
                double extenderPower = 0.0;
                if (pullIn) {
                    extenderPower = -0.5;
                } else if (pushOut) {
                    extenderPower = 0.5;
                }
                extender.setPower(extenderPower);

                float suckIn = gamepad1.left_trigger;
                float suckOut = gamepad1.right_trigger;
                double suckPower = 0.0;
                if ((suckIn > 0.0) && (suckOut == 0.0)) {
                        extenderPower = -0.5;
                } else if ((suckOut > 0.0) && (suckIn == 0.0)) {
                    extenderPower = 0.5;
                }
                tacVac.setPower(suckPower);
            }else{
                boolean pullUp = gamepad1.dpad_down;
                boolean pullOut = gamepad1.dpad_up;
                double pullPower = 0.0;
                if (pullUp) {
                    pullPower = -1.0;
                } else if (pullOut) {
                    pullPower = 1.0;
                }
                shoulder.setPower(pullPower);

                boolean pullIn = gamepad1.a;
                boolean pushOut = gamepad1.y;
                double extenderPower = 0.0;
                if (pullIn) {
                    extenderPower = -1.0;
                } else if (pushOut) {
                    extenderPower = 1.0;
                }
                extender.setPower(extenderPower);

                float suckIn = gamepad1.left_trigger;
                float suckOut = gamepad1.right_trigger;
                double suckPower = 0.0;
                if ((suckIn > 0.0) && (suckOut == 0.0)) {
                    extenderPower = -1.0;
                } else if ((suckOut > 0.0) && (suckIn == 0.0)) {
                    extenderPower = 1.0;
                }
                tacVac.setPower(suckPower);

            }





            // Control the shoulder arm.

           /* double shoulderPower = -gamepad2.left_stick_y;
            if (gamepad2.left_bumper && gamepad2.right_bumper){
                shoulderPower = Range.clip( shoulderPower, -0.4, 0.4);
            }else {
                shoulderPower = Range.clip( shoulderPower, -0.8, 0.8);
            }
            shoulder.setPower(shoulderPower);

            // control the vacuum
            double suckPower = gamepad1.y;
            if (gamepad2.left_bumper && gamepad2.right_bumper){
                shoulderPower = Range.clip( shoulderPower, -0.5, 0.5);
            }else {
                tacVac.setPower(suckPower);

            } */
        }


        /*public void encoderDrive(double speed, double leftInches, double rightInches) {
        int newLeftBackTarget;
        int newRightBackTarget;

        // Determine new target position, and pass to motor controller
        newLeftBackTarget = motorBackRight.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
        newRightBackTarget = motorBackLeft.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
        motorBackRight.setTargetPosition(newLeftBackTarget);
        motorBackLeft.setTargetPosition(newRightBackTarget);
        motorFrontRight.setTargetPosition(newLeftBackTarget);
        motorFrontLeft.setTargetPosition(newRightBackTarget);


        /*motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER); */

        // Turn On RUN_TO_POSITION
       /* motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION); */


        // reset the timeout time and start motion.
        /*runtime.reset();
        motorBackRight.setPower(Math.abs(speed));
        motorBackLeft.setPower(Math.abs(speed));
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
                (motorBackRight.isBusy() && motorBackLeft.isBusy())) {


            // Stop all motion;
            motorBackRight.setPower(0);
            motorBackLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorFrontLeft.setPower(0);


            // Turn off RUN_TO_POSITION
            motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/
        }


    }

