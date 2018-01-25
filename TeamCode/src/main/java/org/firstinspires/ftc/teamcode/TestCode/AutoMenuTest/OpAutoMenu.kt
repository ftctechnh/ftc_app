@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.test.automenutest

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.AutoMenu


/**
 * Test class for the autonomous menu
 */
@Autonomous(name = "Menu Test")
@Disabled
class OpAutoMenu: LinearOpMode()
{
    private val _autoMenu = AutoMenu(this)


    override fun runOpMode()
    {
        _autoMenu.preRunInit()

        waitForStart()

        while (opModeIsActive())
        {
            // Nothing
        }
    }
}