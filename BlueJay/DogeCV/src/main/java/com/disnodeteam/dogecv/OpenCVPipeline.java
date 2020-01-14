package com.disnodeteam.dogecv;

import android.app.Activity;
import android.content.Context;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import com.disnodeteam.dogecv.math.MathFTC;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

/**
 * Created by guinea on 6/19/17.
 * -------------------------------------------------------------------------------------
 * Copyright (c) 2018 FTC Team 5484 Enderbots
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * 
 * By downloading, copying, installing or using the software you agree to this license.
 * If you do not agree to this license, do not download, install,
 * copy or use the software.
 * -------------------------------------------------------------------------------------
 * This is a base class for an OpenCV pipeline loop. In most cases, one would want to override processFrame() with their own function.
 */

public abstract class OpenCVPipeline implements CameraBridgeViewBase.CvCameraViewListener2 {

    //Loads the OpenCV library
    static {
        try {
            System.loadLibrary("opencv_java3");
        } catch (UnsatisfiedLinkError e) {
            OpenCVLoader.loadOpenCV();
            // pass
        }
    }

    //Configurables. You probably don't need to change these.
    public static final int backCameraID = 0;
    public static final int frontCameraID = 1;

    //Set camera displacement. Use the setter methods to adjust these.
    private volatile int CAMERA_FORWARD_DISPLACEMENT  = 0;   // eg: Camera is 0 mm in front of robot center
    private volatile int CAMERA_VERTICAL_DISPLACEMENT = 0;   // eg: Camera is 0 mm above ground
    private volatile int CAMERA_LEFT_DISPLACEMENT     = 0;     // eg: Camera is ON the robot's center line

    //OpenCV-related
    protected JavaCameraView cameraView;
    protected DrawViewSource rawView;
    private volatile ViewDisplay viewDisplay;
    protected Context context;
    private boolean initStarted = false;
    private boolean inited = false;

    //DogeCV logic
    private DogeCV.CameraMode cameraMode = DogeCV.CameraMode.BACK;
    private boolean isVuforia = false;
    private boolean isVuMark = false;
    private volatile boolean isDogeCVEnabled = true;
    private volatile int cameraIndex = 0;
    private volatile boolean isEnabled = false;

    //Vuforia-related
    protected VuforiaLocalizer.Parameters parameters;
    private volatile Dogeforia dogeforia = null;
    private volatile VuforiaTrackables targetsRoverRuckus = null;
    private volatile List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
    private VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;

    /**
     * VUFORIA LICENSE. MUST BE SET BY USER!
     */
    public String VUFORIA_KEY = null; //SET THIS BEFORE CALLING INIT!

    /**
     * Initializes the OpenCVPipeline, but implicitly uses the rear camera, without Vuforia or VuMarks
     * @param context the application context, usually hardwareMap.appContext
     * @param viewDisplay the ViewDisplay that will display the underlying JavaCameraView to the screen;
     *                    in most cases, using CameraViewDisplay.getInstance() as the argument is just fine.
     */
    public void init(Context context, ViewDisplay viewDisplay) {
        init(context, viewDisplay, DogeCV.CameraMode.BACK, false, null);
    }

    /**
     * Initializes the OpenCVPipeline, assuming no webcam used. Will throw a IllegalArgumentException if one is used
     * @param context the application context, usually hardwareMap.appContext
     * @param viewDisplay the ViewDisplay that will display the underlying JavaCameraView to the screen;
     *                    in most cases, using CameraViewDisplay.getInstance() as the argument is just fine.
     * @param cameraMode Which camera is to be used, will be a DogeCV.CameraMode
     * @param findVuMarks A boolean. True to scan for vumarks, false to ignore them
     */
    public void init(Context context, ViewDisplay viewDisplay, DogeCV.CameraMode cameraMode, boolean findVuMarks) {
        init(context, viewDisplay, cameraMode, findVuMarks, null);
    }



