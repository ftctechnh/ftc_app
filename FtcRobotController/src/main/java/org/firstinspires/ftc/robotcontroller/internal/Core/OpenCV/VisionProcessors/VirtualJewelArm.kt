package org.firstinspires.ftc.robotcontroller.internal.Core.OpenCV.VisionProcessors

import org.directcurrent.opencv.equalizeIntensity
import org.directcurrent.opencv.visionprocessors.VisionProcessor
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

/**
 * Created by FnMat on 12/12/2017.
 */
class VirtualJewelArm: VisionProcessor()
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

        Imgproc.line(displayMat , Point(72.0 , 475.0) , Point(110.0 , 410.0) , Scalar(0.0 , 255.0 , 0.0) , 3)
        Imgproc.line(displayMat , Point(110.0 , 410.0) , Point(110.0 , 375.0) , Scalar(0.0 , 255.0 , 0.0) , 3)
        Imgproc.line(displayMat , Point(110.0 , 375.0) , Point(30.0 , 475.0) , Scalar(0.0 , 255.0 , 0.0) , 3)

        return displayMat
    }


    override fun displayInfo(contours: ArrayList<MatOfPoint>)
    {
        // Nothing
    }


    /**
     * Releases mats because garbage collection and C++
     */
    override fun releaseMats()
    {
        displayMat?.release()
    }
}