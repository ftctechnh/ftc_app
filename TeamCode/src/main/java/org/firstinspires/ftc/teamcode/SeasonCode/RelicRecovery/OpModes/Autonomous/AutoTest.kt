package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.OpModes.Autonomous


import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.directcurrent.opencv.CVBridge
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Base
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import org.directcurrent.season.relicrecovery.jewelarm.JewelArm
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.Color


@Autonomous(name = "test")
class AutoTest: LinearOpMode()
{
    private val _base = Base()

    val teamColor = Color.ColorID.RED


    /**
     * Status of jewel detection
     */
    private enum class JewelStatus
    {
        GO_FORWARD ,
        GO_BACKWARD ,
        UNCERTAIN
    }


    /**
     * Runs the autonomous
     */
    override fun runOpMode()
    {
        _base.init(hardwareMap , this)

        _base.imu.fastCalibrate()

        waitForStart()

        telemetry.addLine("Waiting for OpenCV...")
        telemetry.update()

        // Open OpenCV Stuff
        CVBridge.openCvRunner?.toggleShowHide()
        CVBridge.openCvRunner?.toggleAnalyze()
        sleep(2_000)                // Wait for OpenCV


        var jewelStatus = JewelStatus.UNCERTAIN


        // Determine what to do with the jewels
        try
        {
            telemetry.addLine("OpenCV Red Jewel")

            for (i in CVBridge.redJewelPoints)
            {
                telemetry.addLine("(" + i.x() + " , " + i.y() + ")")
                telemetry.addLine()
            }


            telemetry.addLine("OpenCV Blue Jewel")

            for(i in CVBridge.blueJewelPoints)
            {
                telemetry.addLine("(" + i.x() + " , " + i.y() + ")")
                telemetry.addLine()
            }



            if(CVBridge.redJewelPoints.size == 0 && CVBridge.blueJewelPoints.size == 0)
            {
                telemetry.addData("Decision" , "Uncertain")
                jewelStatus = JewelStatus.UNCERTAIN
            }
            // It's backwards because the phone is upside down
            else if(CVBridge.redJewelPoints[0].y() < CVBridge.blueJewelPoints[0].y())
            {
                telemetry.addData("Decision" , "Red on left")

                if(teamColor == Color.ColorID.RED)
                {
                    jewelStatus = JewelStatus.GO_BACKWARD
                }
                else
                {
                    jewelStatus = JewelStatus.GO_FORWARD
                }
            }
            else
            {
                telemetry.addData("Decision" , "Blue on left")
                if(teamColor == Color.ColorID.RED)
                {
                    jewelStatus = JewelStatus.GO_FORWARD
                }
                else
                {
                    jewelStatus = JewelStatus.GO_BACKWARD
                }
            }
        }
        catch(e: Exception)
        {
            e.printStackTrace()
        }


        // Hide OpenCV for battery life
        CVBridge.openCvRunner?.toggleShowHide()


        when(jewelStatus)
        {
            JewelStatus.UNCERTAIN ->
            {
                _base.jewelArm.setState(JewelArm.State.UP)
                sleep(500)

                _base.drivetrain.turnTo.setParams(0.0 , .2)
                _base.drivetrain.turnTo.runSequentially()
            }

            JewelStatus.GO_FORWARD ->
            {
                _base.jewelArm.setState(JewelArm.State.DOWN)
                sleep(750)

                _base.drivetrain.driveTo.setDestination(1.5)
                _base.drivetrain.driveTo.runSequentially()

                sleep(100)

                _base.drivetrain.turnTo.setParams(0.0 , .2)
                _base.drivetrain.turnTo.runSequentially()

                _base.jewelArm.setState(JewelArm.State.UP)
            }

            JewelStatus.GO_BACKWARD ->
            {
                _base.jewelArm.setState(JewelArm.State.DOWN)
                sleep(750)
                _base.drivetrain.driveTo.setDestination(-1.2)
                _base.drivetrain.driveTo.runSequentially()

                sleep(100)

                _base.drivetrain.turnTo.setParams(0.0 , .2)
                _base.drivetrain.turnTo.runSequentially()

                sleep(100)

                _base.jewelArm.setState(JewelArm.State.UP)

                sleep(1_000)

                _base.drivetrain.driveTo.setDestination(10.0)
                _base.drivetrain.driveTo.runSequentially()

                sleep(100)
            }
        }


        _base.drivetrain.driveTo.setDestination(10.0)
        _base.drivetrain.driveTo.runSequentially()


        telemetry.update()
        sleep(50_000)
    }
}