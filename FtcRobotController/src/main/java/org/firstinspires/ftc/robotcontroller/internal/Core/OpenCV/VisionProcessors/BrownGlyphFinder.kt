@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.opencv.visionprocessors


import org.directcurrent.opencv.equalizeIntensity
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import java.lang.Math.abs


class BrownGlyphFinder: VisionProcessor()
{
    private var equalizedMat: Mat? = null
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

        equalizedMat = equalizeIntensity(originalMat)

        equalizedMat?.copyTo(contourMat)
        equalizedMat?.copyTo(boundingMat)

        /*
         * Get our HSV mat, and limit restrict it to a color range we want
         * Color range is HSV- you can get this by taking a picture with the phone and running
         * it through GRIP
         */
        Imgproc.cvtColor(equalizedMat , limitedHsvMat , Imgproc.COLOR_RGB2HSV)
        Core.inRange(limitedHsvMat , Scalar(0.0 , 85.0 , 32.0) ,
                Scalar(25.0 , 196.0 , 140.0) , limitedHsvMat)


        Imgproc.erode(limitedHsvMat , limitedHsvMat , Imgproc.getStructuringElement
        (Imgproc.MORPH_RECT, Size(2.0 , 2.0)) , Point(0.0 , 0.0) , 1)

        Imgproc.dilate(limitedHsvMat , limitedHsvMat , Imgproc.getStructuringElement
        (Imgproc.MORPH_RECT, Size(2.0 , 2.0)) , Point(0.0 , 0.0) , 3)


        // Declare a list of contours, find them, and then draw them.
        val contours = ArrayList<MatOfPoint>()
        val filteredContours = ArrayList<MatOfPoint>()

        Imgproc.findContours(limitedHsvMat , contours , Mat() , Imgproc.RETR_EXTERNAL , Imgproc.CHAIN_APPROX_SIMPLE)

//      Filter out some contours
        contours.filterTo(filteredContours)
        {
            Imgproc.contourArea(it) >= 22_500
        }

        Imgproc.drawContours(contourMat , filteredContours , -1 , Scalar(255.0 , 0.0 , 0.0) , 6)


        for(i in filteredContours)
        {
            val rect = Imgproc.boundingRect(i)

            // If it's roughly in the shape of a square
            if(abs(rect.height - rect.width) <= 50 && rect.height >= 125 && rect.width >= 125)
            {
                if(rect.x >= 500)
                {
                    Imgproc.rectangle(boundingMat , Point(rect.x.toDouble() , rect.y.toDouble()) ,
                            Point((rect.x + rect.width).toDouble() , (rect.y + rect.height).toDouble()) ,
                            Scalar(255.0 , 0.0 , 0.0) , 6)

                    Imgproc.putText(boundingMat , "Brown Glyph :)"
                            , Point(rect.x.toDouble() ,
                            (rect.y + rect.height + 50).toDouble()) , Core.FONT_HERSHEY_COMPLEX , 1.0 ,
                            Scalar(0.0 , 255.0 , 0.0) , 3)

                    Imgproc.putText(boundingMat , "x: " + rect.x , Point(rect.x.toDouble() ,
                            (rect.y + rect.height + 90).toDouble()) , Core.FONT_HERSHEY_COMPLEX , 1.0 ,
                            Scalar(0.0 , 255.0 , 0.0) , 3)
                }
            }
        }

        // Draw bounding rectangles over contours
//        filteredContours
//                .map { Imgproc.boundingRect(it) }
//                .forEach {
//                    Imgproc.rectangle(boundingMat , Point(it.x.toDouble() , it.y.toDouble()) ,
//                            Point((it.x + it.width).toDouble(), (it.y + it.height).toDouble()) ,
//                            Scalar(255.0 , 0.0 , 0.0) , 6)
//                }



        // Deleting pointers
        originalMat?.release()
        equalizedMat?.release()
        limitedHsvMat?.release()
        contourMat?.release()

        return boundingMat
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