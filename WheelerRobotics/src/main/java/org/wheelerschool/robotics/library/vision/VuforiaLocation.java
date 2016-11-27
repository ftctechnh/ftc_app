/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/*
Edits by The Wheeler School FTC Robotics Team
 */
package org.wheelerschool.robotics.library.vision;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.wheelerschool.robotics.config.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * =================================================================================================
 * NOTE: Make sure to set 'VUFORIA_KEY' in 'org.wheelerschool.robotics.config.Config' to your Vuforia key
 * =================================================================================================
 */

/**
 * Library to find the robot locations on the field using "VuforiaLocalizer"
 *
 * @see org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaNavigation
 * @see VuforiaLocalizer
 * @see VuforiaTrackableDefaultListener
 * see  ftc_app/doc/tutorial/FTC_FieldCoordinateSystemDefinition.pdf
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 *
 * @author luciengaitskell
 * @version 1.0
 */


public class VuforiaLocation {
    // DEFAULT VARIABLES:
    public static float MM_PER_INCH = 25.4f;
    private static float MM_FTC_FIELD_WIDTH = (12 * 12 - 2) * MM_PER_INCH;
    public static float DEFAULT_FTC_FIELD_SCALE = 1.1f;

    // Variables:
    public static final String TAG = "Vuforia Location";
    private final float scale;
    private float mmFieldWidth = MM_FTC_FIELD_WIDTH;

    //  Vuforia:
    VuforiaLocalizer vuforia;
    VuforiaTrackables FTCVisionTargets = null;
    Map<String, VuforiaTrackable> allTrackables = null;


    public OpenGLMatrix lastLocation = null;
    public VectorF lastLocationXYZ = null;
    public Orientation lastRotationXYZ = null;
    public Map<String, Boolean> lastTrackableData = null;


    public VuforiaLocation(OpenGLMatrix phoneLocationOnRobot) {
        /**
         * Create instance using default field scaling.
         */
        this(DEFAULT_FTC_FIELD_SCALE, phoneLocationOnRobot);
    }

    public VuforiaLocation(float scale, OpenGLMatrix phoneLocationOnRobot) {
        /**
         * Create instance with custom field scaling.
         */

        RobotLog.ii(TAG, "phone=%s", format(phoneLocationOnRobot));
        // Variable Setup:
        this.scale = scale;  // Set the scale of the field / targets

        // Vuforia Setup:
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = Config.VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        // Load trackables:
        this.FTCVisionTargets = this.vuforia.loadTrackablesFromAsset("FTC_2016-17");

        // List of targets with names:
        List<String> targets = new ArrayList<>(); // Wheels Legos Tools Gears
        targets.add("wheels");
        targets.add("tools");
        targets.add("legos");
        targets.add("gears");


        // Iterate through trackables and set names and other data:
        this.allTrackables = new HashMap<>();
        for (int ii=0; ii < targets.size(); ii++) {
            String name = targets.get(ii);
            VuforiaTrackable trackableObject = this.setUpTrackable(ii, name);
            trackableObject.setLocation(getTargetLocation(name));

            this.allTrackables.put(name, trackableObject);
            ((VuforiaTrackableDefaultListener) trackableObject.getListener())
                    .setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        }

            // Start tracking the data sets we care about
            this.FTCVisionTargets.activate();
    }

    private VuforiaTrackable setUpTrackable(int trackableNumb, String trackableName){
        /**
         * Get a trackable by name and set the name
         */
        VuforiaTrackable trackable = this.FTCVisionTargets.get(trackableNumb);
        trackable.setName(trackableName);
        return trackable;
    }

    public void readData() {
        /**
         * Read the robot location data and set the object variables with the read data.
         */
        OpenGLMatrix lastLocation = null;
        OpenGLMatrix robotLocationTransform = null;
        Map<String, Boolean> trackableData = new HashMap<>();

        for (Map.Entry<String, VuforiaTrackable> trackableEntry : this.allTrackables.entrySet()) {
            VuforiaTrackable trackable = trackableEntry.getValue();

            trackableData.put(trackable.getName(), ((VuforiaTrackableDefaultListener) trackable
                                                    .getListener()).isVisible());

            robotLocationTransform = ((VuforiaTrackableDefaultListener)trackable
                                                   .getListener()).getUpdatedRobotLocation();
            if (robotLocationTransform != null) {
                lastLocation = robotLocationTransform;
            }
        }

        this.lastTrackableData = trackableData;
        this.lastLocation = lastLocation;

        this.lastLocationXYZ = (lastLocation == null ? null : lastLocation.getTranslation());
        this.lastRotationXYZ = (lastLocation == null ? null : Orientation.getOrientation(lastLocation, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS));
    }

    private OpenGLMatrix getTargetLocation(String targetName) {
        /**
         * Get the target location based on the name.
         */

        // Make sure name is lower case:
        targetName = targetName.toLowerCase();

        // Set default target location value:
        OpenGLMatrix target = null;

        // Wheels target:
        if (targetName == "wheels") {
            target = OpenGLMatrix
                    /* Wheels Target
                    * Blue side, closest to corner vortex. */
                    .translation(mmFieldWidth / 12, mmFieldWidth / 2, 0)
                    .multiplied(Orientation.getRotationMatrix(
                            AxesReference.EXTRINSIC, AxesOrder.XZX,
                            AngleUnit.DEGREES, 90, 0, 0));
        }

        // Legos target:
        else if (targetName == "legos") {
            target = OpenGLMatrix
                    /* Legos Target
                    * Blue side, furthest from corner vortex. */
                    .translation(-mmFieldWidth * 3 / 12, mmFieldWidth / 2, 0)
                    .multiplied(Orientation.getRotationMatrix(
                            AxesReference.EXTRINSIC, AxesOrder.XZX,
                            AngleUnit.DEGREES, 90, 0, 0));
        }

        // Gears target:
        else if (targetName == "gears") {
            // Red Side Targets:
            target = OpenGLMatrix
                    /* Gears Target
                    * Red side, closest to corner vortex. */
                    .translation(-mmFieldWidth / 2, -mmFieldWidth / 12, 0)
                    .multiplied(Orientation.getRotationMatrix(
                            AxesReference.EXTRINSIC, AxesOrder.XZX,
                            AngleUnit.DEGREES, 90, 90, 0));
        }

        // Tools target:
        else if (targetName == "tools") {
            target = OpenGLMatrix
                    /* Tools Target
                    * Red side, furthest from corner vortex. */
                    .translation(-mmFieldWidth / 2, mmFieldWidth * 3/ 12, 0)
                    .multiplied(Orientation.getRotationMatrix(
                            AxesReference.EXTRINSIC, AxesOrder.XZX,
                            AngleUnit.DEGREES, 90, 90, 0));
        }

        // Scale target:
        target.scale(this.scale);
        return target;
    }

    /**
     * A simple utility that extracts positioning information from a transformation matrix
     * and formats it in a form palatable to a human being.
     */
    String format(OpenGLMatrix transformationMatrix) {
        return transformationMatrix.formatAsTransform();
    }
}
