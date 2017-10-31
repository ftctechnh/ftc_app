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