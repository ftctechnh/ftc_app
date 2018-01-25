@file:Suppress("PackageDirectoryMismatch", "unused", "LocalVariableName")
package org.directcurrent.test.motorramp


import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp


@TeleOp(name = "ramp" , group = "prototypes")
@Disabled
class OpRamp: LinearOpMode()
{
    override fun runOpMode()
    {
        val _base = Base()

        _base.init(hardwareMap , this)

        waitForStart()

        while(opModeIsActive())
        {
            _base.motors.run()
        }
    }
}