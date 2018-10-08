package com.disnodeteam.dogecv;

import android.app.Activity;
import android.content.Context;
import android.view.Surface;
import android.view.View;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Core;
import org.opencv.core.Mat;

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
    static {
        try {
            System.loadLibrary("opencv_java3");
        } catch (UnsatisfiedLinkError e) {
            OpenCVLoader.loadOpenCV();
            // pass
        }
    }
    protected JavaCameraView cameraView;
    protected DrawViewSource rawView;
    private ViewDisplay viewDisplay;
    protected Context context;
    private boolean initStarted = false;
    private boolean inited = false;
    private boolean isVuforia = false;

    /**
     * Initializes the OpenCVPipeline, but implicitly uses the rear camera.
     * @param context the application context, usually hardwareMap.appContext
     * @param viewDisplay the ViewDisplay that will display the underlying JavaCameraView to the screen;
     *                    in most cases, using CameraViewDisplay.getInstance() as the argument is just fine.
     */
    public void init(Context context, ViewDisplay viewDisplay) {
        init(context, viewDisplay, 0, false);
    }

    /**
     * Initializes the OpenCVPipeline.
     * @param context the application context, usually hardwareMap.appContext
     * @param viewDisplay the ViewDisplay that will display the underlying JavaCameraView to the screen;
     *                    in most cases, using CameraViewDisplay.getInstance() as the argument is just fine.
     * @param cameraIndex The index of the camera to use. On every FTC-legal phone (afaik) 0 is the back camera, and 1 is the front camera.
     */
    public void init(Context context, ViewDisplay viewDisplay, final int cameraIndex, final boolean isVuforia) {
        this.initStarted = true;
        this.viewDisplay = viewDisplay;
        this.context = context;
        this.isVuforia = isVuforia;
        final Activity activity = (Activity) context;
        final Context finalContext = context;
        final CameraBridgeViewBase.CvCameraViewListener2 self = this;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // JCVs must be instantiated on a UI thread
                if(isVuforia){
                    rawView = new DrawViewSource(finalContext);
                }else{
                    cameraView = new CustomCameraView(finalContext, cameraIndex);
                    cameraView.setCameraIndex(cameraIndex);
                    cameraView.setCvCameraViewListener(self);
                    cameraView.enableFpsMeter();

                }
                inited = true;
            }
        });
    }

    /**
     * Attaches the underlying JavaCameraView to the screen and the camera using the set {@link ViewDisplay} to do so, essentially starting OpenCV processing.
     * This function must be called after init().
     * @throws IllegalStateException if enable() is called before init()
     */
    public void enable() {
        if (!initStarted) throw new IllegalStateException("init() needs to be called before an OpenCVPipeline can be enabled!");
        // this is an absolute hack
        try {
            while (!inited) Thread.sleep(10);
        } catch (InterruptedException e) { return; }

        if(isVuforia){
            viewDisplay.setCurrentView(context, rawView);

        }else{
            cameraView.enableView();
            viewDisplay.setCurrentView(context, getCameraView());
        }
    }



    /**
     * Detaches the JavaCameraView from the camera and the screen, stopping OpenCV processing.
     * Be careful not to:
     *     - disable twice
     *     - disable before enabling
     * because dean kamen help you if something bad happens from that
     */
    public void disable() {

        if(!isVuforia){
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

    public DrawViewSource getRawView(){return rawView;}

    /**
     * This function is called when the camera is started; overriding this may be useful to set the
     * maximum width and height parameters of an image processing pipeline.
     * @param width -  the width of the frames that will be delivered
     * @param height - the height of the frames that will be delivered
     */
    @Override
    public void onCameraViewStarted(int width, int height) {
        // override this if you wish
    }

    /**
     * Override this function if there should be logic on camera close.
     */
    @Override
    public void onCameraViewStopped() {

    }

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
                // this breaks horribly for some reason
                Core.rotate(inputFrame.rgba(), rgba, Core.ROTATE_90_CLOCKWISE);
                Core.rotate(inputFrame.gray(), gray, Core.ROTATE_90_CLOCKWISE);
                break;
            case Surface.ROTATION_90:
                rgba = inputFrame.rgba();
                gray = inputFrame.gray();
                break;
            case Surface.ROTATION_270:
                Core.rotate(inputFrame.rgba(), rgba, Core.ROTATE_180);
                Core.rotate(inputFrame.gray(), gray, Core.ROTATE_180);
                break;
        }
        return processFrame(rgba, gray);
    }

    /**
     * Override this with the main image processing logic. This is run every time the camera recieves a frame.
     * @param rgba a {@link Mat} that is in RGBA format
     * @param gray a {@link Mat} that is already grayscale
     * @return the Mat that should be displayed to the screen; in most cases one would probably just want to return rgba
     */
    public abstract Mat processFrame(Mat rgba, Mat gray);
}
