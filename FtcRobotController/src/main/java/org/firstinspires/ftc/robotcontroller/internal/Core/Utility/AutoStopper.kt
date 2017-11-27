@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.core

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase


/**
 * Utility class for stopping an autonomous built with Linear OpMode
 */
@Suppress("unused")
class AutoStopper(private val _auto: LinearOpMode , private val _robot: RobotBase)
{
    /**
     * Call this ONCE in runOpMode
     */
    fun startChecking()
    {
        val t = Thread(Runnable
        {
            while(true)
            {
                if(!_auto.opModeIsActive())
                {
                    _robot.stop()
                    break
                }
            }
        })

        t.start()
    }
}