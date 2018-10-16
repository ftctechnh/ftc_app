package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
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

@TeleOp(name="Monsieur Mallah Vuforia", group="MonsieurMallah")
public class MonsieurMallahNavigation extends OpMode {

    public static final String TAG = "Vuforia Navigation Sample";

    static final double INCREMENT = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int CYCLE_MS = 50;     // period of each cycle
    static final double MAX_POS = 1.0;     // Maximum rotational position
    static final double MIN_POS = 0.0;     // Minimum rotational position

    //constants from encoder sample
    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;


    private static final String VUFORIA_KEY = "AfgOBrf/////AAABmRjMx12ilksPnWUyiHDtfRE42LuceBSFlCTIKmmNqCn2EOk3I4NtDCSr0wCLFxWPoLR2qHKraX49ofQ2JknI76SJS5Hy8cLbIN+1GlFDqC8ilhuf/Y1yDzKN6a4n0fYWcEPlzHRc8C1V+D8vZ9QjoF3r//FDDtm+M3qlmwA7J/jNy4nMSXWHPCn2IUASoNqybTi/CEpVQ+jEBOBjtqxNgb1CEdkFJrYGowUZRP0z90+Sew2cp1DJePT4YrAnhhMBOSCURgcyW3q6Pl10XTjwB4/VTjF7TOwboQ5VbUq0wO3teE2TXQAI53dF3ZUle2STjRH0Rk8H94VtHm9u4uitopFR7zmxVl3kQB565EUHwfvG";

    // Since ImageTarget trackables use mm to specifiy their dimensions, we must use mm for all the physical dimension.
    // We will define some constants and conversions here
    private static final float MM_PER_INCH = 25.4f;
    private static final float FIELD_WIDTH = (12*6) * MM_PER_INCH;       // the width of the FTC field (from the center point to the outer panels)
    private static final float TARGET_HEIGHT = (6) * MM_PER_INCH;          // the height of the center of the target image above the floor

    // Elapsed time since the opmode started.
    private ElapsedTime runtime = new ElapsedTime();

    // Internal Gyroscope in the Rev Hub.
    private BNO055IMU bosch;

    // Motors connected to the hub.
    private DcMotor motorLeft;
    private DcMotor motorRight;
    private DcMotor sweeper;
    private DcMotor arm;

    // Hand servo.
    private Servo servoHand;
    private double angleHand;

    // Navigation stuff.
    private OpenGLMatrix lastLocation;
    private VuforiaLocalizer vuforia;
    private VuforiaTrackables trackables;
    private List<VuforiaTrackable> allTrackables;
    private VuforiaTrackable blueRover;
    private VuforiaTrackable redFootprint;
    private VuforiaTrackable frontCraters;
    private VuforiaTrackable backSpace;

    // Hack stuff.
    private boolean useServoHand = false;
    private boolean useMotors = true;
    private boolean useEncoders = false;
    private boolean useNavigation = true;
    private boolean useNavigationDisplay = true;
    private boolean useTestField = false;


