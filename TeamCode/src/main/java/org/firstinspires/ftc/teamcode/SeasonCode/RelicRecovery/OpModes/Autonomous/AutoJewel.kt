package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.OpModes.Autonomous


import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.directcurrent.core.AutoStopper
import org.directcurrent.opencv.CVBridge
import org.directcurrent.season.relicrecovery.jewelarm.JewelArm
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.AutoMenu
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.Color
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Base


@Autonomous(name = "Jewel")
@Disabled
class AutoJewel : LinearOpMode()
{
    private val _base = Base()

    private val _autoMenu = AutoMenu(this)

    private val _autoStopper = AutoStopper(this , _base)


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

        _autoMenu.preRunInit()

        waitForStart()

        sleep(_autoMenu.startDelay())

        _autoStopper.startChecking()

        telemetry.addLine("Waiting for OpenCV...")
        telemetry.update()

        // Open OpenCV Stuff
        CVBridge.openCvRunner?.toggleShowHide()
        CVBridge.openCvRunner?.toggleAnalyze()


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


            // Jewel color decisions
            if(CVBridge.redJewelPoints.size == 0 && CVBridge.blueJewelPoints.size == 0)
            {
                telemetry.addData("Decision" , "Uncertain")
                jewelStatus = JewelStatus.UNCERTAIN
            }
            // It's backwards because the phone is upside down
            else if(CVBridge.redJewelPoints[0].y() < CVBridge.blueJewelPoints[0].y())
            {
                telemetry.addData("Decision" , "Red on left")

                jewelStatus = if(_autoMenu.teamColor() == Color.ColorID.RED)
                {
                    JewelStatus.GO_BACKWARD
                }
                else
                {
                    JewelStatus.GO_FORWARD
                }
            }
            else
            {
                telemetry.addData("Decision" , "Blue on left")
                jewelStatus = if(_autoMenu.teamColor() == Color.ColorID.RED)
                {
                    JewelStatus.GO_FORWARD
                }
                else
                {
                    JewelStatus.GO_BACKWARD
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
                // Nothing
            }
            JewelStatus.GO_FORWARD ->
            {
                _base.jewelArm.setState(JewelArm.State.DOWN)
                sleep(750)

                _base.drivetrain.driveForTime.setParams(1_000 , .7)
                _base.drivetrain.driveForTime.runSequentially()

                _base.jewelArm.setState(JewelArm.State.UP)
                sleep(750)
            }
            JewelStatus.GO_BACKWARD ->
            {
                _base.jewelArm.setState(JewelArm.State.DOWN)
                sleep(750)

                _base.drivetrain.driveForTime.setParams(1_000 , -.7)
                _base.drivetrain.driveForTime.runSequentially()

                _base.jewelArm.setState(JewelArm.State.UP)
                sleep(750)
            }
        }
    }
}