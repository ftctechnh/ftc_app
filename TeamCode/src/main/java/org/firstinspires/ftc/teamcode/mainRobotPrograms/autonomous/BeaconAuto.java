package org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous;

public abstract class BeaconAuto extends AutoBase
{
    private final double BEACON_DP = 0.27;

    //Called after runOpMode() has finished initializing by BaseFunctions.
    protected void runBeaconAutonomous (Alliance alliance) throws InterruptedException
    {
        //Results in a coefficient of 1 if doing blue, and -1 for red.
        boolean onBlueAlliance = (alliance == Alliance.BLUE);
        int autonomousSign = (onBlueAlliance ? 1 : -1);

        /******** STEP 1: SHOOT, DRIVE, TURN TO BE PARALLEL WITH WALL ********/

        //Drive until we are just far enough from the cap ball to score reliably.
        outputNewLineToDrivers("Driving forward to the cap ball to score...");
        driveUntilDistanceFromObstacle (39, BEACON_DP);

        //Shoot the balls into the center vortex.
        outputNewLineToDrivers("Shooting balls into center vortex...");
        shootBallsIntoCenterVortex ();

        //Turn to face the wall directly.
        outputNewLineToDrivers("Turning to face wall at an angle...");
        turnToHeading(73 * autonomousSign, TurnMode.BOTH, 3000);

        //Drive to the wall and stop once a little ways away.
        outputNewLineToDrivers ("Driving to the wall...");
        driveUntilDistanceFromObstacle ((onBlueAlliance ? 31 : 36), BEACON_DP); //Based on the way the range sensor is angled.

        //Turn back to become parallel with the wall.
        outputNewLineToDrivers("Turning to become parallel to the wall...");
        turnToHeading(onBlueAlliance ? 0 : -180, TurnMode.BOTH, 3000);

        //For each of the two beacons.
        for (int currentBeacon = 1; currentBeacon <= 2; currentBeacon++)
        {
            /******** STEP 2: FIND AND CENTER SELF ON BEACON ********/

            outputNewLineToDrivers ("Looking for beacon " + currentBeacon);

            //Set movement speed.
            startDrivingAt (0.35 * autonomousSign);

            boolean aboutToSeeWhiteLine = false;
            while (bottomColorSensor.alpha () <= 5)
            {
                updateColorSensorStates ();

                //Adjust speed and such.
                if (!aboutToSeeWhiteLine)
                {
                    if (onBlueAlliance ? (option1Red || option1Blue) : (option2Red || option2Blue))
                    {
                        outputNewLineToDrivers ("Slowing down for beacon.");
                        hardBrake (100);
                        startDrivingAt (BEACON_DP * autonomousSign);
                        aboutToSeeWhiteLine = true;
                    }
                }

                //adjustDirectionBasedOnColorSensors ();
                applySensorAdjustmentsToMotors (true, true, true);
            }
            //Stop once centered on the beacon.
            hardBrake (150);


            /******** STEP 3: PRESS AND VERIFY THE BEACON!!!!! ********/

            outputNewLineToDrivers ("Ahoy there!  Beacon spotted!  Option 1 is " + (option1Blue ? "blue" : "red") + " and option 2 is " + (option2Blue ? "blue" : "red"));

            //While the beacon is not completely blue (this is the verification step).
            int failedAttempts = 0; //The robot tries different drive lengths for each trial.
            updateColorSensorStates (); //Has to know the initial colors.
            initializeAndResetEncoders (); //Does this twice in total to prevent time loss.

            while (onBlueAlliance ? (!(option1Blue && option2Blue)) : (!(option1Red && option2Red)))
            {
                outputNewLineToDrivers ("Beacon is not completely blue, attempting to press the correct color!");

                boolean driveBackwardsToRecenter;

                //The possible events that could occur upon either verification or first looking at the beacon.
                //Different drives are attempted for each trial.
                if (option1Blue && option2Red)
                {
                    outputNewLineToDrivers ("Chose option 1");
                    //Use the option 1 button pusher.
                    driveForDistance (BEACON_DP * autonomousSign, (onBlueAlliance ? 90 : 60) + 20 * failedAttempts);
                    pressButton ();
                    driveBackwardsToRecenter = autonomousSign > 0;
                }
                else if (option1Red && option2Blue)
                {
                    outputNewLineToDrivers ("Chose option 2");
                    //Use the option 2 button pusher.
                    driveForDistance (-BEACON_DP * autonomousSign, (onBlueAlliance ? 150 : 130) + 20 * failedAttempts);
                    pressButton ();
                    driveBackwardsToRecenter = autonomousSign < 0;
                }
                else if (option1Blue ? (option1Red && option2Red) : (option1Blue && option2Blue))
                {
                    outputNewLineToDrivers ("Neither option is the correct color, toggling beacon!");
                    //Toggle beacon.
                    driveForDistance (BEACON_DP * autonomousSign, (onBlueAlliance ? 90 : 60) + 20 * failedAttempts);
                    pressButton ();
                    driveBackwardsToRecenter = autonomousSign > 0;
                }
                else
                {
                    failedAttempts = -1; //This will be incremented and returned to 0, fear not.
                    outputNewLineToDrivers ("Can't see the beacon clearly, so double checking!");
                    driveForDistance (0.35, 100); //Do something to try and find the correct values, try and re-center self by some miracle.
                    driveBackwardsToRecenter = true;
                }

                //On occasion this does happen for some reason, in which all are false or something.  Sometimes they shift back to being valid, however.
                //Set the movement power based on the direction we have to return to.
                startDrivingAt ((driveBackwardsToRecenter ? -1 : 1) * BEACON_DP);

                while (bottomColorSensor.alpha () <= 5)
                {
                    //adjustDirectionBasedOnColorSensors ();

                    applySensorAdjustmentsToMotors (true, true, false);
                }

                hardBrake (100);

                //Update the number of trials completed so that we know the new drive distance and such.
                failedAttempts++;

                //Update beacon states to check loop condition.
                updateColorSensorStates ();
            }

            outputNewLineToDrivers ("Success!  The beacon is completely blue.");

            driveForDistance (0.42 * autonomousSign, 500); //Drive a bit forward from the white line to set up for the next step.
        }


        /******** STEP 4: PARK AND KNOCK OFF THE CAP BALL ********/

        //Dash backward to the ramp afterward.
        outputNewLineToDrivers ("Knocking the cap ball off of the pedestal...");
        turnToHeading(36 * autonomousSign - (onBlueAlliance ? 0 : 180), TurnMode.BOTH, 2000);
        driveForDistance(-1.0 * autonomousSign, 3000); //SPRINT TO THE CAP BALL TO PARK

    }

//    private void adjustDirectionBasedOnColorSensors() throws InterruptedException
//    {
//        updateColorSensorStates ();
//
//        if (movementPower > 0)
//        {
//            if (!(option1Red || option1Blue) && (option2Red || option2Blue))
//            {
//                hardBrake (150);
//                startDrivingAt (-BEACON_DP);
//                outputNewLineToDrivers ("Drove past beacon, swapping direction!");
//            }
//        }
//        else if (movementPower < 0)
//        {
//            if ((option1Red || option1Blue) && !(option2Red || option2Blue))
//            {
//                hardBrake (150);
//                startDrivingAt (BEACON_DP);
//                outputNewLineToDrivers ("Drove past beacon, swapping direction!");
//            }
//        }
//    }

    private void pressButton() throws InterruptedException
    {
        //Determine the length to push the pusher out based on the distance from the wall.
        double distanceFromWall = sideRangeSensor.cmUltrasonic ();
        if (distanceFromWall >= 255)
        {
            //Possible that this was a misreading.
            idle();
            distanceFromWall = sideRangeSensor.cmUltrasonic ();
            if (distanceFromWall >= 255) //It can't actually be 255.
                distanceFromWall = 20;
        }
        double extendLength = 67 * distanceFromWall;
        outputNewLineToDrivers ("Extending the button pusher for " + extendLength + " ms.");

        //Run the continuous rotation servo out to press, then back in.
        rightButtonPusher.setPosition(0);
        sleep((long) (extendLength));
        rightButtonPusher.setPosition(1);
        sleep((long) (extendLength - 200));
        rightButtonPusher.setPosition(.5);
    }
}