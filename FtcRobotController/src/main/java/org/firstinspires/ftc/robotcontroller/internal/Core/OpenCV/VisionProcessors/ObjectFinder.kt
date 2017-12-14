@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.opencv.visionprocessors


import org.directcurrent.opencv.equalizeIntensity
import org.opencv.core.*
import org.opencv.imgproc.Imgproc


/**
 * Abstract Kotlin class for finding objects
 */
abstract class ObjectFinder: VisionProcessor()
{
    /** Mat that has undergone Histogram Equalization */
    protected var equalizedMat: Mat? = null

    /** HSV mat that has had its range limited */
    protected var limitedHsvMat: Mat? = null

    /** Mat that contains drawn contours */
    protected var contourMat: Mat? = null

    /** Mat that contains bounding shapes and other elements */
    protected var boundingMat: Mat? = null


    /**
     * Initializes internal Mats for object detection
     */
    override fun initMats(width: Int , height: Int)
    {
        equalizedMat = Mat(width , height , CvType.CV_8UC4)
        limitedHsvMat = Mat(width , height , CvType.CV_8UC4)
        contourMat = Mat(width , height , CvType.CV_8UC4)
        boundingMat = Mat(width , height , CvType.CV_8UC4)
    }


    /**
     * Finds an object and draws a contour around them on a mat, which is returned
     */
    override fun processFrame(originalMat: Mat?): Mat?
    {

        limitedHsvMat = Mat()

        equalizedMat = equalizeIntensity(originalMat)

        equalizedMat?.copyTo(contourMat)
        equalizedMat?.copyTo(boundingMat)

        /*
         * Get our HSV mat, and limit restrict it to a color range we want
         * Color range is HSV- you can get this by taking a picture with the phone and running
         * it through GRIP
         */
        Imgproc.cvtColor(equalizedMat , limitedHsvMat , Imgproc.COLOR_RGB2HSV)
        Core.inRange(limitedHsvMat , Scalar(0.0 , 23.0 , 30.0) ,
                Scalar(19.0 , 192.0 , 159.0) , limitedHsvMat)


        Imgproc.erode(limitedHsvMat , limitedHsvMat , Imgproc.getStructuringElement
        (Imgproc.MORPH_RECT, Size(2.0 , 2.0)) , Point(0.0 , 0.0) , 3)

        Imgproc.dilate(limitedHsvMat , limitedHsvMat , Imgproc.getStructuringElement
        (Imgproc.MORPH_RECT, Size(2.0 , 2.0)) , Point(0.0 , 0.0) , 5)


        // Declare a list of contours, find them, and then draw them.
        val contours = ArrayList<MatOfPoint>()
        val filteredContours = ArrayList<MatOfPoint>()

        Imgproc.findContours(limitedHsvMat , contours , Mat() , Imgproc.RETR_EXTERNAL , Imgproc.CHAIN_APPROX_SIMPLE)

        // Filter out some contours
        contours.filterTo(filteredContours) { Imgproc.contourArea(it) >= 2_000 }

        Imgproc.drawContours(contourMat , filteredContours , -1 , Scalar(255.0 , 0.0 , 0.0) , 6)

        // Draw bounding rectangles over contours
        filteredContours
                .map { Imgproc.boundingRect(it) }
                .forEach {
                    Imgproc.rectangle(boundingMat , Point(it.x.toDouble() , it.y.toDouble()) ,
                            Point((it.x + it.width).toDouble(), (it.y + it.height).toDouble()) ,
                            Scalar(255.0 , 0.0 , 0.0) , 6)
                }


        // Deleting pointers
        originalMat?.release()
        equalizedMat?.release()
        limitedHsvMat?.release()
        contourMat?.release()

        return boundingMat
    }



    fun restrictHSV(hsvMat: Mat , lowBound: Scalar , highBound: Scalar)
    {
        Core.inRange(hsvMat , lowBound , highBound , hsvMat)
    }


    /**
     * Releases mats because garbage collection and C++
     */
    override fun releaseMats()
    {
        equalizedMat?.release()
        limitedHsvMat?.release()
        contourMat?.release()
        boundingMat?.release()
    }
}