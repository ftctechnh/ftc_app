@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.test.motorramp

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase


class Base: RobotBase()
{
    var motors = Motors()

    override fun init(HW: HardwareMap?, OPMODE: LinearOpMode?)
    {
        super.init(HW, OPMODE)

        motors.init(this)
    }


    override fun stop()
    {
        // Nothing :)
    }
}