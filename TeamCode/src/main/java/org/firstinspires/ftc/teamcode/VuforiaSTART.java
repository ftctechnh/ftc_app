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
//@Autonomous(name="VuforiaSTART", group="MonsieurMallah")
public class VuforiaSTART /* extends StandardChassis */ {

//    // MAGIC NUMBERS for the motor encoders
//    static final double COUNTS_PER_MOTOR_REV = 560;    // http://www.revrobotics.com/content/docs/Encoder-Guide.pdf
//    // 28 cycles per rotation at the main motor, times 20:1 geared down
//    static final double COUNTS_PER_MOTOR_TORKNADO = 1440; // https://asset.pitsco.com/sharedimages/resources/torquenado_dcmotorspecifications.pdf
//    // 24 cycles per revolution, times 60:1 geared down.
//
//    static final double WHEEL_DIAMETER_INCHES = 4.9375;     // For figuring circumference
//    static final double COUNTS_PER_INCH = COUNTS_PER_MOTOR_TORKNADO / (WHEEL_DIAMETER_INCHES * Math.PI);
//
//
//    // Hack stuff.
//    private boolean madeTheRun = false;
//    private boolean useNavigation = true;
//    private boolean useNavigationDisplay = true;
//
//
//    /**
//     * Code to run ONCE when the driver hits INIT
//     */
//    @Override
//    public void init() {
//
//        if (useNavigation) {
//            /*
//             * To start up Vuforia, tell it the view that we wish to use for camera monitor (on the RC phone);
//             * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
//             */
//            int cameraMonitorViewId = 0;
//            if (useNavigationDisplay) {
//                cameraMonitorViewId =
//                        hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId",
//                                "id", hardwareMap.appContext.getPackageName());
//            }
//            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
//            parameters.vuforiaLicenseKey = VUFORIA_KEY;
//            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
//            this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
//
//            // Load the data sets that for the trackable objects. These particular data
//            // sets are stored in the 'assets' part of our application.
//            trackables = this.vuforia.loadTrackablesFromAsset("RoverRuckus");
//            blueRover = trackables.get(0);
//            blueRover.setName("Blue-Rover");
//            redFootprint = trackables.get(1);
//            redFootprint.setName("Red-Footprint");
//            frontCraters = trackables.get(2);
//            frontCraters.setName("Front-Craters");
//            backSpace = trackables.get(3);
//            backSpace.setName("Back-Space");
//            allTrackables = new ArrayList<VuforiaTrackable>();
//            allTrackables.addAll(trackables);
//
//            //initialize all lastLocation state
//            lastLocation = null;
//            lastLocationSource = lastLocationSource.Unknown;
//            lastLocationTime = 0;
//
//            if (useTestField) {
//                buildPlayfieldConnollyBasement();
//            }else {
//                buildPlayfieldREAL();
//            }
//
//            /**
//             * Create a transformation matrix describing where the phone is on the robot. Here, we
//             * put the phone on the right hand side of the robot with the screen facing in (see our
//             * choice of BACK camera above) and in landscape mode. Starting from alignment between the
//             * robot's and phone's axes, this is a rotation of -90deg along the Y axis.
//             *
//             * When determining whether a rotation is positive or negative, consider yourself as looking
//             * down the (positive) axis of rotation from the positive towards the origin. Positive rotations
//             * are then CCW, and negative rotations CW. An example: consider looking down the positive Z
//             * axis towards the origin. A positive rotation about Z (ie: a rotation parallel to the the X-Y
//             * plane) is then CCW, as one would normally expect from the usual classic 2D geometry.
//             *
//             *  .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
//             CAMERA_CHOICE == FRONT ? 90 : -90, 0, 0));
//             */
//            final float CAMERA_FORWARD_DISPLACEMENT  = 0.0f; // 110;   // eg: Camera is 110 mm in front of robot center
//            //final float CAMERA_VERTICAL_DISPLACEMENT = 14.0f * MM_PER_INCH; THIS CAUSES PROBLEMS!!!
//            final float CAMERA_VERTICAL_DISPLACEMENT = 0.0f * MM_PER_INCH;
//            final float CAMERA_LEFT_DISPLACEMENT     = 0.0f * MM_PER_INCH;
//            OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
//                    .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
//                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
//                            -90, 90, 0));
//
//            /**  Let all the trackable listeners know where the phone is.  */
//            for (VuforiaTrackable trackable : allTrackables)
//            {
//                ((VuforiaTrackableDefaultListener)trackable.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
//            }
//
//        }
//
//        initMotors();
//
//        //init internal gyroscope of hub
//        initGyroscope();
//
//        initTimeouts();
//    }
//
//    /**
//     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
//     */
//    @Override
//    public void init_loop () {
//    }
//
//    /**
//     * Code to run ONCE when the driver hits PLAY
//     */
//    @Override
//    public void start () {
//
//        if (useNavigation) {
//            /** Start tracking the data sets we care about. */
//            trackables.activate();
//        }
//
//        // Reset the game timer.
//        runtime.reset();
//    }
//
//    /**
//     * Code to run ONCE after the driver hits STOP
//     */
//    @Override
//    public void stop () {
//    }
//
//    /**
//     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
//     */
//    @Override
//    public void loop () {
//
//        if (useNavigation) {
//            /** Stop tracking the data sets we care about. */
//            trackables.deactivate();
//        }
//
//        if (madeTheRun == false) {
//            double speed = 0.5;
//
//            // forward 35 inches, turn 90degrees, forward 40 inches
//            encoderDrive(speed, 15, 15);
//            turnLeft(68);
//            encoderDrive(speed, 30, 30);
//            turnLeft(55);
//            encoderDrive(speed, 45, 45);
//
//            dropFlag();
//            sleep(3000);
//            angleHand = 0.75;
//            flagHolder.setPosition(angleHand);
//
//            turnRight(165);
//            encoderDrive(speed, 76, 76);
//
//            madeTheRun = true;
//
//        }
//
//        if (useNavigation) {
//            OpenGLMatrix nowLocation = null;
//
//            LocationSource nowSource = lastLocationSource.Unknown;
//
//            // Loop through all 4 images to see if any a re visible
//            for (VuforiaTrackable trackable : allTrackables) {
//                /**
//                 * getUpdatedRobotLocation() will return null if no new information is available since
//                 * the last time that call was made, or if the trackable is not currently visible.
//                 * getRobotLocation() will return null if the trackable is not currently visible.
//                 */
//                telemetry.addData(trackable.getName(), ((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible() ? "Visible" : "Not Visible");    //
//
//                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();
//                if (robotLocationTransform != null) {
//                    nowLocation = robotLocationTransform;
//
//                    //record which image gave us the location, in nowSource
//                    if (trackable == blueRover) {
//                        nowSource = LocationSource.Rover;
//                    }
//                    else if (trackable == this.frontCraters) {
//                        nowSource = LocationSource.Crater;
//                    }
//                    else if (trackable == this.redFootprint) {
//                        nowSource = LocationSource.Footprint;
//                    }
//                    else if (trackable == this.backSpace) {
//                        nowSource = LocationSource.Nebula;
//                    }
//                }
//
//            }
//
//            /**
//             * Provide feedback as to where the robot was last located (if we know).
//             */
//            if (nowLocation != null) {
//                lastLocation = nowLocation;
//                lastLocationTime = runtime.milliseconds();
//                lastLocationSource = nowSource;
//            }
//
//
//            // Only print out the location if it is still fresh. Note that soetimes it flickers, and it is normal to get null.
//            if ((lastLocation != null) && (runtime.milliseconds() - lastLocationTime) < 3000.0) {
//                // express position (translation) of robot in inches.
//                VectorF translation = lastLocation.getTranslation();
//                telemetry.addData("Pos", "{X, Y, Z} = %.0f, %.0f, %.0f",
//                        translation.get(0) / MM_PER_INCH, translation.get(1) / MM_PER_INCH, translation.get(2) / MM_PER_INCH);
//
//                telemetry.addData("Heading", "{Standard, Crazy} = %.0f, %.0f", getHeading(), getVuforiaHeadingRaw());
//
//            } else {
//                telemetry.addData("Pos", "Unknown");
//                lastLocation = null;
//            }
//        }
//
//        // Show the elapsed game time and wheel power.
//        telemetry.addData("Status", "time: " + runtime.toString());
//        telemetry.addData("Status", "madeTheRun=%b", madeTheRun);
//
//    }
}
