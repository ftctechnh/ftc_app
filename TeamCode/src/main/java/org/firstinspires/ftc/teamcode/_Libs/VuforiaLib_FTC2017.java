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
package org.firstinspires.ftc.teamcode._Libs;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * This library encapsulates the code needed by OpModes using the Vuforia localizer to determine
 * positioning and orientation of robot on the FTC field.
 *
 * Vuforia uses the phone's camera to inspect its surroundings, and attempt to locate target images.
 *
 * When images are located, Vuforia is able to determine the position and orientation of the
 * image relative to the camera.  This sample code then combines that information with a
 * knowledge of where the target images are on the field, to determine the location of the camera.
 * A final calculation then uses the location of the camera on the robot to determine the
 * robot's location and orientation on the field.
 *
 * This library assumes a field configuration with four targets, one under each of four beacons,
 * mounted two each on two adjacent walls of the field.
 *
 * @see VuforiaLocalizer
 * @see VuforiaTrackableDefaultListener
 * see  ftc_app/doc/tutorial/FTC_FieldCoordinateSystemDefinition.pdf
 *
 * IMPORTANT: In order to use this Library, you need to obtain your own Vuforia license key as
 * is explained below.
 */


public class VuforiaLib_FTC2017 implements HeadingSensor, LocationSensor {

    VuforiaLocalizer vuforia;
    VuforiaTrackables relicTrackables = null;
    VuforiaTrackable relicTemplate;

    RelicRecoveryVuMark mVuMark = null;
    OpenGLMatrix mLastLocation = null;

    OpMode mOpMode;

    BlockingQueue<VuforiaLocalizer.CloseableFrame> mFrameQueue;
    VuforiaLocalizer.CloseableFrame mCF;

