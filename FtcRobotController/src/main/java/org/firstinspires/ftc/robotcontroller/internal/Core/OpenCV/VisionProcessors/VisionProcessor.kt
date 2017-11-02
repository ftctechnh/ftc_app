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

import org.opencv.core.Mat


/**
 * Kotlin abstract class for vision processing
 */
abstract class VisionProcessor
{
    /**
     * Initializes mats for use
     */
    abstract fun initMats(width: Int , height: Int)


    /**
     * Processes mat and returns result
     */
    abstract fun processFrame(originalMat: Mat?): Mat?


    /**
     * Releases mats for garbage collection
     */
    abstract fun releaseMats()
}