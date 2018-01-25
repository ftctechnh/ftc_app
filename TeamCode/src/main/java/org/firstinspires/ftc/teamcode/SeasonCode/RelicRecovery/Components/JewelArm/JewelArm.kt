@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.season.relicrecovery.jewelarm


import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent


/**
 * Class that manages the jewel arm
 */
class JewelArm: RobotComponent()
{
    private var _armServo: Servo? = null


    /**
     * States of the jewel arm
     */
    enum class State
    {
        DOWN ,
        UP
    }


    /**
     * Initializes the Jewel Arm
     */
    override public fun init(BASE: RobotBase?)
    {
        super.init(BASE)

        _armServo = mapper.mapServo("jewelServo" , Servo.Direction.FORWARD)
    }


    /**
     * Sets the state of the jewel arm
     */
    fun setState(state: State)
    {
        when(state)
        {
            State.DOWN -> _armServo?.position = 0.27

            State.UP -> _armServo?.position = 0.8
        }

        base.telMet().tagWrite("Arm Pos" , _armServo?.position)
    }


    /**
     * Stops the Jewel Arm
     */
    override fun stop()
    {
        // Nothing :)
    }
}