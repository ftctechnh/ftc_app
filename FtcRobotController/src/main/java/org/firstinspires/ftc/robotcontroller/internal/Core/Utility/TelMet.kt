/*
    Team 5893 Direct Current

    Authors: Matthew Fan
    Date Created: 2017-11-01

    Please adhere to these units when working in this project:

    Time: Milliseconds
    Distance: Centimeters
    Angle: Degrees (mathematical orientation)
 */
@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.core


import org.firstinspires.ftc.robotcore.external.Telemetry


/**
 * Small Kotlin wrapper class for telemetry, allows non-OpMode classes to output telemetry
 */
@Suppress("unused")
@Deprecated("Robot base LinearOpModes can now be accessed- just use that instead")
class TelMet(private val _telMet: Telemetry)
{
    /**
     * Writes message to OpMode telemetry with tag.
     *
     * Output looks like this:
     * tag: msg
     */
    fun<T> tagWrite(tag: String, msg: T)
    {
        _telMet.addData(tag , msg)
    }


    /**
     * Writes message to OpMode telemetry and then appends newline
     */
    fun write(msg: String)
    {
        _telMet.addLine(msg)
    }


    /**
     * Adds a new line to OpMode telemetry
     */
    fun newLine()
    {
        _telMet.addLine()
    }


    /**
     * Clears OpMode telemetry
     */
    fun clear()
    {
        _telMet.clear()
    }


    /**
     * Updates OpMode telemetry
     *
     * Call this in a loop- by default, telemetry only updates at the
     * end of the OpMode loop
     */
    fun update()
    {
        _telMet.update()
    }
}