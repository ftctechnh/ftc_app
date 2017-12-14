@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.opencv


import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc


/**
 * Equalizes intensity using Histogram Equalization
 *
 * @param inputImg Image to equalize in RGB
 *
 * @return The processed Mat- if the given MAT does not have at least 3 channels, then a blank Mat
 *         is returned
 */
fun equalizeIntensity(inputImg: Mat?): Mat
{
    if(inputImg?.channels()!! >= 3)
    {
        val ycrcb = Mat()
        val result = Mat()

        val channels = ArrayList<Mat>()


        Imgproc.cvtColor(inputImg , ycrcb , Imgproc.COLOR_RGB2YCrCb)
        Core.split(ycrcb , channels)
        Imgproc.equalizeHist(channels.get(0) , channels.get(0))
        Core.merge(channels , ycrcb)
        Imgproc.cvtColor(ycrcb , result , Imgproc.COLOR_YCrCb2RGB)

        return result
    }

    return Mat()
}