package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.OpModes.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.directcurrent.core.AutoStopper;
import org.directcurrent.opencv.CVBridge;
import org.directcurrent.season.relicrecovery.jewelarm.JewelArm;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.Vuforia;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Base;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.GlyphGrabber.GlyphGrabber;


@Autonomous(name = "Front Side Red")
public class AutoFrontSideRed extends LinearOpMode
{
    private Base _base = new Base();
    private AutoStopper _autoStopper = new AutoStopper(this , _base);
    private Vuforia _vuforia = new Vuforia();


    /**
     * Status of jewel detection
     */
    private enum JewelStatus
    {
        GO_FORWARD ,
        GO_BACKWARD ,
        UNCERTAIN
    }


    @Override
    public void runOpMode() throws InterruptedException
    {
        RelicRecoveryVuMark vuMark;
        JewelStatus jewelStatus;


        // Some boilerplate initialization
        _base.init(hardwareMap , this);

        _base.imu.calibrateTo(95);

        _vuforia.init(hardwareMap , VuforiaLocalizer.CameraDirection.BACK);
        _vuforia.activate();


        waitForStart();

        _autoStopper.startChecking();

        _base.drivetrain.setZeroPowerMode(DcMotor.ZeroPowerBehavior.BRAKE);
        vuMark = _vuforia.currentMarker();
        _vuforia.deactivate();

        // Turn to get the jewels
        _base.drivetrain.turnTo.setParams(75.0, .4 , 4_000);
        _base.drivetrain.turnTo.runSequentially();

        CVBridge.openCvRunner.toggleShowHide();
        CVBridge.openCvRunner.toggleAnalyze();

        // Sleep this long to give OpenCV a chance to see the jewels
        sleep(2_000);


        /*
            Keep trying this until it works- because it sometimes won't work.

            This determines which jewel to knock off the stand thing
         */
        for(;;)
        {
            try
            {
                // Jewel color decisions
                if (CVBridge.redJewelPoints.size() == 0 && CVBridge.blueJewelPoints.size() == 0)
                {
                    jewelStatus = JewelStatus.UNCERTAIN;
                }
                // It's backwards because the phone is upside down
                else if (CVBridge.redJewelPoints.get(0).y() < CVBridge.blueJewelPoints.get(0).y())
                {
                    jewelStatus = JewelStatus.GO_BACKWARD;
                }
                else
                {
                    jewelStatus = JewelStatus.GO_FORWARD;
                }

                break;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }   // End for loop

        // Close OpenCV for battery saving
        CVBridge.openCvRunner.toggleShowHide();


        switch(jewelStatus)
        {
            /*
                Straighten out robot
             */
            case UNCERTAIN:
                _base.drivetrain.turnTo.setParams(90.0 , .4 , 4_000);
                _base.drivetrain.turnTo.runSequentially();
                sleep(100);
                break;


            /*
                Lower arm, rotate counter-clockwise to knock jewel off, raise arm, straighten
                out
             */
            case GO_FORWARD:
                _base.jewelArm.setState(JewelArm.State.DOWN);
                sleep(750);

                _base.drivetrain.turnTo.setParams(100.0 , .3 , 4_000);
                _base.drivetrain.turnTo.runSequentially();
                sleep(100);

                _base.jewelArm.setState(JewelArm.State.UP);
                sleep(750);

                _base.drivetrain.turnTo.setParams(90.0 , .5 , 4_000);
                _base.drivetrain.turnTo.runSequentially();
                sleep(100);
                break;


            /*
                Lower arm, drive slightly backward to knock jewel off, drive back to original spot,
                raise arm, straighten out
             */
            case GO_BACKWARD:
                _base.jewelArm.setState(JewelArm.State.DOWN);
                sleep(750);

                _base.drivetrain.driveTo.setParams(-3.0 , .25 , 3_000);
                _base.drivetrain.driveTo.runSequentially();
                sleep(200);

                _base.jewelArm.setState(JewelArm.State.UP);
                sleep(750);

                _base.drivetrain.turnTo.setParams(90.0 , .5 , 4_000);
                _base.drivetrain.turnTo.runSequentially();

                sleep(100);
                break;
        }   // End switch


        _base.drivetrain.driveTo.setParams(25.0 , 1.0 , 3_000);
        _base.drivetrain.driveTo.runSequentially();
        sleep(100);

        // Drive backwards into the stone for alignment and constant distance
        _base.drivetrain.driveForTime.setParams(750 , -.75);
        _base.drivetrain.driveForTime.runSequentially();
        sleep(100);

        _base.drivetrain.driveTo.setParams(5.0 , .5 , 2_000);
        _base.drivetrain.driveTo.runSequentially();
        sleep(100);

        _base.drivetrain.turnTo.setParams(90 , .65 , 2_000);
        _base.drivetrain.turnTo.runSequentially();
        sleep(100);




        final double NEAR_COL_DISTANCE = 3.5;
        final double COL_SEPARATION = 8.5;


        switch(vuMark)
        {
            case RIGHT:
                _base.drivetrain.driveTo.setParams(NEAR_COL_DISTANCE , .5 , 3_000);
                break;

            case CENTER:
                _base.drivetrain.driveTo.setParams(NEAR_COL_DISTANCE + COL_SEPARATION , .5 , 3_000);
                break;

            case LEFT:
                // Fall through intentional

            case UNKNOWN:
                _base.drivetrain.driveTo.setParams(NEAR_COL_DISTANCE + 2 * COL_SEPARATION , .5 , 3_000);
                break;
        }

        _base.drivetrain.driveTo.runSequentially();
        sleep(100);

        _base.drivetrain.turnTo.setParams(0 , .60 , 3_000);
        _base.drivetrain.turnTo.runSequentially();
        sleep(100);

        _base.glyphGrabber.activateForTime.setParams(1_000 , GlyphGrabber.State.OUTPUT);
        _base.glyphGrabber.activateForTime.runSequentially();

        _base.drivetrain.driveForTime.setParams(1_000 , .3);
        _base.drivetrain.driveForTime.runSequentially();
        sleep(100);

        _base.drivetrain.driveTo.setParams(-10 , .3 , 1_000);
        _base.drivetrain.driveTo.runSequentially();
        sleep(100);

        _base.drivetrain.turnTo.setParams(180 , .75 , 3_000);
        _base.drivetrain.turnTo.runSequentially();
        sleep(100);


        // Multi-glyph part
        _base.glyphGrabber.activateForTime.setParams(7_000 , GlyphGrabber.State.INPUT);
        _base.glyphGrabber.activateForTime.runParallel();

        _base.drivetrain.driveTo.setParams(25 , 1 , 2_000);
        _base.drivetrain.driveTo.runSequentially();
        sleep(100);


        // Sweep back and forth to increase chances of glyph collection
        _base.drivetrain.turnTo.setParams(145 , 1 , 1_000);
        _base.drivetrain.turnTo.runSequentially();
        sleep(100);

        _base.drivetrain.turnTo.setParams(215 , 1 , 1_000);
        _base.drivetrain.turnTo.runSequentially();
        sleep(100);

        _base.drivetrain.turnTo.setParams(180 , .75 , 3_000);
        _base.drivetrain.turnTo.runSequentially();
        sleep(100);

        _base.drivetrain.driveForTime.setParams(750 , -1);
        _base.drivetrain.driveForTime.runSequentially();
        sleep(100);

        _base.drivetrain.turnTo.setParams(0 , .75 , 3_000);
        _base.drivetrain.turnTo.runSequentially();
        sleep(100);


        _base.drivetrain.driveForTime.setParams(500 , 1);
        _base.drivetrain.driveForTime.runSequentially();
        sleep(100);


        _base.drivetrain.driveForTime.setParams(200 , -1);
        _base.drivetrain.driveForTime.runSequentially();
        sleep(100);


//        _base.drivetrain.driveTo.setParams(28 , 1 , 3_000);
//        _base.drivetrain.driveTo.runSequentially();
//        sleep(100);
//
//        _base.drivetrain.turnTo.setParams(0 , .75 , 2_000);
//        _base.drivetrain.turnTo.runSequentially();
//        sleep(100);

        _base.drivetrain.setZeroPowerMode(DcMotor.ZeroPowerBehavior.FLOAT);
    }
}
