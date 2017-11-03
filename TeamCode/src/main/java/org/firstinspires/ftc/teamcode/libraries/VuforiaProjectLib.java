package org.firstinspires.ftc.teamcode.libraries;

import android.support.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.vuforia.CameraCalibration;
import com.vuforia.Matrix34F;
import com.vuforia.Tool;
import com.vuforia.Vec3F;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaLocalizerImpl;
import org.firstinspires.ftc.teamcode.opmodes.demo.VumarkOpenCV;
import org.opencv.core.Point;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Noah on 11/3/2017.
 * Library to handle the vumark and projection of points onto camera image
 * Also includes openCV functionality
 */

public abstract class VuforiaProjectLib extends OpenCVLib {
    //vuforia objects
    protected VuforiaLocalizerShim vuforia;
    protected VuforiaTrackable relicTemplate;
    protected CameraCalibration camCal;

    //load vuforia libraries and configure
    protected CameraCalibration initVuforia() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AZPbGaP/////AAAAGcIykH/KO0QNvZGSYxc0fDlVytYrk0HHv6OLmjHsswRvi/1l9RZCkepChaAZup3DIJlrjK2BV57DEz/noNO0oqT9iu2moP/svGmJ+pBG7FlfF4RHxu6UhvVLaKUZCsTJ1zTkd7XnMuRw8aSuIxowOiLJQYcgjmddi11LG26lAr6aRmoWJzr2pv6Yui2Gom0wt9J4+1g3kXqjngnH3h6NPA/6aUfpVngFaFPp5knyDJWZT88THttPsqcKW41QC/qgNh3CHIdADu15Rm51JNRlvG+2+sYstiHeHFQqCDwUkTgWor0v/Bk+xXoj3oUCb4REwT9w94E/VEI4qEAFPpmeo6YgxQ4LLFknu6tgNy8xdD6S";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.NONE;
        //bwahaha java shim
        this.vuforia = new VuforiaLocalizerShim(parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadShimTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        return this.camCal = vuforia.getCameraCalibration();
    }

    //start vuforia tracking
    protected void startVuforia() {
        relicTemplate.getTrackables().activate();
    }

    protected RelicRecoveryVuMark isTracking() {
        return RelicRecoveryVuMark.from(relicTemplate);
    }

    protected OpenGLMatrix getPose() {
        return ((VuforiaDefaultListenerShim) relicTemplate.getListener()).getPose();
    }

    protected Point[] getProjectedPoints(Vec3F[] points) {
        Matrix34F realPose = ((VuforiaDefaultListenerShim) relicTemplate.getListener()).getRealPose();
        Point[] ret = new Point[points.length];

        for(int i = 0; i < points.length; i++) {
            float[] imagePoint = Tool.projectPoint(this.camCal, realPose, points[i]).getData();
            ret[i] = new Point(imagePoint[0], imagePoint[1]);
        }

        return ret;
    }

    protected void stopVuforia() {
        this.vuforia.stop();
    }

    /**
     * I'm so sorry
     * Shims created so I can access the pose in vuforia matrix form
     * they are protected variables, so I need to java fu my way into them
     */
    //start from the very begining...
    //vuforia localizer shim
    private static class VuforiaLocalizerShim extends VuforiaLocalizerImpl {
        //constructor
        VuforiaLocalizerShim(VuforiaLocalizer.Parameters params){
            super(params);
        }
        //the shim part
        public VuforiaTrackables loadShimTrackablesFromAsset(String assetName){
            return loadTrackablesFromAsset(assetName, VuforiaDefaultListenerShim.class);
        }

        public void stop() {
            //god damn quallcomm
            this.stopAR();
        }
    }
    //vuforia default listener shim
    public static class VuforiaDefaultListenerShim extends VuforiaTrackableDefaultListener {
        //constructor shim
        public VuforiaDefaultListenerShim(VuforiaTrackable trackable) {
            super(trackable);
        }
        //and the jesus data gettums
        @Nullable
        public synchronized Matrix34F getRealPose() {
            return this.currentPose;
        }
    }
}
