/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-10-30

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
@file:Suppress("PackageDirectoryMismatch", "ClassName")
package org.directcurrent.opencv

import android.app.Activity
import android.util.Log
import android.view.View
import com.qualcomm.ftcrobotcontroller.R
import org.directcurrent.opencv.visionprocessors.BrownGlyphFinder
import org.directcurrent.opencv.visionprocessors.GrayGlyphFinder
import org.directcurrent.opencv.visionprocessors.VisionProcessor
import org.opencv.android.*
import org.opencv.core.Mat


/**
 * Kotlin class for managing OpenCV
 *
 * Controls very basic functionality of OpenCV- doesn't do any processing of any kind
 * Readies OpenCV for use and manages its state depending on activity state.
 *
 * Default Constructor:
 * Takes the main activity of the FTC App and implicitly uses the back camera
 */
class OpenCVRunner constructor(var mainActivity: Activity , var cameraIndex: Int) : CameraBridgeViewBase.CvCameraViewListener2
{
    private var _jCamView: JavaCameraView? = null       // Camera View in main activity

    private var _loaderCallBack =                       // Used when activity state changes
    object : BaseLoaderCallback(mainActivity)
    {
        override fun onManagerConnected(status: Int)
        {
            when (status)
            {
                BaseLoaderCallback.SUCCESS -> _jCamView?.enableView()

                else -> super.onManagerConnected(status)
            }

            super.onManagerConnected(status)
        }
    }

    private var _visionProcessors = ArrayList<VisionProcessor>()

    private var _analyze = false


    /**
     * Starts OpenCV- starting messages are displayed in Logcat. OpenCV likes to fail the first
     * time and then start working- don't worry about that.
     *
     * Be sure to call this before doing anything else with OpenCV, otherwise your robot
     * might explode. We don't want that now, do we?
     */
    fun start()
    {
        if(!OpenCVLoader.initDebug())
        {
            Log.e(this.javaClass.simpleName, "  OpenCVLoader.initDebug(), not working.")
        }
        else
        {
            Log.d(this.javaClass.simpleName, "  OpenCVLoader.initDebug(), working.")
        }

        mainActivity.runOnUiThread {
            _jCamView = mainActivity.findViewById(R.id.java_camera_view) as JavaCameraView
            _jCamView!!.setCameraIndex(cameraIndex)
            _jCamView!!.setCvCameraViewListener(this@OpenCVRunner)
        }

        // Add our vision processors
        _visionProcessors.add(GrayGlyphFinder())
        _visionProcessors.add(BrownGlyphFinder())
    }


    /**
     * Enables the Java Camera View (the vision thingy in the middle of the FTC app).
     *
     * In your main Activity, call this in:
     *
     * onStart()
     * onResume()
     */
    fun enableView()
    {
        if (!OpenCVLoader.initDebug())
        {
            Log.e(this.javaClass.simpleName, "  OpenCVLoader.initDebug(), not working.")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, mainActivity, _loaderCallBack)
        }
        else
        {
            Log.d(this.javaClass.simpleName, "  OpenCVLoader.initDebug(), working.")
            _loaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }


    /**
     * Disables the Java Camera View to prevent it from running when not needed.
     *
     * In your main Activity, call this in:
     *
     * onStop()
     * onDestroy()
     * onPause()
     */
    fun disableView()
    {
        _jCamView?.disableView()
    }


    /**
     * QOL function that shows the Java Camera View in your main activity layout. Also enables
     * the Java Camera View
     */
    fun enableCameraView()
    {
        _jCamView?.visibility = View.VISIBLE
        enableView()
    }


    /**
     * QOL function that hides the Java Camera View in your main activity layout. Also disables
     * the Java Camera View
     */
    fun disableCameraView()
    {
        _jCamView?.visibility = View.GONE
        disableView()
    }


    /**
     * Toggles whether to perform vision analysis on frame or not
     */
    fun toggleAnalyze()
    {
        _analyze = !_analyze
    }


    /**
     * Returns whether or not analysis in enabled
     */
    fun analysisEnabled(): Boolean
    {
        return _analyze
    }


    /**
     * Initializes Mats of all vision processors
     */
    override fun onCameraViewStarted(width: Int, height: Int)
    {
        for(i in _visionProcessors)
        {
            i.initMats(width , height)
        }
    }


    /**
     * Releases Mats of all vision processors
     */
    override fun onCameraViewStopped()
    {
        for(i in _visionProcessors)
        {
            i.releaseMats()
        }
    }


    /**
     * Runs each vision processor in order added to list and returns end result
     */
    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat?
    {
        var processedFrame = inputFrame?.rgba()

        if(_analyze)
        {
            for(i in _visionProcessors)
            {
                processedFrame = i.processFrame(processedFrame)
            }
        }

        return processedFrame
    }
}