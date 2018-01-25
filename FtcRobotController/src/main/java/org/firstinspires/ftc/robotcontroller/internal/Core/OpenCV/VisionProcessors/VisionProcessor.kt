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

import org.directcurrent.opencv.CVBridge
import org.directcurrent.opencv.Sweeper
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint


/**
 * Kotlin abstract class for vision processing
 */
abstract class VisionProcessor
{
    protected val sweeper = Sweeper(20_000)

    /**
     * Initializes mats for use
     */
    abstract fun initMats(width: Int , height: Int)


    /**
     * Processes mat and returns result
     */
    abstract fun processFrame(originalMat: Mat?): Mat?


    /**
     * Called toward the end of processFrame- should be used for things such as drawing
     * bounding boxes and adding text
     */
    abstract fun displayInfo(contours: ArrayList<MatOfPoint>)


    /**
     * Releases mats for garbage collection
     */
    abstract fun releaseMats()
}