@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.test.motorramp

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.directcurrent.core.commands.RampMotor
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent


class Motors: RobotComponent()
{
    var m1: DcMotor? = null
    var m2: DcMotor? = null

    var ramp1: RampMotor? = null
    var ramp2: RampMotor? = null

    public override fun init(BASE: RobotBase?)
    {
        super.init(BASE)

        m1 = mapper.mapMotor("one" , DcMotorSimple.Direction.FORWARD)
        m2 = mapper.mapMotor("two" , DcMotorSimple.Direction.FORWARD)

        ramp1 = RampMotor(m1!!)
        ramp2 = RampMotor(m2!!)

        ramp1!!.setRampParams(0.0 , 1.0 , 5_000)
        ramp2!!.setRampParams(1.0 , 0.0 , 5_000)
    }


    fun run()
    {
        ramp1?.runParallel()
        ramp2?.runParallel()
    }


    override fun stop()
    {
        // Nothing :)
    }
}