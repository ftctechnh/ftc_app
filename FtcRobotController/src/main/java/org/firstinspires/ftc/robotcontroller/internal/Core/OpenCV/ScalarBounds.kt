@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.opencv


import org.opencv.core.Scalar


/** Lower HSV bound for the brown glyph */
val BrownGlyphLower = Scalar(2.0 , 35.0 , 10.0)

/** Upper HSV bound for the brown glyph */
val BrownGlyphUpper = Scalar(25.0 , 260.0 , 125.0)