    /**
     * This will build up the geometry of the test field in the Connolly basment, with just the Rover picture mounted 600 mm in front of the origin point.
     */
    private void buildPlayfieldConnollyBasement() {
        // Target 1:
        OpenGLMatrix roverLocation = OpenGLMatrix
                .translation(600, 0, TARGET_HEIGHT)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ,
                        AngleUnit.DEGREES, 90, 0, -90));
        blueRover.setLocation(roverLocation);
    }

    private void buildPlayfieldREAL() {
        /**
         * In order for localization to work, we need to tell the system where each target is on the field, and
         * where the phone resides on the robot.  These specifications are in the form of <em>transformation matrices.</em>
         * Transformation matrices are a central, important concept in the math here involved in localization.
         * See <a href="https://en.wikipedia.org/wiki/Transformation_matrix">Transformation Matrix</a>
         * for detailed information. Commonly, you'll encounter transformation matrices as instances
         * of the {@link OpenGLMatrix} class.
         *
         * If you are standing in the Red Alliance Station looking towards the center of the field,
         *     - The X axis runs from your left to the right. (positive from the center to the right)
         *     - The Y axis runs from the Red Alliance Station towards the other side of the field
         *       where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
         *     - The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
         *
         * This Rover Ruckus sample places a specific target in the middle of each perimeter wall.
         *
         * Before being transformed, each target image is conceptually located at the origin of the field's
         *  coordinate system (the center of the field), facing up.
         */

        /**
         * To place the BlueRover target in the middle of the blue perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Then, we translate it along the Y axis to the blue perimeter wall.
         */
        OpenGLMatrix blueRoverLocationOnField = OpenGLMatrix
                .translation(0, FIELD_WIDTH, TARGET_HEIGHT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0));
        blueRover.setLocation(blueRoverLocationOnField);

        /**
         * To place the RedFootprint target in the middle of the red perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it 180 around the field's Z axis so the image is flat against the red perimeter wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the negative Y axis to the red perimeter wall.
         */
        OpenGLMatrix redFootprintLocationOnField = OpenGLMatrix
                .translation(0, -FIELD_WIDTH, TARGET_HEIGHT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180));
        redFootprint.setLocation(redFootprintLocationOnField);

        /**
         * To place the FrontCraters target in the middle of the front perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it 90 around the field's Z axis so the image is flat against the front wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the negative X axis to the front perimeter wall.
         */
        OpenGLMatrix frontCratersLocationOnField = OpenGLMatrix
                .translation(-FIELD_WIDTH, 0, TARGET_HEIGHT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90));
        frontCraters.setLocation(frontCratersLocationOnField);

        /**
         * To place the BackSpace target in the middle of the back perimeter wall:
         * - First we rotate it 90 around the field's X axis to flip it upright.
         * - Second, we rotate it -90 around the field's Z axis so the image is flat against the back wall
         *   and facing inwards to the center of the field.
         * - Then, we translate it along the X axis to the back perimeter wall.
         */
        OpenGLMatrix backSpaceLocationOnField = OpenGLMatrix
                .translation(FIELD_WIDTH, 0, TARGET_HEIGHT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90));
        backSpace.setLocation(backSpaceLocationOnField);
    }

    /**
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        // Initialize the motors.
        if (useMotors) {
            motorLeft = hardwareMap.get(DcMotor.class, "motor0");
            motorRight = hardwareMap.get(DcMotor.class, "motor1");
            sweeper = hardwareMap.get(DcMotor.class, "motor2");
            arm = hardwareMap.get(DcMotor.class, "motor3");

            servoHand = hardwareMap.get(Servo.class, "servo0");
            angleHand = (MAX_POS - MIN_POS) / 2; // Start at halfway position

            // Most robots need the motor on one side to be reversed to drive forward
            // Reverse the motor that runs backwards when connected directly to the battery
            //motorLeft.setDirection(DcMotor.Direction.REVERSE);
            motorLeft.setDirection(DcMotor.Direction.FORWARD);
            motorRight.setDirection(DcMotor.Direction.FORWARD);

            if (useEncoders) {
                motorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
        }

        if (useNavigation) {
            /*
             * To start up Vuforia, tell it the view that we wish to use for camera monitor (on the RC phone);
             * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
             */
            int cameraMonitorViewId = 0;
            if (useNavigationDisplay) {
                cameraMonitorViewId =
                        hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId",
                                "id", hardwareMap.appContext.getPackageName());
            }
            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
            parameters.vuforiaLicenseKey = VUFORIA_KEY;
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
            this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

            // Load the data sets that for the trackable objects. These particular data
            // sets are stored in the 'assets' part of our application.
            trackables = this.vuforia.loadTrackablesFromAsset("RoverRuckus");
            blueRover = trackables.get(0);
            blueRover.setName("Blue-Rover");
            redFootprint = trackables.get(1);
            redFootprint.setName("Red-Footprint");
            frontCraters = trackables.get(2);
            frontCraters.setName("Front-Craters");
            backSpace = trackables.get(3);
            backSpace.setName("Back-Space");
            allTrackables = new ArrayList<VuforiaTrackable>();
            allTrackables.addAll(trackables);

            if (useTestField) {
                buildPlayfieldConnollyBasement();
            }else {
                buildPlayfieldREAL();
            }

            /**
             * Create a transformation matrix describing where the phone is on the robot. Here, we
             * put the phone on the right hand side of the robot with the screen facing in (see our
             * choice of BACK camera above) and in landscape mode. Starting from alignment between the
             * robot's and phone's axes, this is a rotation of -90deg along the Y axis.
             *
             * When determining whether a rotation is positive or negative, consider yourself as looking
             * down the (positive) axis of rotation from the positive towards the origin. Positive rotations
             * are then CCW, and negative rotations CW. An example: consider looking down the positive Z
             * axis towards the origin. A positive rotation about Z (ie: a rotation parallel to the the X-Y
             * plane) is then CCW, as one would normally expect from the usual classic 2D geometry.
             *
             *  .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
             CAMERA_CHOICE == FRONT ? 90 : -90, 0, 0));
             */
            final int CAMERA_FORWARD_DISPLACEMENT  = 0; // 110;   // eg: Camera is 110 mm in front of robot center
            final int CAMERA_VERTICAL_DISPLACEMENT = 0; // 200;   // eg: Camera is 200 mm above ground
            final int CAMERA_LEFT_DISPLACEMENT     = 0;     // eg: Camera is ON the robot's center line


            OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                    .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
                             -90, 90, 0));

            /**  Let all the trackable listeners know where the phone is.  */
            for (VuforiaTrackable trackable : allTrackables)
            {
                ((VuforiaTrackableDefaultListener)trackable.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
            }

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

        if (useNavigation) {
            /** Start tracking the data sets we care about. */
            trackables.activate();
        }
    }

    /**
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

        if (useNavigation) {
            /** Stop tracking the data sets we care about. */
            trackables.deactivate();
        }
    }

    /**
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        if (useNavigation) {
            for (VuforiaTrackable trackable : allTrackables) {
                /**
                 * getUpdatedRobotLocation() will return null if no new information is available since
                 * the last time that call was made, or if the trackable is not currently visible.
                 * getRobotLocation() will return null if the trackable is not currently visible.
                 */
                telemetry.addData(trackable.getName(), ((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible() ? "Visible" : "Not Visible");    //

                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }
            }
            /**
             * Provide feedback as to where the robot was last located (if we know).
             */
            if (lastLocation != null) {
                //  RobotLog.vv(TAG, "robot=%s", format(lastLocation));
                telemetry.addData("Pos", format(lastLocation));

                // express position (translation) of robot in inches.
                VectorF translation = lastLocation.getTranslation();
                telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                        translation.get(0) / MM_PER_INCH, translation.get(1) / MM_PER_INCH, translation.get(2) / MM_PER_INCH);

                // express the rotation of the robot in degrees.
                Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
                telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);

            } else {
                telemetry.addData("Pos", "Unknown");
            }
        }

        if (useMotors) {
            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
            double leftPower = Range.clip(drive + turn, -1.0, 1.0);
            double rightPower = Range.clip(drive - turn, -1.0, 1.0);
            motorLeft.setPower(leftPower);
            motorRight.setPower(rightPower);

            // Control the sweeper.
            boolean suckIn = gamepad1.right_bumper;
            boolean suckOut = gamepad1.left_bumper;
            double suckPower = 0.0;
            if (suckIn) {
                suckPower = -1.0;
            } else if (suckOut) {
                suckPower = 1.0;
            }
            sweeper.setPower(suckPower);

            //control the arm
            float pullUp = gamepad1.right_trigger;
            float pullDown = gamepad1.left_trigger;
            double pullPower = 0.0;
            if ((pullUp > 0.0) && (pullDown == 0.0)) {
                pullPower = -1.0;
            } else if ((pullDown > 0.0) && (pullUp == 0.0)) {
                pullPower = 1.0;
            }
            arm.setPower(pullPower);

            // control the hand
            if (useServoHand) {
                if (gamepad1.dpad_up) {
                    // Keep stepping up until we hit the max value.
                    angleHand += INCREMENT;
                    angleHand = Math.min(angleHand, MAX_POS);
                } else if (gamepad1.dpad_down) {
                    // Keep stepping down until we hit the min value.
                    angleHand -= INCREMENT;
                    angleHand = Math.max(angleHand, MIN_POS);
                }
            }
            servoHand.setPosition(angleHand);

            // HACK: If press the secret  y key, go forward 12 inchses to test encider.
            if (useEncoders) {
                boolean encodertest = gamepad1.y;
                if (encodertest) {
                    double speed = 1;
                    encoderDrive(speed, 12, 12);
                    encoderDrive(speed, 12, -12);
                    encoderDrive(speed, 12, 12);
                    encoderDrive(speed,5,5);
                }
            }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "time: " + runtime.toString());
            telemetry.addData("Motors", "left:%.2f, right:%.2f, lpos:%d, rpos=%d",
                    leftPower, rightPower, motorLeft.getCurrentPosition(), motorRight.getCurrentPosition());
            telemetry.addData("Motors", "drive (%.2f), turn (%.2f)", drive, turn);
            telemetry.addData("Sweeper", "sweep (%.2f)", suckPower);
            telemetry.addData("Hand", " angle %5.2f", angleHand);
        }
    }


    enum StartingPosition {
        Position_A,
        Position_B,
        Position_C,
        Position_D
    };

    enum Team {
        TeamBlue,
        TeamRed
    };

    private void awesomecool () {
        // TODO: swivel camera until an image is found

        // TODO: get position from viewmark
        int x = 0;
        int y = 0;

        // Which starting position am I in?
        StartingPosition position;
        if((x>0) && (y>0)){
            position = StartingPosition.Position_A;
        }else if((x>0) && (y<0)) {
            position = StartingPosition.Position_D;
        }else if((x<0) && (y>0)) {
            position = StartingPosition.Position_B;
        }else {
            position = StartingPosition.Position_C;
        }

        // Which team am i on?
        Team team;
        if ((position == StartingPosition.Position_D) || (position == StartingPosition.Position_C))
        {
            team = Team.TeamRed;
        } else
        {
            team = Team.TeamBlue;
        }

        // Which crater should i look for?
        int targetX = 0;
        int targetY = 0;
        if (team == Team.TeamRed){
            targetX = -72;
            targetY = -72;
        } else {
            targetX = 72;
            targetY = 72;
        }
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
        int newLeftTarget;
        int newRightTarget;

        // Determine new target position, and pass to motor controller
        newLeftTarget = motorRight.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
        newRightTarget = motorLeft.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
        motorRight.setTargetPosition(newLeftTarget);
        motorLeft.setTargetPosition(newRightTarget);

        // Turn On RUN_TO_POSITION
        motorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        runtime.reset();
        motorRight.setPower(Math.abs(speed));
        motorLeft.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.
        ElapsedTime motorOnTime = new ElapsedTime();
        while ((motorOnTime.seconds() < 30) &&
                (motorRight.isBusy() && motorLeft.isBusy())) {

            // Display it for the driver.
            telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
            telemetry.addData("Path2", "Running at %7d :%7d",
                    motorRight.getCurrentPosition(),
                    motorLeft.getCurrentPosition());
            telemetry.update();
           sleep(100);
        }

        // Stop all motion;
        motorRight.setPower(0);
        motorLeft.setPower(0);

        // Turn off RUN_TO_POSITION
        motorRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * A simple utility that extracts positioning information from a transformation matrix
     * and formats it in a form palatable to a human being.
     */
    String format(OpenGLMatrix transformationMatrix) {
        return transformationMatrix.formatAsTransform();
    }
}

