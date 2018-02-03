@file:Suppress("PackageDirectoryMismatch", "LocalVariableName")
package org.directcurrent.season.relicrecovery.autonomous


import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import org.directcurrent.core.AutoStopper
import org.directcurrent.opencv.CVBridge
import org.directcurrent.season.relicrecovery.jewelarm.JewelArm
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.AutoMenu
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.Color
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.Vuforia
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Base
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.GlyphGrabber.GlyphGrabber


/**
 * Autonomous for the back side of the field (side away from the Relic Recovery Zones)
 *
 * BLUE ALLIANCE
 *
 * What it does:
 *
 * 1. Knock off correct Jewel
 * 2. Move off of Balancing Stone
 * 3. Turn and drive in front of appropriate Cryptobox column as dictated by Cryptobox Key
 * 4. Place Glyph in Cryptobox column
 * 5. Turn to face Glyph Pit
 */
@Autonomous(name = "Back Side Blue")
@Suppress("unused")
class AutoBackSideBlue : LinearOpMode()
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
    override fun runOpMode() {
        _base.init(hardwareMap, this)

        _base.imu.calibrateTo(95)

        _vuforia.init(hardwareMap, VuforiaLocalizer.CameraDirection.BACK)
        _vuforia.activate()

        waitForStart()

        _base.drivetrain.setZeroPowerMode(DcMotor.ZeroPowerBehavior.BRAKE)

        _autoStopper.startChecking()

        val vuMark = _vuforia.currentMarker()

        _vuforia.deactivate()
        telemetry.addLine("Waiting for OpenCV...")
        telemetry.update()

        // Open OpenCV Stuff
        CVBridge.openCvRunner?.toggleShowHide()
        CVBridge.openCvRunner?.toggleAnalyze()


        // Turn to get the jewels
        _base.drivetrain.turnTo.setParams(75.0, .4 , 4_000)
        _base.drivetrain.turnTo.runSequentially()

        sleep(2_000)


        var jewelStatus = JewelStatus.UNCERTAIN


        // Determine what to do with the jewels
        try
        {
            // Jewel color decisions
            if (CVBridge.redJewelPoints.size == 0 && CVBridge.blueJewelPoints.size == 0)
            {
                telemetry.addData("Decision", "Uncertain")
                jewelStatus = JewelStatus.UNCERTAIN
            }
            // It's backwards because the phone is upside down
            else if (CVBridge.redJewelPoints[0].y() < CVBridge.blueJewelPoints[0].y())
            {
                telemetry.addData("Decision", "Red on left")

                jewelStatus = if (_autoMenu.teamColor() == Color.ColorID.RED)
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
                telemetry.addData("Decision", "Blue on left")
                jewelStatus = if (_autoMenu.teamColor() == Color.ColorID.RED)
                {
                    JewelStatus.GO_FORWARD
                }
                else
                {
                    JewelStatus.GO_BACKWARD
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }


        // Hide OpenCV for battery life
        CVBridge.openCvRunner?.toggleShowHide()


        when (jewelStatus)
        {
            JewelStatus.UNCERTAIN -> {
                _base.jewelArm.setState(JewelArm.State.UP)
                sleep(750)

                _base.drivetrain.turnTo.setParams(90.0 , .4 , 4_000)
                _base.drivetrain.turnTo.runSequentially()
                sleep(100)
            }
            JewelStatus.GO_FORWARD -> {
                _base.jewelArm.setState(JewelArm.State.DOWN)
                sleep(750)

                _base.drivetrain.turnTo.setParams(100.0 , .3 , 4_000)
                _base.drivetrain.turnTo.runSequentially()
                sleep(100)

                _base.jewelArm.setState(JewelArm.State.UP)
                sleep(750)

                _base.drivetrain.turnTo.setParams(90.0 , .5 , 4_000)
                _base.drivetrain.turnTo.runSequentially()
                sleep(100)
            }
            JewelStatus.GO_BACKWARD ->
            {
                _base.jewelArm.setState(JewelArm.State.DOWN)
                sleep(750)

                _base.drivetrain.driveTo.setParams(-3.0 , .3 , 3_000)
                _base.drivetrain.driveTo.runSequentially()
                sleep(100)

                _base.jewelArm.setState(JewelArm.State.UP)
                sleep(750)

                _base.drivetrain.turnTo.setParams(90.0 , .5 , 4_000)
                _base.drivetrain.turnTo.runSequentially()

                sleep(100)
            }
        }

        if(jewelStatus == JewelStatus.GO_BACKWARD)
        {
            _base.drivetrain.driveTo.setParams(-21.0 , .75 , 5_000)
        }
        else
        {
            _base.drivetrain.driveTo.setParams(-24.0 , .75 , 5_000)
        }

        _base.drivetrain.driveTo.runSequentially()
        sleep(100)

        _base.drivetrain.turnTo.setParams(180.0 , .75 , 7_500)
        _base.drivetrain.turnTo.runSequentially()
        sleep(100)


        // Run backwards to align
        _base.drivetrain.driveForTime.setParams(1_250 , -.50)
        _base.drivetrain.driveForTime.runSequentially()
        sleep(100)


        // Go forward various amounts depending on VuMark
        val NEAR_COL_DISTANCE = 18.0
        val COL_SEPARATION = 8.5


        when(vuMark)
        {
            RelicRecoveryVuMark.LEFT ->
            {
                _base.drivetrain.driveTo.setParams(NEAR_COL_DISTANCE , .5 , 3_000)
            }

            RelicRecoveryVuMark.CENTER ->
            {
                _base.drivetrain.driveTo.setParams(NEAR_COL_DISTANCE + COL_SEPARATION , .5 , 3_000)
            }

            RelicRecoveryVuMark.RIGHT ->
            {
                _base.drivetrain.driveTo.setParams(NEAR_COL_DISTANCE + 2 * COL_SEPARATION , .5 , 3_000)
            }

            RelicRecoveryVuMark.UNKNOWN ->
            {
                _base.drivetrain.driveTo.setParams(NEAR_COL_DISTANCE + 2 * COL_SEPARATION , .5 , 3_000)
            }

            else ->
            {
                _base.drivetrain.driveTo.setParams(NEAR_COL_DISTANCE + 2 * COL_SEPARATION , .5 , 3_000)
            }
        }

        _base.drivetrain.driveTo.runSequentially()
        sleep(100)

        _base.drivetrain.turnTo.setParams(270.0 , .60 , 3_000)
        _base.drivetrain.turnTo.runSequentially()
        sleep(100)

        _base.glyphGrabber.activateForTime.setParams(1_000 , GlyphGrabber.State.OUTPUT)
        _base.glyphGrabber.activateForTime.runSequentially()
        sleep(100)


        _base.drivetrain.driveForTime.setParams(2_000 , .3)
        _base.drivetrain.driveForTime.runSequentially()
        sleep(100)

        _base.drivetrain.driveTo.setParams(-9.0 , .3 , 1_000)
        _base.drivetrain.driveTo.runSequentially()
        sleep(100)

        _base.drivetrain.turnTo.setParams(145.0 , .75 , 3_000)
        _base.drivetrain.turnTo.runSequentially()


        if(vuMark == RelicRecoveryVuMark.RIGHT || vuMark == RelicRecoveryVuMark.UNKNOWN)
        {
            sleep(100)
            _base.drivetrain.driveTo.setParams(-4.0 , .3 , 2_000)
            _base.drivetrain.driveTo.runSequentially()
        }

        _base.drivetrain.setZeroPowerMode(DcMotor.ZeroPowerBehavior.FLOAT)

        telemetry.update()
    }
}