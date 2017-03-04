package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="Blue Blur", group = "Auto Group")

public class BlueBlur extends _AutonomousBase
{
    //Called after runOpMode() has finished initializing by BaseFunctions.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Drive until we are just far enough from the cap ball to score reliably.
        outputNewLineToDrivers("Driving forward to the cap ball to score...");
        setMovementPower (.3);
        while(frontRangeSensor.cmUltrasonic() > 43)
            adjustMotorPowersBasedOnGyroSensor();
        stopDriving();

        //Shoot the balls into the center vortex.
        outputNewLineToDrivers("Shooting balls into center vortex...");
        flywheels.setPower(0.4);
        sleep(300);
        harvester.setPower(-1.0);
        sleep(2500);
        flywheels.setPower(0);
        harvester.setPower(0);

        //Turn to face the wall directly.
        outputNewLineToDrivers("Turning to face wall at an angle...");
        turnToHeading(75, TurnMode.BOTH, 3500);

        //Drive to the wall and stop once a little ways away.
        setMovementPower (0.7);
        outputNewLineToDrivers("Driving to wall before turn...");
        while (frontRangeSensor.cmUltrasonic () > 68)
            adjustMotorPowersBasedOnGyroSensor ();
        setMovementPower(0.17);
        while (frontRangeSensor.cmUltrasonic () > 46)
            adjustMotorPowersBasedOnGyroSensor ();
        stopDriving ();

        //Turn back to become parallel with the wall.
        outputNewLineToDrivers("Turning to become parallel to the wall...");
        turnToHeading(0, TurnMode.BOTH, 3500);

        //For each of the two beacons.
        for (int i = 0; i < 2; i++)
        {
            outputNewLineToDrivers ("Looking for beacon " + (i + 1));

            //Set movement speed.
            setMovementPower(0.17);

            //Drive until centered on the beacon.
            boolean aboutToSeeWhiteLine = false;
            long lastAdjustmentTime = System.currentTimeMillis ();
            while (bottomColorSensor.alpha() <= 5)
            {
                //Slow down if we are really close to hitting the white line so that we definitely see it.
                if (!aboutToSeeWhiteLine)
                {
                    updateColorSensorStates ();
                    if (option1Red || option1Blue)
                    {
                        setMovementPower (0.17);
                        aboutToSeeWhiteLine = true;
                    }
                }

                //Swerve correction.
                if (sideRangeSensor.cmUltrasonic () > 17 && (System.currentTimeMillis () - lastAdjustmentTime) > 700 && (!aboutToSeeWhiteLine))
                {
                    lastAdjustmentTime = System.currentTimeMillis ();
                    setLeftPower (Range.clip (movementPower + 0.7, -1, 1));
                    gyroAdjustFactor--;
                    long swerveStart = System.currentTimeMillis ();
                    while (System.currentTimeMillis () - swerveStart < 220)
                    {
                        if (bottomColorSensor.alpha () > 5)
                            break;
                        outputConstantDataToDrivers (
                                new String[]
                                        {
                                                "Swerving!"
                                        }
                        );
                        idle();
                    }
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
                    driveForDistance (0.17, 90 + 10 * failedAttempts);
                    pressButton();
                    driveBackwardsToRecenter = true;
                }
                else if (option1Red && option2Blue)
                {
                    outputNewLineToDrivers ("Chose option 2");
                    //Use the option 2 button pusher.
                    driveForDistance (-0.17, 130 + 10 * failedAttempts);
                    pressButton();
                    driveBackwardsToRecenter = false;
                }
                else if (option1Red && option2Red)
                {
                    failedAttempts = 0;
                    outputNewLineToDrivers ("Neither option is blue, toggling beacon!");
                    //Toggle beacon.
                    driveForDistance (0.17, 80 + 10 * failedAttempts);
                    pressButton();
                    driveBackwardsToRecenter = true;
                }
                else
                {
                    failedAttempts = -1; //This will be incremented and returned to 0, fear not.
                    outputNewLineToDrivers("Run provided weird booleans!  Attempting reset!");
                    driveForDistance (0.17, 150); //Do something to try and find the correct values, try and re-center self by some miracle.
                    driveBackwardsToRecenter = true;
                }

                //On occasion this does happen for some reason, in which all are false or something.  Sometimes they shift back to being valid, however.
                //Set the movement power based on the direction we have to return to.
                setMovementPower((driveBackwardsToRecenter ? -1 : 1) * 0.17);
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
                driveForDistance (0.3, 300);
        }

        //Dash backward to the ramp afterward.
        turnToHeading(48, TurnMode.BOTH, 2200);
        driveForDistance(-1.0, 3000); //SPRINT TO THE CAP BALL TO PARK

    }

    private void pressButton() throws InterruptedException
    {
        //Determine the length to push the pusher out based on the distance from the wall.
        double extendLength = 95 * sideRangeSensor.cmUltrasonic();
        extendLength = Range.clip(extendLength, 0, 3000);
        outputNewLineToDrivers ("Extending the button pusher for " + extendLength + " ms.");

        //Run the continuous rotation servo out to press, then back in.
        rightButtonPusher.setPosition(.2);
        sleep((long) (extendLength));
        rightButtonPusher.setPosition(.8);
        sleep((long) (extendLength - 30));
        rightButtonPusher.setPosition(.5);
    }
}