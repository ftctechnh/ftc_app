@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.opencv.visionprocessors


import org.directcurrent.opencv.equalizeIntensity
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import java.lang.Math.abs


class BrownGlyphFinder: VisionProcessor()
{
    private var displayMat: Mat? = null
    private var processingMat: Mat? = null


    /**
     * Initializes internal Mats for gray glyph processing
     */
    override fun initMats(width: Int, height: Int)
    {
        displayMat = Mat(width , height , CvType.CV_8UC4)
        processingMat = Mat(width , height , CvType.CV_8UC4)
    }


    /**
     * Finds grey glyphs and draws a contour around them on a mat, which is returned
     */
    override fun processFrame(originalMat: Mat?): Mat?
    {
        originalMat?.copyTo(displayMat)


        // Equalize using Histogram Equalization
        processingMat = equalizeIntensity(originalMat)
        originalMat?.release()

        // Blur for extra edge detection
        Imgproc.medianBlur(processingMat , processingMat , 5)

        /*
         * Get our HSV mat, and limit restrict it to a color range we want
         * Color range is HSV- you can get this by taking a picture with the phone and running
         * it through GRIP
         */
        Imgproc.cvtColor(processingMat , processingMat , Imgproc.COLOR_RGB2HSV)
        Core.inRange(processingMat , Scalar(0.0 , 85.0 , 32.0) ,
                Scalar(25.0 , 196.0 , 140.0) , processingMat)


        Imgproc.erode(processingMat , processingMat , Imgproc.getStructuringElement
        (Imgproc.MORPH_RECT, Size(2.0 , 2.0)) , Point(0.0 , 0.0) , 1)

        Imgproc.dilate(processingMat , processingMat , Imgproc.getStructuringElement
        (Imgproc.MORPH_RECT, Size(2.0 , 2.0)) , Point(0.0 , 0.0) , 3)


        // Declare a list of contours, find them, and then draw them.
        val contours = ArrayList<MatOfPoint>()
        val filteredContours = ArrayList<MatOfPoint>()

        Imgproc.findContours(processingMat , contours , Mat() , Imgproc.RETR_EXTERNAL , Imgproc.CHAIN_APPROX_SIMPLE)
        processingMat?.release()

        // Filter out some contours
        contours.filterTo(filteredContours)
        {
            Imgproc.contourArea(it) >= 22_500
        }

        displayInfo(filteredContours)

        return displayMat
    }


    override fun displayInfo(contours: ArrayList<MatOfPoint>)
    {
        for(i in contours)
        {
            val rect = Imgproc.boundingRect(i)

            // If it's roughly in the shape of a square
            if(abs(rect.height - rect.width) <= 50 && rect.height >= 125 && rect.width >= 125)
            {
                if(rect.x >= 500)
                {
                    Imgproc.rectangle(displayMat, Point(rect.x.toDouble() , rect.y.toDouble()) ,
                            Point((rect.x + rect.width).toDouble() , (rect.y + rect.height).toDouble()) ,
                            Scalar(255.0 , 0.0 , 0.0) , 6)

                    Imgproc.putText(displayMat, "Brown Glyph :)"
                            , Point(rect.x.toDouble() ,
                            (rect.y + rect.height + 50).toDouble()) , Core.FONT_HERSHEY_COMPLEX , 1.0 ,
                            Scalar(0.0 , 255.0 , 0.0) , 3)

                    Imgproc.putText(displayMat, "x: " + rect.x , Point(rect.x.toDouble() ,
                            (rect.y + rect.height + 90).toDouble()) , Core.FONT_HERSHEY_COMPLEX , 1.0 ,
                            Scalar(0.0 , 255.0 , 0.0) , 3)

                    Imgproc.putText(displayMat, "y: " + rect.y , Point(rect.x.toDouble() ,
                            (rect.y + rect.height + 130).toDouble()) , Core.FONT_HERSHEY_COMPLEX , 1.0 ,
                            Scalar(0.0 , 255.0 , 0.0) , 3)
                }
            }
        }
    }


    /**
     * Releases mats because garbage collection and C++
     */
    override fun releaseMats()
    {
        displayMat?.release()
    }
}