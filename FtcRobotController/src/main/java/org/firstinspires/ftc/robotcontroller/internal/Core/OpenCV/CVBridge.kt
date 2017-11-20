@file:Suppress("PackageDirectoryMismatch", "unused")
package org.directcurrent.opencv


import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.UtilPoint


/**
 * Class that bridges the gap between the core package and the TeamCode module. Contains
 * data from objects detected and allows the TeamCode module to access them.
 *
 * When reading information from the class, you MUST wrap it in a try-catch block
 *
 * You must you must you must
 *
 * If you don't the program WILL crash. I promise you you don't want to debug it
 *
 * The reason this happens is because writing to these variables occurs on a separate thread while
 * reading usually occurs on a different thread. Objects could get changed partially through a read!
 *
 * I'm working on a better solution but until then you MUST use the try-catch block
 */
class CVBridge
{
    companion object Data
    {
        @JvmField
        var redJewelPoints = ArrayList<UtilPoint>()

        @JvmField
        var blueJewelPoints = ArrayList<UtilPoint>()
    }
}