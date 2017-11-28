package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.OpModes.Autonomous

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Base


@Autonomous(name = "test")
class AutoTest: LinearOpMode()
{
    private val _base = Base()

    override fun runOpMode()
    {
        _base.init(hardwareMap , this)

        waitForStart()

        _base.drivetrain.driveTo.setDestination(120.0)

        _base.drivetrain.driveTo.runSequentially()
    }
}