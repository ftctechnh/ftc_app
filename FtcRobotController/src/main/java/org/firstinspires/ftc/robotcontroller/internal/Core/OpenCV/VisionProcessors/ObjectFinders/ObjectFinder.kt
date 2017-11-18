@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.opencv.visionprocessors.objectfinders


import org.directcurrent.opencv.equalizeIntensity
import org.directcurrent.opencv.visionprocessors.VisionProcessor
import org.opencv.core.*
import org.opencv.imgproc.Imgproc


/**
 * Abstract Kotlin class for finding objects
 */
abstract class ObjectFinder(private val lowerHSVBound: Scalar , private val upperHSVBound: Scalar):
        VisionProcessor()
{
    /** Mat used to display information- this is the final Mat that is returned */
    protected var displayMat: Mat? = null

    private var _processingMat: Mat? = null


    /**
     * Initializes internal Mats for object detection
     */
    override fun initMats(width: Int , height: Int)
    {
        displayMat = Mat(width , height , CvType.CV_8UC4)
        _processingMat = Mat(width , height , CvType.CV_8UC4)
    }


    /**
     * Finds an object and draws a contour around them on a mat, which is returned
     */
    override fun processFrame(originalMat: Mat?): Mat?
    {
        originalMat?.copyTo(displayMat)


        // Equalize using Histogram Equalization
        _processingMat = equalizeIntensity(originalMat)
        originalMat?.release()

        // Blur for extra edge detection
        Imgproc.medianBlur(_processingMat, _processingMat, 5)

        /*
         * Get our HSV mat, and limit restrict it to a color range we want
         * Color range is HSV- you can get this by taking a picture with the phone and running
         * it through GRIP
         */
        Imgproc.cvtColor(_processingMat, _processingMat, Imgproc.COLOR_RGB2HSV)
        Core.inRange(_processingMat, lowerHSVBound,
                upperHSVBound, _processingMat)


        Imgproc.erode(_processingMat, _processingMat, Imgproc.getStructuringElement
        (Imgproc.MORPH_RECT, Size(2.0 , 2.0)) , Point(0.0 , 0.0) , 1)

        Imgproc.dilate(_processingMat, _processingMat, Imgproc.getStructuringElement
        (Imgproc.MORPH_RECT, Size(2.0 , 2.0)) , Point(0.0 , 0.0) , 3)


        // Declare a list of contours, find them, and then draw them.
        val contours = ArrayList<MatOfPoint>()
        val filteredContours = ArrayList<MatOfPoint>()

        Imgproc.findContours(_processingMat, contours , Mat() , Imgproc.RETR_EXTERNAL , Imgproc.CHAIN_APPROX_SIMPLE)
        _processingMat?.release()

        // Filter out some contours
        contours.filterTo(filteredContours)
        {
            Imgproc.contourArea(it) >= 0
        }

        displayInfo(filteredContours)

        return displayMat
    }


    /**
     * Releases mats because garbage collection and C++
     */
    override fun releaseMats()
    {
        displayMat?.release()
    }
}