@file:Suppress("PackageDirectoryMismatch", "unused")
package org.directcurrent.season.relicrecovery.autonomous


import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.directcurrent.opencv.CVBridge
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Base
import org.directcurrent.core.AutoStopper
import org.directcurrent.season.relicrecovery.jewelarm.JewelArm
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.AutoMenu
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.Color
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.Vuforia
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.GlyphGrabber.GlyphGrabber


@Autonomous(name = "Relic Side")
class AutoRelicSide : LinearOpMode()
{
    private val _base = Base()

    private val _autoMenu = AutoMenu(this)

    private val _autoStopper = AutoStopper(this , _base)

    private val _vuforia = Vuforia()


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

        _base.imu.calibrateTo(180)

        _vuforia.init(hardwareMap, VuforiaLocalizer.CameraDirection.BACK)
        _vuforia.activate()

        _autoMenu.preRunInit()

        waitForStart()

        _autoStopper.startChecking()

        val vuMark = _vuforia.currentMarker()

        _vuforia.deactivate()
        telemetry.addLine("Waiting for OpenCV...")
        telemetry.update()

        // Open OpenCV Stuff
        CVBridge.openCvRunner?.toggleShowHide()
        CVBridge.openCvRunner?.toggleAnalyze()



        // Turn to get the jewels
        _base.drivetrain.turnTo.setParams(160.0 , .4)
        _base.drivetrain.turnTo.runSequentially()

        sleep(2_000)


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


                _base.jewelArm.setState(JewelArm.State.UP)
                sleep(750)

                _base.drivetrain.driveTo.setDestination(-20.0)
                _base.drivetrain.driveTo.runSequentially()
                sleep(100)
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
                _base.drivetrain.driveTo.setDestination(-20.0)
                _base.drivetrain.driveTo.runSequentially()
                sleep(100)
            }
            JewelStatus.GO_FORWARD ->
            {
                _base.jewelArm.setState(JewelArm.State.DOWN)
                sleep(750)

                _base.drivetrain.driveTo.setDestination(2.5)
                _base.drivetrain.driveTo.runSequentially()
                sleep(100)

                _base.jewelArm.setState(JewelArm.State.UP)
                sleep(750)

                _base.drivetrain.driveTo.setDestination(-30.0)
                _base.drivetrain.driveTo.runSequentially()
                sleep(100)
            }
            JewelStatus.GO_BACKWARD ->
            {
                _base.jewelArm.setState(JewelArm.State.DOWN)
                sleep(750)

                _base.drivetrain.driveTo.setDestination(-20.0)
                _base.drivetrain.driveTo.runSequentially()
                sleep(100)
            }
        }


        // Glyph part of the auto
        if(_autoMenu.teamColor() == Color.ColorID.BLUE)
        {
            // Drive to proper spot
            if(vuMark == RelicRecoveryVuMark.LEFT || vuMark == RelicRecoveryVuMark.UNKNOWN)
            {
                _base.drivetrain.driveTo.setDestination(-10.0)
                _base.drivetrain.driveTo.runSequentially()
                sleep(100)
            }
            else if(vuMark == RelicRecoveryVuMark.CENTER)
            {
                _base.drivetrain.driveTo.setDestination(-15.0)
                _base.drivetrain.driveTo.runSequentially()
                sleep(100)
            }
            else
            {
                _base.drivetrain.driveTo.setDestination(-20.0)
                _base.drivetrain.driveTo.runSequentially()
                sleep(100)
            }

            // Turn to the proper spot
            _base.drivetrain.turnTo.setParams(90.0 , .7)
            _base.drivetrain.turnTo.runSequentially()
            sleep(100)

        }
        else
        {
            // Drive to proper spot
            if(vuMark == RelicRecoveryVuMark.LEFT || vuMark == RelicRecoveryVuMark.UNKNOWN)
            {
                _base.drivetrain.driveTo.setDestination(10.0)
                _base.drivetrain.driveTo.runSequentially()
                sleep(100)
            }
            else if(vuMark == RelicRecoveryVuMark.CENTER)
            {
                _base.drivetrain.driveTo.setDestination(15.0)
                _base.drivetrain.driveTo.runSequentially()
                sleep(100)
            }
            else
            {
                _base.drivetrain.driveTo.setDestination(20.0)
                _base.drivetrain.driveTo.runSequentially()
                sleep(100)
            }

            // Turn to proper spot
            _base.drivetrain.turnTo.setParams(270.0 , .7)
            _base.drivetrain.turnTo.runSequentially()
            sleep(100)
        }


        // Output and drive in
        _base.drivetrain.driveForTime.setParams(4_000 , .5)
        _base.drivetrain.driveForTime.runSequentially()
        sleep(100)

        _base.drivetrain.driveForTime.setParams(1_000 , -.4)
        _base.drivetrain.driveForTime.runSequentially()
        sleep(100)

        _base.glyphGrabber.activateForTime.setParams(5_000 , GlyphGrabber.State.OUTPUT)
        _base.glyphGrabber.activateForTime.runSequentially()
        sleep(100)

        _base.drivetrain.driveForTime.setParams(1_000 , .4)
        _base.drivetrain.driveForTime.runSequentially()
        sleep(100)

        telemetry.update()
    }
}