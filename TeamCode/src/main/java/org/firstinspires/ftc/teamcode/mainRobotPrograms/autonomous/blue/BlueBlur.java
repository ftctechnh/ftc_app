package org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.AutonomousBase;

@Autonomous(name="Blue Blur", group = "Auto Group")

public class BlueBlur extends AutonomousBase
{
    //Called after runOpMode() has finished initializing by BaseFunctions.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Drive until we are just far enough from the cap ball to score reliably.
        outputNewLineToDrivers("Driving forward to the cap ball to score...");
        startToDriveAt (0.5);
        while(frontRangeSensor.cmUltrasonic() > 40)
            adjustMotorPowersBasedOnGyroSensor();
        stopDriving();

        //Shoot the balls into the center vortex.
        outputNewLineToDrivers("Shooting balls into center vortex...");
        flywheels.setPower(0.33);
        sleep(300);
        harvester.setPower(-1.0);
        sleep(2500);
        flywheels.setPower(0);
        harvester.setPower(0);

        //Turn to face the wall directly.
        outputNewLineToDrivers("Turning to face wall at an angle...");
        turnToHeading(70, TurnMode.BOTH, 3700);

        //Drive to the wall and stop once a little ways away.
        startToDriveAt (0.7);
        outputNewLineToDrivers("Driving to wall before turn...");
        while (frontRangeSensor.cmUltrasonic () > 68)
            adjustMotorPowersBasedOnGyroSensor ();
        startToDriveAt (0.37);
        while (frontRangeSensor.cmUltrasonic () > 27)
            adjustMotorPowersBasedOnGyroSensor ();
        stopDriving ();

        //Turn back to become parallel with the wall.
        outputNewLineToDrivers("Turning to become parallel to the wall...");
        turnToHeading(0, TurnMode.BOTH, 3700);

        //For each of the two beacons.
        for (int i = 0; i < 2; i++)
        {
            outputNewLineToDrivers ("Looking for beacon " + (i + 1));

            //Set movement speed.
            startToDriveAt (0.37);

            //Drive until centered on the beacon.
            boolean aboutToSeeWhiteLine = false;
            long lastSwerveTime = System.currentTimeMillis ();

            /***** SWERVE CORRECTION ******/
            int tooFarThreshold = 18, tooCloseThreshold = 5;
            while (bottomColorSensor.alpha() <= 5)
            {
                //Slow down if we are really close to hitting the white line so that we definitely see it.
                if (!aboutToSeeWhiteLine)
                {
                    updateColorSensorStates ();
                    if (option1Red || option1Blue)
                    {
                        startToDriveAt (0.33); //Not useful right now but may be at one point soon when we decide to change it.
                        aboutToSeeWhiteLine = true;
                    }
                }

                double distanceFromWall = sideRangeSensor.cmUltrasonic ();
                if (distanceFromWall >= 255) //This is invalid, it can't be that far away.
                {
                    distanceFromWall = 16;
                }

                //Will be -1 if we need to swerve toward the wall and +1 if we need to swerve away from the wall.
                int swerveCorrectionSign = (int) (Math.signum (distanceFromWall - Range.clip(distanceFromWall, tooCloseThreshold, tooFarThreshold)));

                if ((System.currentTimeMillis () - lastSwerveTime) > 1500 && swerveCorrectionSign != 0 && !aboutToSeeWhiteLine)
                {
                    outputNewLineToDrivers ("SWERVE!  Current = " + System.currentTimeMillis () + " and last " + lastSwerveTime);

                    //Power up the appropriate side to a massive degree for a very short period of time.
                    if (swerveCorrectionSign == -1)
                        setRightPower (movementPower * 4);
                    else if (swerveCorrectionSign == 1)
                        setLeftPower (movementPower * 4);

                    gyroAdjustFactor -= swerveCorrectionSign; //Make the gyro think that heading 1 is now heading 0 since apparently the last heading was off.

                    //Don't allow the gyro to correct the heading for a very brief period of time.
                    long swerveStart = System.currentTimeMillis ();
                    while (System.currentTimeMillis () - swerveStart < 220)
                    {
                        if (bottomColorSensor.alpha () > 5)
                        {
                            stopDriving ();
                            turnToHeading (0, TurnMode.BOTH, 1000); //The swerve sets us off course to a large degree.
                            break;
                        }

                        idle();
                    }

                    lastSwerveTime = System.currentTimeMillis (); //Swerves can't occur one after another right away.
                    idle();
                }
                else
                    //Adjust motors based on gyro to remain parallel to wall.
                    adjustMotorPowersBasedOnGyroSensor ();
            }
            stopDriving ();

            outputNewLineToDrivers ("Ahoy there!  Beacon spotted!  Option 1 is " + (option1Blue ? "blue" : "red") + " and option 2 is " + (option2Blue ? "blue" : "red"));

            //While the beacon is not completely blue (this is the verification step).
            int failedAttempts = 0; //The robot tries different drive lengths for each trial.
            updateColorSensorStates ();
            while (! (option1Blue && option2Blue))
            {
                outputNewLineToDrivers ("Beacon is not completely blue, attempting to press the correct color!");

                boolean driveBackwardsToRecenter;

                //The possible events that could occur upon either verification or first looking at the beacon.
                //Different drives are attempted for each trial.
                if (option1Blue && option2Red)
                {
                    outputNewLineToDrivers ("Chose option 1");
                    //Use the option 1 button pusher.
                    driveForDistance (0.33, 90 + 10 * failedAttempts);
                    pressButton();
                    driveBackwardsToRecenter = true;
                }
                else if (option1Red && option2Blue)
                {
                    outputNewLineToDrivers ("Chose option 2");
                    //Use the option 2 button pusher.
                    driveForDistance (-0.33, 130 + 10 * failedAttempts);
                    pressButton();
                    driveBackwardsToRecenter = false;
                }
                else if (option1Red && option2Red)
                {
                    failedAttempts = 0;
                    outputNewLineToDrivers ("Neither option is blue, toggling beacon!");
                    //Toggle beacon.
                    driveForDistance (0.33, 80 + 10 * failedAttempts);
                    pressButton();
                    driveBackwardsToRecenter = true;
                }
                else
                {
                    failedAttempts = -1; //This will be incremented and returned to 0, fear not.
                    outputNewLineToDrivers("Run provided weird booleans!  Attempting reset!");
                    driveForDistance (0.33, 150); //Do something to try and find the correct values, try and re-center self by some miracle.
                    driveBackwardsToRecenter = true;
                }

                //On occasion this does happen for some reason, in which all are false or something.  Sometimes they shift back to being valid, however.
                //Set the movement power based on the direction we have to return to.
                startToDriveAt ((driveBackwardsToRecenter ? -1 : 1) * 0.33);
                while (bottomColorSensor.alpha() <= 5)
                    adjustMotorPowersBasedOnGyroSensor();
                stopDriving();

                //Update the number of trials completed so that we know the new drive distance and such.
                failedAttempts++;

                //Update beacon states to check loop condition.
                updateColorSensorStates ();
            }

            outputNewLineToDrivers ("Success!  Beacon is completely blue.");

            //Drive a bit forward from the white line to set up for the next step.
            if (i == 0)
                driveForDistance (0.37, 300);
        }

        //Dash backward to the ramp afterward.
        outputNewLineToDrivers ("Knocking the cap ball off of the pedestal...");
        turnToHeading(48, TurnMode.BOTH, 2200);
        driveForDistance(-1.0, 3000); //SPRINT TO THE CAP BALL TO PARK

    }
}