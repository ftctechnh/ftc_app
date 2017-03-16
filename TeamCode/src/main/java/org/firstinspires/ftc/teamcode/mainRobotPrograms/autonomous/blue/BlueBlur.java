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
        /******** STEP 1: SHOOT, DRIVE, TURN TO BE PARALLEL WITH WALL ********/

        //Drive until we are just far enough from the cap ball to score reliably.
        outputNewLineToDrivers("Driving forward to the cap ball to score...");
        driveUntilDistanceFromObstacle (40);

        //Shoot the balls into the center vortex.
        outputNewLineToDrivers("Shooting balls into center vortex...");
        shootBallsIntoCenterVortex ();

        //Turn to face the wall directly.
        outputNewLineToDrivers("Turning to face wall at an angle...");
        turnToHeading(73, TurnMode.BOTH, 3000);

        //Drive to the wall and stop once a little ways away.
        outputNewLineToDrivers ("Driving to the wall...");
        driveUntilDistanceFromObstacle (30);

        //Turn back to become parallel with the wall.
        outputNewLineToDrivers("Turning to become parallel to the wall...");
        turnToHeading(0, TurnMode.BOTH, 3000);

        //For each of the two beacons.
        for (int i = 0; i < 2; i++)
        {
            /******** STEP 2: FIND AND CENTER SELF ON BEACON ********/

            outputNewLineToDrivers ("Looking for beacon " + (i + 1));

            //Set movement speed.
            startDrivingAt (0.55);

            //Drive until centered on the beacon.
            boolean aboutToSeeWhiteLine = false;

            while (bottomColorSensor.alpha() <= 5)
            {
                //Slow down if we are really close to hitting the white line so that we are more likely to see it (only happens once)
                if (!aboutToSeeWhiteLine)
                {
                    updateColorSensorStates ();
                    if (option1Red || option1Blue)
                    {
                        //Brake
                        stopDriving ();
                        sleep(100);
                        startDrivingAt (0.30);
                        aboutToSeeWhiteLine = true;
                    }
                }

                applySensorAdjustmentsToMotors (true, true, true);
            }
            //Stop once centered on the beacon.
            stopDriving ();


            /******** STEP 3: PRESS AND VERIFY THE BEACON!!!!! ********/

            outputNewLineToDrivers ("Ahoy there!  Beacon spotted!  Option 1 is " + (option1Blue ? "blue" : "red") + " and option 2 is " + (option2Blue ? "blue" : "red"));

            //While the beacon is not completely blue (this is the verification step).
            int failedAttempts = 0; //The robot tries different drive lengths for each trial.
            updateColorSensorStates (); //Has to know the initial colors.
            initializeAndResetEncoders (); //Does this twice in total to prevent time loss.
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
                    driveForDistance (0.30, 90 + 10 * failedAttempts);
                    pressButton();
                    driveBackwardsToRecenter = true;
                }
                else if (option1Red && option2Blue)
                {
                    outputNewLineToDrivers ("Chose option 2");
                    //Use the option 2 button pusher.
                    driveForDistance (-0.30, 150 + 10 * failedAttempts);
                    pressButton();
                    driveBackwardsToRecenter = false;
                }
                else if (option1Red && option2Red)
                {
                    failedAttempts = 0;
                    outputNewLineToDrivers ("Neither option is blue, toggling beacon!");
                    //Toggle beacon.
                    driveForDistance (0.30, 90 + 10 * failedAttempts);
                    pressButton();
                    driveBackwardsToRecenter = true;
                }
                else
                {
                    failedAttempts = -1; //This will be incremented and returned to 0, fear not.
                    outputNewLineToDrivers("Can't see the beacon clearly, so double checking!");
                    driveForDistance (0.33, 100); //Do something to try and find the correct values, try and re-center self by some miracle.
                    driveBackwardsToRecenter = true;
                }

                //On occasion this does happen for some reason, in which all are false or something.  Sometimes they shift back to being valid, however.
                //Set the movement power based on the direction we have to return to.
                startDrivingAt ((driveBackwardsToRecenter ? -1 : 1) * 0.30);

                while (bottomColorSensor.alpha() <= 5)
                    applySensorAdjustmentsToMotors (true, true, false);

                stopDriving();

                //Update the number of trials completed so that we know the new drive distance and such.
                failedAttempts++;

                //Update beacon states to check loop condition.
                updateColorSensorStates ();
            }

            outputNewLineToDrivers ("Success!  The beacon is completely blue.");

            //Drive a bit forward from the white line to set up for the next step.
            if (i == 0)
                driveForDistance (0.55, 500);
        }


        /******** STEP 4: PARK AND KNOCK OFF THE CAP BALL ********/

        //Dash backward to the ramp afterward.
        outputNewLineToDrivers ("Knocking the cap ball off of the pedestal...");
        turnToHeading(48, TurnMode.BOTH, 2000);
        driveForDistance(-1.0, 3000); //SPRINT TO THE CAP BALL TO PARK

    }
}