    /**
     * Initializes the OpenCVPipeline.
     * @param context the application context, usually hardwareMap.appContext
     * @param viewDisplay the ViewDisplay that will display the underlying JavaCameraView to the screen;
     *                    in most cases, using CameraViewDisplay.getInstance() as the argument is just fine.
     * @param cameraMode Which camera is to be used, will be a DogeCV.CameraMode
     * @param findVuMarks A boolean. True to scan for vumarks, false to ignore them
     * @param webcamName The CameraName representing the webcam to be used
     */
    public void init(Context context, ViewDisplay viewDisplay, DogeCV.CameraMode cameraMode, boolean findVuMarks, CameraName webcamName) {
        this.initStarted = true;
        this.viewDisplay = viewDisplay;
        this.context = context;
        this.cameraMode = cameraMode;
        this.isVuMark = findVuMarks;
        //Sets up camera
        switch (this.cameraMode) {
            case BACK:
                this.cameraIndex = backCameraID;
                break;
            case FRONT:
                this.cameraIndex = frontCameraID;
                this.CAMERA_CHOICE = FRONT;
                break;
            case WEBCAM:
                this.isVuforia = true;
                this.cameraIndex = -1;
                if(webcamName == null) throw new IllegalArgumentException("To use the webcam, a webcame name is needed!");
                break;
        }
        if(this.isVuMark) this.isVuforia = true;
        if(isVuforia && this.VUFORIA_KEY == null) { //Checks that a license is provided.
            throw new IllegalArgumentException("No Vuforia license provided. Use detector.VUFORIA_KEY to provide one - they're freely available");
        }  else if(isVuforia) {
            //Preps Vuforia, if necessary
            parameters = new VuforiaLocalizer.Parameters(context.getResources().getIdentifier("cameraMonitorViewId", "id", context.getPackageName()));
            parameters.vuforiaLicenseKey = VUFORIA_KEY;
            parameters.cameraMonitorViewIdParent = context.getResources().getIdentifier("cameraMonitorViewId", "id", context.getPackageName());
            switch (cameraMode) { //Sets the Vuforia CameraMode
                case FRONT:
                    CAMERA_CHOICE = FRONT;
                    parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
                    break;
                case BACK:
                    CAMERA_CHOICE = BACK;
                    parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
                    break;
                case WEBCAM:
                    parameters.cameraName = webcamName;
                    break;
            }
            //Instantiates Dogeforia, and gives it access to this detector's analyzeFrame method
            dogeforia = new Dogeforia(parameters) {
                @Override
                public Mat analyzeFrame(Mat rgba, Mat gray) {
                    return processFrame(rgba, gray);
                }
            };
            dogeforia.enableConvertFrameToBitmap();
            if(isVuMark) { //Loads VuMarks, if necessary
                loadVuMarks();
            }
        }

        //Starts CV on separate thread
        final Activity activity = (Activity) context;
        final Context finalContext = context;
        final CameraBridgeViewBase.CvCameraViewListener2 self = this;
        final int cameraMoniterViewID = context.getResources().getIdentifier("cameraMonitorViewId", "id", context.getPackageName());
        //final int cameraMoniterViewID = context.getResources().getIdentifier("RelativeLayout", "id", context.getPackageName());

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // JavaCameraViews must be instantiated on a UI thread
                if(getIsVuforia()){
                    rawView = new DrawViewSource(finalContext);
                    View l = activity.findViewById(cameraMoniterViewID);
                    if(l != null) l.setVisibility(View.INVISIBLE);
                }else{
                    cameraView = new CustomCameraView(finalContext, getCameraIndex());
                    cameraView.setCameraIndex(getCameraIndex());
                    cameraView.setCvCameraViewListener(self);
                    cameraView.enableFpsMeter();
                }
                inited = true;
            }
        });
    }

    /**
     * Enables the detector.
     * This function must be called AFTER init().
     * @throws IllegalStateException if enable() is called before init()
     */
    public void enable() {
        if (!initStarted) throw new IllegalStateException("init() needs to be called before an OpenCVPipeline can be enabled!");
        // this is an absolute hack
        try {
            while (!inited) Thread.sleep(10);
        } catch (InterruptedException e) { return; }
        //Runs enabling sequence for Dogeforia
        if(isVuforia){
            final Activity activity = (Activity) context;
            final int cameraMoniterViewID = context.getResources().getIdentifier("cameraMonitorViewId", "id", context.getPackageName());
            //Updates displayed view
            viewDisplay.setCurrentView(context, rawView);
            //Gives dogeforia access to the correct View
            dogeforia.setRawView(rawView);
            //Starts dogeforia
            dogeforia.start();
            //Hides the vuforia camera feed
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ViewGroup g = (ViewGroup) activity.findViewById(cameraMoniterViewID);
                    View sl = g.getChildAt(0);
                    if(sl != null) sl.setVisibility(View.GONE);
                    if(g != null) g.setVisibility(View.GONE);

                }
            });
        }else{ //Runs if simple DogeCV
            cameraView.enableView();
            viewDisplay.setCurrentView(context, getCameraView());
        }
        this.isEnabled = true;
    }

    /**
     * Detaches the JavaCameraView from the camera and the screen, stopping OpenCV processing.
     * Be careful not to:
     *     - disable twice
     *     - disable before enabling
     * because dean kamen help you if something bad happens from that
     */
    public void disable() {
        this.isEnabled = false;
        if(isVuforia) {
            dogeforia.stop();
        } else {
            cameraView.disableView();
        }
        viewDisplay.removeCurrentView(context);
    }

    /**
     * Exposes the underlying JavaCameraView used. Before init() is called, this is null.
     * @return the JavaCameraView.
     */
    public JavaCameraView getCameraView() {
        return cameraView;
    }

    /**
     * Returns a boolean corresponding to whether this detector wil use vuforia
     * @return
     */
    public synchronized boolean getIsVuforia() {return isVuforia;}

    /**
     * Exposes the index of the camera used
     * @return
     */
    protected synchronized int getCameraIndex() {return cameraIndex;}

    /**
     * This function is called when the camera is started; overriding this may be useful to set the
     * maximum width and height parameters of an image processing pipeline.
     * @param width -  the width of the frames that will be delivered
     * @param height - the height of the frames that will be delivered
     */
    @Override
    public void onCameraViewStarted(int width, int height) {}

    /**
     * Override this function if there should be logic on camera close.
     */
    @Override
    public void onCameraViewStopped() {}

    /**
     * The method that calls {@link #processFrame(Mat, Mat)}; there's little reason to override this, if ever.
     * @param inputFrame the input frame given by the internal JavaCameraView
     * @return the result of {@link #processFrame(Mat, Mat)}
     */
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = new Mat();
        Mat gray = new Mat();

        switch (((Activity) context).getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
                // These methods don't work. Please tell Levi if you ever find yourself needing to use them!
                Core.rotate(inputFrame.rgba(), rgba, Core.ROTATE_90_CLOCKWISE);
                Core.rotate(inputFrame.gray(), gray, Core.ROTATE_90_CLOCKWISE);
                break;
            case Surface.ROTATION_90:
                rgba = inputFrame.rgba();
                gray = inputFrame.gray();
                break;
            case Surface.ROTATION_270:
                // These methods don't work. Please tell Levi if you ever find yourself needing to use them!
                Core.rotate(inputFrame.rgba(), rgba, Core.ROTATE_180);
                Core.rotate(inputFrame.gray(), gray, Core.ROTATE_180);
                break;
        }
        if(isDogeCVEnabled) return processFrame(rgba, gray);
        else return rgba;
    }

    /**
     * Override this with the main image processing logic. This is run every time the camera receives a frame.
     * @param rgba a {@link Mat} that is in RGBA format
     * @param gray a {@link Mat} that is already grayscale
     * @return the Mat that should be displayed to the screen; in most cases one would probably just want to return rgba
     */
    public abstract Mat processFrame(Mat rgba, Mat gray);

    /**
     * Loads VuMarks from file and configures their positions on the field. There should never be a reason to mess with this.
     */
    private void loadVuMarks() {
        targetsRoverRuckus = this.dogeforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blueRover = targetsRoverRuckus.get(0);
        blueRover.setName("Blue-Rover");
        VuforiaTrackable redFootprint = targetsRoverRuckus.get(1);
        redFootprint.setName("Red-Footprint");
        VuforiaTrackable frontCraters = targetsRoverRuckus.get(2);
        frontCraters.setName("Front-Craters");
        VuforiaTrackable backSpace = targetsRoverRuckus.get(3);
        backSpace.setName("Back-Space");

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        allTrackables.addAll(targetsRoverRuckus);

        // Set trackables' location on field
        OpenGLMatrix blueRoverLocationOnField = OpenGLMatrix
                .translation(0, MathFTC.mmFTCFieldWidth, MathFTC.mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0));
        blueRover.setLocation(blueRoverLocationOnField);

        OpenGLMatrix redFootprintLocationOnField = OpenGLMatrix
                .translation(0, -MathFTC.mmFTCFieldWidth, MathFTC.mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180));
        redFootprint.setLocation(redFootprintLocationOnField);

        OpenGLMatrix frontCratersLocationOnField = OpenGLMatrix
                .translation(-MathFTC.mmFTCFieldWidth, 0, MathFTC.mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90));
        frontCraters.setLocation(frontCratersLocationOnField);

        OpenGLMatrix backSpaceLocationOnField = OpenGLMatrix
                .translation(MathFTC.mmFTCFieldWidth, 0, MathFTC.mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90));
        backSpace.setLocation(backSpaceLocationOnField);

        // Set phone location on robot
        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
                        CAMERA_CHOICE == FRONT ? 90 : -90, 0, 0));

        //Set info for the trackables
        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener)trackable.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        }

        targetsRoverRuckus.activate();
    }

}