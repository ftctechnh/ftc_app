@file:Suppress("PackageDirectoryMismatch", "unused")
package org.directcurrent.opencv.visionprocessors.objectfinders


import org.directcurrent.opencv.BrownGlyphLower
import org.directcurrent.opencv.BrownGlyphUpper
import org.opencv.core.Core
import org.opencv.core.MatOfPoint
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import java.lang.Math.abs


/**
 * In charge of detecting and reporting the brown glyph
 */
class BrownGlyphFinder: ObjectFinder(BrownGlyphLower , BrownGlyphUpper)
{
    /**
     * Displays information of any detected Brown Glyphs- draws a contour around them and gives
     * an x and y position
     */
    override fun displayInfo(contours: ArrayList<MatOfPoint>)
    {
        for(i in contours)
        {
            val rect = Imgproc.boundingRect(i)

            // If it's roughly in the shape of a square and large enough
            if(abs(rect.height - rect.width) <= 50 && rect.height >= 125 && rect.width >= 125)
            {
                // If it's below a certain line (on the floor-ish)
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
}