    public void init(OpMode opMode, String licenseKey) {

        // remember this so we can do telemetry output
        mOpMode = opMode;

        /**
         * Start up Vuforia, telling it the id of the view that we wish to use as the parent for
         * the camera monitor feedback; if no camera monitor feedback is desired, use the parameterless
         * constructor instead. We also indicate which camera on the RC that we wish to use. For illustration
         * purposes here, we choose the back camera; for a competition robot, the front camera might
         * prove to be more convenient.
         *
         * Note that in addition to indicating which camera is in use, we also need to tell the system
         * the location of the phone on the robot; see phoneLocationOnRobot below.
         *
         * IMPORTANT: You should obtain your own license key to use Vuforia. The string below with which
         * 'parameters.vuforiaLicenseKey' is initialized is valid and will function, but better to get your own.
         * Vuforia will not load without a valid license being provided. Vuforia 'Development' license
         * keys, which is what is needed here, can be obtained free of charge from the Vuforia developer
         * web site at https://developer.vuforia.com/license-manager.
         *
         * Valid Vuforia license keys are always 380 characters long, and look as if they contain mostly
         * random data. As an example, here is a example of a fragment of a valid key:
         *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
         * Once you've obtained a license key, copy the string form of the key from the Vuforia web site
         * and paste it in to your code as the value of the 'vuforiaLicenseKey' field of the
         * {@link Parameters} instance with which you initialize Vuforia.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey =
                (licenseKey != null && licenseKey.length() > 0) ? licenseKey :
                "ARf809H/////AAAAGRswBQwUCUJ5nqfgZxGbDEQ8oO7YP5GdnbReYr8ZHinqQ74OsP7UdOxNZJDmhaF2OeGD20jpSexpr2CcXGSGuHXNB2p9Z6zUNLDTfEggL+yg4ujefoqdkSpCqZf1medpwh3KXcK76FcfSJuqEudik2PC6kQW/cqJXnnHofVrrDTzJmWMnK3hlqTMjig81DEPMAHbRnA5wn7Eu0irnmqqboWyOlQ0xTF+P4LVuxaOUFlQC8zPqkr1Gvzvix45paWtyuLCnS9YDWMvI1jIM4giMrTRCT0lG8F+vkuKMiK647KJp9QIsFdWQ0ecQhau3ODNQ03pcTzprVN72b9VObpv6FNBpjGKRAcA59xlZiM2l6fc";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        /**
         * Load the data sets that for the trackable objects we wish to track. These particular data
         * sets are stored in the 'assets' part of our application (you'll see them in the Android
         * Studio 'Project' view over there on the left of the screen).
         */
        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        /**
         * Create a transformation matrix describing where the phone is on the robot. Here, we
         * put the phone with the back camera facing the front of the robot (see our
         * choice of BACK camera above) and in landscape mode with the USB jack either up or down.
         * The starting alignment between the robot's and phone's axes is assumed to be phone
         * lying on the robot in portrait mode with the long axis (Y) pointing at the front of the robot.
         *
         * Rotations are right handed: i.e. positive rotations wrap each axis into the next (X-Y-Z-X)
         * in the direction of your fingers when your right thumb points down the remaining axis.
         */
        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(0, 0, 0)
                .multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.YZX,
                        // AngleUnit.DEGREES, 90, -90, 0));                // USB on top
                        AngleUnit.DEGREES, -90, 90, 0));       // USB on bottom

        /**
         * Let the trackable listeners we care about know where the phone is. We know that each
         * listener is a {@link VuforiaTrackableDefaultListener} and can so safely cast because
         * we have not ourselves installed a listener of a different type.
         */
        ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);

        // get access to video frames so we can do other processing like looking for red/blue beacons
        vuforia.setFrameQueueCapacity(3);
        mFrameQueue = vuforia.getFrameQueue();
        mCF = null;
    }

    public void start()
    {
        /** Start tracking the data sets we care about. */
        relicTrackables.activate();
    }

    public void loop()
    {
        /**
         * See if any of the instances of {@link relicTemplate} are currently visible.
         * {@link RelicRecoveryVuMark} is an enum which can have the following values:
         * UNKNOWN, LEFT, CENTER, and RIGHT. When a VuMark is visible, something other than
         * UNKNOWN will be returned by {@link RelicRecoveryVuMark#from(VuforiaTrackable)}.
         */
        mVuMark = RelicRecoveryVuMark.from(relicTemplate);
        mLastLocation = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
    }


    public void stop()
    {
        /** Stop tracking the data sets we care about. */
        relicTrackables.deactivate();
    }

    // return last recognized sign
    public RelicRecoveryVuMark getVuMark() { return mVuMark; }

    // return lastLocation matrix (may be null)
    public OpenGLMatrix getLastLocation()
    {
        return mLastLocation;
    }

    public VectorF getFieldPosition()
    {
        if (mLastLocation != null)
            return mLastLocation.getTranslation();
        else
            return null;
    }

    public Orientation getOrientation(AxesReference ref, AxesOrder order, AngleUnit unit)
    {
        if (mLastLocation != null)
            return Orientation.getOrientation(mLastLocation, ref, order, unit);
        else
            return null;
    }

    public Orientation getOrientation()
    {
        return this.getOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
    }

    public float getPitch()
    {
        return this.getOrientation().firstAngle;
    }

    public float getRoll()
    {
        return this.getOrientation().secondAngle;
    }

    public float getYaw()
    {
        return this.getOrientation().thirdAngle;    // -180 .. 0 .. +180
    }

    // implements HeadingSensor interface
    public float getHeading()
    {
       return getYaw();
    }
    public boolean haveHeading()
    {
        return (mLastLocation != null);
    }

    // implements LocationSensor interface
    public VectorF getLocation() { return getFieldPosition(); }
    public boolean haveLocation() { return (mLastLocation != null);}       // is there valid location data?

    /**
     * Some simple utilities that extract information from a transformation matrix
     * and format it in a form palatable to a human being.
     * For sanity's sake, display translations in inches rather than mm.
     */

    static public String formatPosition(OpenGLMatrix transformationMatrix) {
        //return transformationMatrix.formatAsTransform();
        VectorF translation = transformationMatrix.getTranslation();
        translation.multiply(1.0f/25.4f);       // convert from mm to inches
        return String.format("%s inches", translation.toString());
    }

    static public String formatOrientation(OpenGLMatrix transformationMatrix) {
        //return transformationMatrix.formatAsTransform();
        Orientation orientation = Orientation.getOrientation(transformationMatrix, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        return String.format("%s", orientation.toString());
    }

    // get access to video frames for other uses
    public VuforiaLocalizer.CloseableFrame getFrame()
    {
        if (mFrameQueue != null)
            mCF = mFrameQueue.poll();
        return mCF;
    }

    public void releaseFrame()
    {
        if (mCF != null)
            mCF.close();
        mCF = null;         // forget the frame
    }
}
