package org.firstinspires.ftc.robotcontroller.external.samples.Team9620;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 *
 */
@Autonomous(name = "Concept: Blue Test Encoder Drive", group = "Concept")
@Disabled
public class AutoOpEncoderDriveTest extends LinearOpMode {

    public static final String TAG = "TestBot Blue Encoder Drive Test Nav OpMode";

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();

    @Override public void runOpMode() throws InterruptedException {

        /**
         * For Vuforia recommend field and bot dimensions in mm  because vuforia trackables are in mm
         * so using mm simplifies the math.  Use Convert for convenience so you can specify in uint of choice.
         *
         *
         *
         *                            -X
         *     red             gears          tools
         *         +--------------O----+---------O---------+
         *         ||  /               |                  /|
         *         ||/                 |                /  |
         *         |                   |             /     |
         *  +----+ |                   |          /        O legos
         *  |red | |                   |       /           |
         *  |    | |                   |    /            x |
         *  |    | |                +--+--+              x |
         *  | -Y | +----------------+  +  +-------------x-+   Y
         *  |    | |                +--+--+           x    |
         *  |    | |              /    |       x  x x      O wheels
         *  |    | |           /       |   x               |
         *  +----+ |        /          |x                  |
         *         |     /            x|                   |
         *         |  /              x |                 /||
         *         |/                x |               /  ||
         *         +-------------------+-------------------+
         *                  +---------------------+           blue
         *                  |          X   blue   |
         *                  +---------------------+
         *
         *
         *
         */

        /** Initialize encoder drive.*/
        EncoderDrive eDrive = new EncoderDrive();
        eDrive.initializeForOpMode( this, hardwareMap );

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start driving");
        telemetry.update();

        /** Wait for the game to start (driver presses PLAY) */
        waitForStart();

        String pathInfo = new String();
        // drive forward 12 inches
        eDrive.encoderDriveBase( 0.2, 12.0, 12.0, 3.0);
        pathInfo += "DF 12.0\"\n";
        telemetry.addData( "Path", pathInfo);

        // pivot turn right 45 deg
        eDrive.encoderPivotTurn( 0.25, -45.0, 3.0); // CW is negative
        pathInfo += "PT.Rt 45.0d\n";
        telemetry.addData( "Path", pathInfo);

        // drive forward 36 inches
        eDrive.encoderDriveStraight( 0.25, 36.0, true, 5.0);
        pathInfo += "DF 36.0\"\n";
        telemetry.addData( "Path", pathInfo);

        // pivot turn right 45 deg
        eDrive.encoderPivotTurn( 0.25, -45.0, 3.0); // CW is negative
        pathInfo += "PT.Rt 45.0d\n";
        telemetry.addData( "Path", pathInfo);

        // drive forward 12 inches
        eDrive.encoderDriveStraight( 0.25, 12, true, 3.0);
        pathInfo += "DF 12.0\"\n";
        telemetry.addData( "Path", pathInfo);

        // arc turn forward 90 deg to the left with arch turn radius of 24 inches to center
        eDrive.encoderArcTurnLeft( 0.25, 24, 90, true, 3.0);
        pathInfo += "AT.left R 24.0\" A 90.0d\n";
        telemetry.addData( "Path", pathInfo);

        while (opModeIsActive()) {
            idle();
        }
    }
}
