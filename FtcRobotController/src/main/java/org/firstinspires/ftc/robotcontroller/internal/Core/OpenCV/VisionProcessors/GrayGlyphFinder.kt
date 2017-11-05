/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-10-30

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
@file:Suppress("PackageDirectoryMismatch", "ClassName", "FunctionName")
package org.directcurrent.opencv.visionprocessors

import org.opencv.core.*
import org.opencv.imgproc.Imgproc


/**
 * Kotlin class for gray glyph processing. Currently draws contour around gray glyph
 */
class GrayGlyphFinder : VisionProcessor()
{
    private var limitedHsvMat: Mat? = null
    private var contourMat: Mat? = null
    private var boundingMat: Mat? = null


    /**
     * Initializes internal Mats for gray glyph processing
     */
    override fun initMats(width: Int, height: Int)
    {
        limitedHsvMat = Mat(width , height , CvType.CV_8UC4)
        contourMat = Mat(width , height , CvType.CV_8UC4)
        boundingMat = Mat(width , height , CvType.CV_8UC4)
    }


    /**
     * Finds grey glyphs and draws a contour around them on a mat, which is returned
     */
    override fun processFrame(originalMat: Mat?): Mat?
    {
        limitedHsvMat = Mat()
        originalMat?.copyTo(contourMat)
        originalMat?.copyTo(boundingMat)


        /*
         * Get our HSV mat, and limit restrict it to a color range we want
         * Color range is HSV- you can get this by taking a picture with the phone and running
         * it through GRIP
         */
        Imgproc.cvtColor(originalMat , limitedHsvMat , Imgproc.COLOR_RGB2HSV)
        Core.inRange(limitedHsvMat , Scalar(86.0 , 5.0 , 69.0) ,
                Scalar(157.0 , 42.0 , 190.0) , limitedHsvMat)


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
                            Scalar(0.0 , 0.0 , 255.0) , 6)
                }



        // Deleting pointers
        originalMat?.release()
        limitedHsvMat?.release()
        contourMat?.release()

        sweeper.sweep()

        return boundingMat
    }


    override fun displayInfo(contours: ArrayList<MatOfPoint>)
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    /**
     * Releases mats because garbage collection and C++
     */
    override fun releaseMats()
    {
        limitedHsvMat?.release()
        contourMat?.release()
        boundingMat?.release()
    }
}