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


import android.util.Log
import android.view.View
import com.qualcomm.ftcrobotcontroller.R
import org.directcurrent.opencv.visionprocessors.VisionProcessor
import org.directcurrent.opencv.visionprocessors.objectfinders.BlueJewelFinder
import org.directcurrent.opencv.visionprocessors.objectfinders.RedJewelFinder
import org.firstinspires.ftc.robotcontroller.internal.Core.OpenCV.VisionProcessors.VirtualJewelArm
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity
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
@Suppress("FunctionName")
class OpenCVRunner constructor(var mainActivity: FtcRobotControllerActivity ,
                   private var _cameraIndex: Int): CameraBridgeViewBase.CvCameraViewListener2
{
    /**
     * Constructor, initializes the bridge
     */
    init
    {
        CVBridge.openCvRunner = this
    }


    /**
     * Used in the OpenCV state machine that determines whether the display is open/closed, etc.
     */
    private enum class _CVState
    {
        HIDDEN ,
        SHOW_ANALYZE ,
        SHOW
    }

    private var _state = _CVState.SHOW   // We start with SHOW because that's what it defaults to

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
            _jCamView!!.setCameraIndex(_cameraIndex)
            _jCamView!!.setCvCameraViewListener(this@OpenCVRunner)
        }

        // Add our vision processors
        _visionProcessors.add(RedJewelFinder())
        _visionProcessors.add(BlueJewelFinder())
        _visionProcessors.add(VirtualJewelArm())
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
            Log.e(this.javaClass.simpleName, "Initialization failed")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, mainActivity, _loaderCallBack)
        }
        else
        {
            Log.d(this.javaClass.simpleName, "Initialization successful")
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
     * Toggles OpenCV analysis
     */
    fun toggleAnalyze()
    {
        mainActivity.runOnUiThread({
            if(_state == _CVState.SHOW)
            {
                _setState(_CVState.SHOW_ANALYZE)
            }
            else
            {
                _setState(_CVState.SHOW)
            }
        })
    }


    /**
     * Toggles showing/hiding the OpenCV window
     */
    fun toggleShowHide()
    {
        mainActivity.runOnUiThread {
            if(_state == _CVState.HIDDEN)
            {
                _setState(_CVState.SHOW)
            }
            else
            {
                _setState(_CVState.HIDDEN)
            }
        }
    }


    /**
     * OpenCV state machine
     */
    private fun _setState(state: _CVState)
    {
        _state = state

        when(state)
        {
            _CVState.HIDDEN          ->
            {
                _jCamView?.visibility = View.GONE
                disableView()

                mainActivity.analyzeButton().isEnabled = false
                mainActivity.layoutHeader().setText(R.string.OpenCvCameraViewDisabled)
                mainActivity.analysisText().setText(R.string.analysis_disabled)
                mainActivity.showHideButton().setText(R.string.show)

                _setAnalyze(false)
            }

            _CVState.SHOW            ->
            {
                _jCamView?.visibility = View.VISIBLE
                enableView()

                mainActivity.analyzeButton().isEnabled = true
                mainActivity.layoutHeader().setText(R.string.opencv_camera_view_enabled)
                mainActivity.analysisText().setText(R.string.analysis_disabled)
                mainActivity.showHideButton().setText(R.string.hide)

                _setAnalyze(false)
            }

            _CVState.SHOW_ANALYZE    ->
            {
                _jCamView?.visibility = View.VISIBLE
                enableView()

                mainActivity.analyzeButton().isEnabled = true
                mainActivity.layoutHeader().setText(R.string.opencv_camera_view_enabled)
                mainActivity.analysisText().setText(R.string.analysis_enabled)
                mainActivity.showHideButton().setText(R.string.hide)

                _setAnalyze(true)
            }
        }
    }


    /**
     * Sets whether or not to analyze to the Boolean value passed in
     */
    private fun _setAnalyze(set: Boolean)
    {
        _analyze = set
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