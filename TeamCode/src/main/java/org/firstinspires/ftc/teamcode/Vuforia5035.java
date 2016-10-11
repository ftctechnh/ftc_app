package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;



/*
 * This OpMode was written for the Vuforia Basics video. This demonstrates basic principles of
 * using Vuforia in FTC.
 */
@Autonomous(name = "Vuforia5035")
public class Vuforia5035 extends LinearOpMode
{
        public static final String TAG = "Vuforia Sample";

        OpenGLMatrix lastLocation = null;

        /**
         * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
         * localization engine.
         */
        VuforiaLocalizer vuforia;

        @Override public void runOpMode() throws InterruptedException {
            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
            parameters.vuforiaLicenseKey = "AfXb48r/////AAAAGeD2f/Vqr091sIpDI7RLaYYXDM4Ao03klW8aOZpnKhW7owlW94atv0FpmrIMSu8f15XxGzIXZa9xjWrEw+Cqnea2mZE/FuHbD6WUGnU1Mwyy8CzejVRQV0dTu2Y/KuS9nxcCMcMDKnH3OZjFZYJLPgJ3TqqgL47MkEszN/iS8LKg82rPhB81mh3t5c7ZohzPRNDhvrgUOQHruNu+7YcjilNMbtqBGutFkNxJ5qSbA1WajcXwIrgMwvQFDMnr3O1kqo5Mks4lYReyNvczQ4I7TZuRtqox4SzZf9hJN7EfuOGVwRX8YdOTyMMOnekK7lJSNbdydaTQA3ye0eLxO90kOX1zOhexEzGO9WPFiG3hN/s4";
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
            this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

            VuforiaTrackables FTCFieldtrackables = this.vuforia.loadTrackablesFromAsset("FTC_2016-17");
            VuforiaTrackable WheelTarget = FTCFieldtrackables.get(0);
            WheelTarget.setName("wheeltarget");  // wheels

            VuforiaTrackable ToolTarget  = FTCFieldtrackables.get(1);
            ToolTarget.setName("tooltarget");  // tools

            VuforiaTrackable LegosTarget  = FTCFieldtrackables.get(2);
            LegosTarget.setName("legostarget");  // tools

            VuforiaTrackable GearsTarget  = FTCFieldtrackables.get(3);
            GearsTarget.setName("gearstarget");  // tools

            /** For convenience, gather together all the trackable objects in one easily-iterable collection */
            List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
            allTrackables.addAll(FTCFieldtrackables);

            /**
             * We use units of mm here because that's the recommended units of measurement for the
             * size values specified in the XML for the ImageTarget trackables in data sets. E.g.:
             *      <ImageTarget name="stones" size="247 173"/>
             * You don't *have to* use mm here, but the units here and the units used in the XML
             * target configuration files *must* correspond for the math to work out correctly.
             */
            float mmPerInch        = 25.4f;
            float mmBotWidth       = 18 * mmPerInch;            // ... or whatever is right for your robot
            float mmFTCFieldWidth  = (12*12 - 2) * mmPerInch;   // the FTC field is ~11'10" center-to-center of the glass panels

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
            OpenGLMatrix wheelsTargetLocationOnField = OpenGLMatrix
                /* Then we translate the target off to the RED WALL. Our translation here
                is a negative translation in X.*/
                    .translation(12 * mmPerInch, 6 * 12 * mmPerInch, 6 * mmPerInch)
                    .multiplied(Orientation.getRotationMatrix(
                        /* First, in the fixed (field) coordinate system, we rotate 90deg in X, then 90 in Z */
                            AxesReference.EXTRINSIC, AxesOrder.XYZ,
                            AngleUnit.DEGREES, 90, 90, 0));
            WheelTarget.setLocation(wheelsTargetLocationOnField);
            RobotLog.ii(TAG, "wheels=%s", format(wheelsTargetLocationOnField));

       /*
        * To place the Stones Target on the Blue Audience wall:
        * - First we rotate it 90 around the field's X axis to flip it upright
        * - Finally, we translate it along the Y axis towards the blue audience wall.
        */
            OpenGLMatrix toolsTargetLocationOnField = OpenGLMatrix
                /* Then we translate the target off to the Blue Audience wall.
                Our translation here is a positive translation in Y.*/
                    .translation(-6*12*mmPerInch, 3*12*mmPerInch, 6*mmPerInch)
                    .multiplied(Orientation.getRotationMatrix(
                        /* First, in the fixed (field) coordinate system, we rotate 90deg in X */
                            AxesReference.EXTRINSIC, AxesOrder.XYZ,
                            AngleUnit.DEGREES, 0, 90, 0));
            GearsTarget.setLocation(toolsTargetLocationOnField);
            RobotLog.ii(TAG, "tools=%s", format(toolsTargetLocationOnField));


            OpenGLMatrix legosTargetLocationOnField= OpenGLMatrix
                /* Then we translate the target off to the Blue Audience wall.
                Our translation here is a positive translation in Y.*/
                    .translation(-3*12*mmPerInch, 6*12*mmPerInch, 6*mmPerInch)
                    .multiplied(Orientation.getRotationMatrix(
                        /* First, in the fixed (field) coordinate system, we rotate 90deg in X */
                            AxesReference.EXTRINSIC, AxesOrder.XYZ,
                            AngleUnit.DEGREES, -90, 90, 0));
            LegosTarget.setLocation(legosTargetLocationOnField);
            RobotLog.ii(TAG, "tools=%s", format(legosTargetLocationOnField));

            OpenGLMatrix gearsTargetLocationOnField = OpenGLMatrix
                /* Then we translate the target off to the Blue Audience wall.
                Our translation here is a positive translation in Y.*/
                    .translation(-6*12*mmPerInch, -1*12*mmPerInch, 6*mmPerInch)
                    .multiplied(Orientation.getRotationMatrix(
                        /* First, in the fixed (field) coordinate system, we rotate 90deg in X */
                            AxesReference.EXTRINSIC, AxesOrder.XYZ,
                            AngleUnit.DEGREES, 0, 90, 0));
            GearsTarget.setLocation(gearsTargetLocationOnField);
            RobotLog.ii(TAG, "tools=%s", format(gearsTargetLocationOnField));


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
             */

            OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                    .translation(0,0,0)
                    .multiplied(Orientation.getRotationMatrix(
                            AxesReference.EXTRINSIC, AxesOrder.XYZ,
                            AngleUnit.DEGREES, 0, 0, 0));
            RobotLog.ii(TAG, "phone=%s", format(phoneLocationOnRobot));

            /**
             * Let the trackable listeners we care about know where the phone is. We know that each
             * listener is a {@link VuforiaTrackableDefaultListener} and can so safely cast because
             * we have not ourselves installed a listener of a different type.
             */
            ((VuforiaTrackableDefaultListener)WheelTarget.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
            ((VuforiaTrackableDefaultListener)GearsTarget.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
            ((VuforiaTrackableDefaultListener)LegosTarget.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
            ((VuforiaTrackableDefaultListener)ToolTarget.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);

            /**
             * A brief tutorial: here's how all the math is going to work:
             *
             * C = phoneLocationOnRobot  maps   phone coords -> robot coords
             * P = tracker.getPose()     maps   image target coords -> phone coords
             * L = redTargetLocationOnField maps   image target coords -> field coords
             *
             * So
             *
             * C.inverted()              maps   robot coords -> phone coords
             * P.inverted()              maps   phone coords -> imageTarget coords
             *
             * Putting that all together,
             *
             * L x P.inverted() x C.inverted() maps robot coords to field coords.
             *
             * @see VuforiaTrackableDefaultListener#getRobotLocation()
             */

            /** Wait for the game to begin */
            telemetry.addData(">", "Press Play to start tracking");
            telemetry.update();
            waitForStart();

            /** Start tracking the data sets we care about. */
            FTCFieldtrackables.activate();

            while (opModeIsActive()) {

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
                } else {
                    telemetry.addData("Pos", "Unknown");
                }
                telemetry.update();
                idle();
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
