package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Shane on 11/19/2017.
 */

public class UseVuforia {
    VuforiaTrackables relicTrackables;
    VuforiaTrackable relicTemplate;

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the UseVuforia
     * localization engine.
     */
    VuforiaLocalizer vuforia;

    HardwareMap hardwareMap;
    Telemetry telemetry;

    public UseVuforia(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }

    public void init() {
        /**
         * To start up UseVuforia, tell it the view that we wish to use
         * for camera monitor (on the RC phone);
         * If no camera monitor is desired, use the
         * parameterless constructor instead (commented out below).
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(
                cameraMonitorViewId);

        // OR...  Do Not Activate the Camera Monitor View, to save power
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        /**
         * Key from: https://developer.vuforia.com/license-manager
         * email: spmcenaney@outlook.com
         * password: 6168Robot
         */
        parameters.vuforiaLicenseKey = "AascJmD/////AAAAGWifnzO6BEaKhxz1Z2lm3H4H/iy2UJMv6UO24RZN" +
                "bfht4hcdeBy7Gj4x9ub3O7yucyYoAC++W374mETWgqgkkvDnIQQf/rSTi7ONVxUSn1HyWFV1ladeY6e" +
                "ZwJ24SvpFyZPkzYk1XtetE9pixH6us3xnKwtiCygQJBUMaTWCGShKFBJemLh88WGJ6teByIDWLpIQqf" +
                "56wFRAjefr+Rkzt2x+MpB4ONSioBHpcKnsoBl9n+fbDSfE7wgsduoVPamKqiYO0XxJjLN4KmjLtloEu" +
                "PDNzO4uKA3IaAiTX0Q227+Sk6Q562VVoLnjHjtHDDzpfN4KPRnKV5LrSV+P149LfFLOK6oMWRiN7Ucx" +
                "5MnL2o4u";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        /**
         * Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
         * in this data set: all three of the VuMarks in the game were created from this one
         * template, but differ in their instance id information.
         * @see VuMarkInstanceId
         */
        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging
    }

    public void start() {relicTrackables.activate();}

    public boolean run() {
        boolean returnable = false;
        /**
         * See if any of the instances of {@link relicTemplate} are currently visible.
         * {@link RelicRecoveryVuMark} is an enum which can have the following values:
         * UNKNOWN, LEFT, CENTER, and RIGHT. When a VuMark is visible, something other than
         * UNKNOWN will be returned by {@link RelicRecoveryVuMark#from(VuforiaTrackable)}.
         */
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

            /**
             * Found an instance of the template. In the actual game, you will probably
             * loop until this condition occurs, then move on to act accordingly depending
             * on which VuMark was visible.
             */
            telemetry.addData("VuMark", "%s visible", vuMark);

            /**
             * For fun, we also exhibit the navigational pose. In the Relic Recovery game,
             * it is perhaps unlikely that you will actually need to act on this pose information,
             * but we illustrate it nevertheless, for completeness.
             */
            OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener())
                    .getPose();
            telemetry.addData("Pose", format(pose));

            /**
             * We further illustrate how to decompose the pose into useful rotational and
             * translational components
             */
            if (pose != null) {
                VectorF trans = pose.getTranslation();
                Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC,
                        AxesOrder.XYZ, AngleUnit.DEGREES);

                // Extract the X, Y, and Z components of the offset of the target relative to robot
                double tX = trans.get(0);
                double tY = trans.get(1);
                double tZ = trans.get(2);

                // Extract the rotational components of the target relative to the robot
                double rX = rot.firstAngle;
                double rY = rot.secondAngle;
                double rZ = rot.thirdAngle;
            }
            returnable = true;
        } else {
            telemetry.addData("VuMark", "not visible");
        }
        return returnable;
    }
    /**
     * A simple utility that extracts positioning information from a transformation matrix
     * and formats it in a form palatable to a human being.
     */
    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }

    public VuforiaLocalizer getVuforia() {
        return vuforia;
    }


}
