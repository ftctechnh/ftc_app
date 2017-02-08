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
         * field and bot dimensions in mm - recommended because vuforia trackables are in mm
         * so using mm simplifies the math.  Use Convert for convenience so you can specify in uint of choice.
         *
         * NOTE: this diagram needs to be updated once we identify proper locations,
         * as do the rotation angles and translation distances below.
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
         *  |    | |                   |    /              |
         *  |    | |                +--+--+                |
         *  | -Y | +----------------+  +  +----------------+   Y
         *  |    | |                +--+--+                |
         *  |    | |              /    |       x  x x x x  O wheels
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

        do {
            // drive forward 18 inches
            if( !opModeIsActive()) {break;}
            eDrive.encoderDriveBase(this, 0.2, 16.0, 16.0, 3.0);

            // pivot turn right 45 deg
            if( !opModeIsActive()) {break;}
            eDrive.encoderPivotTurn(this, 0.25, 45.0, 3.0); // CW is negative

            // drive forward 48 inches
            if( !opModeIsActive()) {break;}
            eDrive.encoderDriveStraight(this, 0.25, 48.0, true, 5.0);

            // pivot turn right 45 deg
            if( !opModeIsActive()) {break;}
            eDrive.encoderPivotTurn(this, 0.25, 45.0, 3.0); // CW is negative

            // drive forward 12 inches
            if( !opModeIsActive()) {break;}
            eDrive.encoderDriveStraight(this, 0.25, 12, true, 3.0);

            // arc turn forward 90 deg to the left with arch turn radius of 24 inches to center
            if( !opModeIsActive()) {break;}
            eDrive.encoderArcTurnLeft(this, 0.25, 24, 90, true, 3.0);

        } while (false);

        while (opModeIsActive()) {
            idle();
        }
    }
}
