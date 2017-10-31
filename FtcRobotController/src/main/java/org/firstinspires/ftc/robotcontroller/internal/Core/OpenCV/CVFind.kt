@file:Suppress("PackageDirectoryMismatch", "ClassName", "FunctionName")
package org.directcurrent.opencv

import android.app.Activity
import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.core.MatOfPoint


/**
 * Kotlin class for finding a grey glyph
 *
 * Expanding this in the future to find more stuff (or make this an abstract class?)
 */
class CVFind constructor(var activity: Activity , camIndex: Int) : OpenCVRunner(activity , camIndex)
{
    private var limitedHsvMat: Mat? = null
    private var contourMat: Mat? = null
    private var boundingMat: Mat? = null


    /**
     * Initializes the mats needed to do spooky vision processing
     */
    override fun onCameraViewStarted(width: Int, height: Int)
    {
        limitedHsvMat = Mat(width , height , CvType.CV_8UC4)
        contourMat = Mat(width , height , CvType.CV_8UC4)
        boundingMat = Mat(width , height , CvType.CV_8UC4)
    }


    /**
     * Cleanup code, C++ library, garbage collection and such
     *
     * Honestly, I love C++ for the control it gives me, but I wish it would clean
     * up after itself every once in a while.
     */
    override fun onCameraViewStopped()
    {
        limitedHsvMat?.release()
        contourMat?.release()
        boundingMat?.release()
    }


    /**
     * Called every time the camera reads a frame into OpenCV- we do vision processing here.
     *
     * Returns the processed frame to the Java Camera View if we pass in a non-null frame.
     * Returns a default Mat object if we pass in a null object. Honestly, this should never happen
     */
    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat?
    {
        if (inputFrame != null)
        {
            return _processFrame(inputFrame.rgba())
        }

        return Mat()
    }


    /**
     * Returns frame with contours drawn around light grey glyph
     */
    private fun _processFrame(originalMat: Mat): Mat?
    {
        limitedHsvMat = Mat()
        originalMat.copyTo(contourMat)
        originalMat.copyTo(boundingMat)


        /*
         * Get our HSV mat, and limit restrict it to a color range we want
         * Color range is RGB- you can get this by taking a picture with the phone and running
         * it through GRIP, and then running the HSV values through a converter
         */
        Imgproc.cvtColor(originalMat , limitedHsvMat , Imgproc.COLOR_RGB2HSV)
        Core.inRange(limitedHsvMat , Scalar(33.0 , 33.0 , 33.0) ,
                    Scalar(230.0 , 230.0 , 230.0) , limitedHsvMat)


        // Declare a list of contours, find them, and then draw them.
        val contours = ArrayList<MatOfPoint>()
        val filteredContours = ArrayList<MatOfPoint>()

        Imgproc.findContours(limitedHsvMat , contours , Mat() , Imgproc.RETR_EXTERNAL , Imgproc.CHAIN_APPROX_SIMPLE)

        // Filter out some contours
        contours.filterTo(filteredContours) { Imgproc.contourArea(it) >= 1_000 }

        Imgproc.drawContours(contourMat , filteredContours , -1 , Scalar(255.0 , 0.0 , 0.0) , 2)

        // Deleting pointers
        originalMat.release()
        limitedHsvMat?.release()
        boundingMat?.release()

        return contourMat
    }
}