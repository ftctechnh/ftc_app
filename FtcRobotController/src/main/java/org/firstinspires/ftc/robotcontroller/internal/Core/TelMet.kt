@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.core

import com.qualcomm.robotcore.eventloop.opmode.OpMode


/**
 * Small Kotlin wrapper class for telemetry, allows non-OpMode classes to output telemetry
 */
@Suppress("unused")
class TelMet(private val _opMode: OpMode)
{
    /**
     * Writes message to OpMode telemetry with tag.
     *
     * Output looks like this:
     * tag: msg
     */
    fun<T> write(tag: String , msg: T)
    {
        _opMode.telemetry.addData(tag , msg)
    }


    /**
     * Adds a new line to OpMode telemetry
     */
    fun newLine()
    {
        _opMode.telemetry.addLine()
    }


    /**
     * Clears OpMode telemetry
     */
    fun clear()
    {
        _opMode.telemetry.clear()
    }


    /**
     * Updates OpMode telemetry
     *
     * Call this in a loop- by default, telemetry only updates at the
     * end of the OpMode loop
     */
    fun update()
    {
        _opMode.telemetry.update()
    }
}