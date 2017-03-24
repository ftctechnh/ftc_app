package org.firstinspires.ftc.teamcode.autonomous.maintypes;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.autonomous.OnAlliance;
import org.firstinspires.ftc.teamcode.programflow.ConsoleManager;

public abstract class BeaconAuto extends AutoBase implements OnAlliance
{
    private final double BEACON_DP = 1;

    @Override
    protected void driverStationSaysINITIALIZE() throws InterruptedException
    {
        getAlliance ();
    }

    //Called after runOpMode() has finished initializing by ImprovedOpModeBase.
    protected void driverStationSaysGO () throws InterruptedException
    {
        Alliance alliance = getAlliance ();

        //Results in a coefficient of 1 if doing blue, and -1 for red.
        boolean onBlueAlliance = (alliance == Alliance.BLUE);
        int autonomousSign = (onBlueAlliance ? 1 : -1);

        /******** STEP 1: SHOOT, DRIVE, TURN TO BE PARALLEL WITH WALL ********/

        //Drive until we are just far enough from the cap ball to score reliably.
        ConsoleManager.outputNewLineToDrivers("Driving forward to the cap ball to score...");
        drive(SensorStopType.Ultrasonic, 39, PowerUnits.RevolutionsPerSecond, 2);

        //Shoot the balls into the center vortex.
        ConsoleManager.outputNewLineToDrivers("Shooting balls into center vortex...");
        shootBallsIntoCenterVortex ();

        //Turn to face the wall directly.
        ConsoleManager.outputNewLineToDrivers("Turning to face wall at an angle...");
        turnToHeading(73 * autonomousSign, TurnMode.BOTH, 3000);

        //Drive to the wall and stop once a little ways away.
        ConsoleManager.outputNewLineToDrivers ("Driving to the wall...");
        drive (SensorStopType.Ultrasonic, (onBlueAlliance ? 31 : 36), PowerUnits.RevolutionsPerSecond, 1); //Based on the way the range sensor is angled.

        //Turn back to become parallel with the wall.
        ConsoleManager.outputNewLineToDrivers("Turning to become parallel to the wall...");
        turnToHeading(onBlueAlliance ? 0 : -180, TurnMode.BOTH, 3000);

        //For each of the two beacons.
        for (int currentBeacon = 1; currentBeacon <= 2; currentBeacon++)
        {
            /******** STEP 2: FIND AND CENTER SELF ON BEACON ********/

            ConsoleManager.outputNewLineToDrivers ("Looking for beacon " + currentBeacon);
            drive(SensorStopType.BottomColorAlpha, 5, PowerUnits.RevolutionsPerSecond, BEACON_DP);

            //Extend the button pusher until the colors become distinctly visible.
            long extensionStartTime = System.currentTimeMillis ();
            rightButtonPusher.setPosition (0); //Start button pusher extension.
            while (!((option1Red && option2Blue) || (option1Blue && option2Red)))
            {
                updateColorSensorStates ();
                idle();
            }
            rightButtonPusher.setPosition (.5); //Stop button pusher.
            long timeTakenToSeeColors = System.currentTimeMillis () - extensionStartTime;

            //Press and retract as many times as necessary.

            //Retract almost completely and then drive to the next one.


            /******** STEP 3: PRESS AND VERIFY THE BEACON!!!!! ********/

            ConsoleManager.outputNewLineToDrivers ("Ahoy there!  Beacon spotted!  Option 1 is " + (option1Blue ? "blue" : "red") + " and option 2 is " + (option2Blue ? "blue" : "red"));

            //While the beacon is not completely blue (this is the verification step).
            int failedAttempts = 0; //The robot tries different drive lengths for each trial.
            updateColorSensorStates (); //Has to know the initial colors.

            leftDrive.resetEncoder (); //Does this twice in total to prevent time loss.
            rightDrive.resetEncoder ();

            while (onBlueAlliance ? (!(option1Blue && option2Blue)) : (!(option1Red && option2Red)))
            {
                ConsoleManager.outputNewLineToDrivers ("Beacon is not completely blue, attempting to press the correct color!");

                //The possible events that could occur upon either verification or first looking at the beacon.
                //Different drives are attempted for each trial.
                int driveSign = 1;
                if (option1Blue && option2Red)
                {
                    ConsoleManager.outputNewLineToDrivers ("Chose option 1");
                    driveSign = 1;
                    drive (SensorStopType.Distance, (onBlueAlliance ? 90 : 60) + 20 * failedAttempts, PowerUnits.RevolutionsPerSecond, BEACON_DP);
                    pressButton ();
                }
                else if (option1Red && option2Blue)
                {
                    ConsoleManager.outputNewLineToDrivers ("Chose option 2");
                    driveSign = -1;
                    drive (SensorStopType.Distance, (onBlueAlliance ? 150 : 130) + 20 * failedAttempts, PowerUnits.RevolutionsPerSecond, -BEACON_DP);
                    pressButton ();
                }
                else if (option1Blue ? (option1Red && option2Red) : (option1Blue && option2Blue))
                {
                    ConsoleManager.outputNewLineToDrivers ("Neither option is the correct color, toggling beacon!");
                    driveSign = -1;
                    drive (SensorStopType.Distance, (onBlueAlliance ? 90 : 60) + 20 * failedAttempts, PowerUnits.RevolutionsPerSecond, -BEACON_DP);
                    pressButton ();
                }
                else
                {
                    failedAttempts = -1; //This will be incremented and returned to 0, fear not.
                    ConsoleManager.outputNewLineToDrivers ("Can't see the beacon clearly, so double checking!");
                    driveSign = 1;
                    drive (SensorStopType.Distance, 100 + 20 * failedAttempts, PowerUnits.RevolutionsPerSecond, .35);
                }

                //Drive back to the original value.
                drive(SensorStopType.BottomColorAlpha, 5, PowerUnits.RevolutionsPerSecond, Math.signum(autonomousSign * driveSign) * BEACON_DP);

                //Update the number of trials completed so that we know the new drive distance and such.
                failedAttempts++;

                //Update beacon states to check loop condition.
                updateColorSensorStates ();
            }

            ConsoleManager.outputNewLineToDrivers ("Success!  The beacon is completely blue.");

            drive (SensorStopType.Distance, 500, PowerUnits.RevolutionsPerSecond, -BEACON_DP);
        }


        /******** STEP 4: PARK AND KNOCK OFF THE CAP BALL ********/

        //Dash backward to the ramp afterward.
        ConsoleManager.outputNewLineToDrivers ("Knocking the cap ball off of the pedestal...");
        turnToHeading(36 * autonomousSign - (onBlueAlliance ? 0 : 180), TurnMode.BOTH, 2000);
        drive (SensorStopType.Distance, 3000, PowerUnits.RevolutionsPerSecond, -BEACON_DP);
    }

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
        ConsoleManager.outputNewLineToDrivers ("Extending the button pusher for " + extendLength + " ms.");

        //Run the continuous rotation servo out to press, then back in.
        rightButtonPusher.setPosition(0);
        sleep((long) (extendLength));
        rightButtonPusher.setPosition(1);
        sleep((long) (extendLength - 200));
        rightButtonPusher.setPosition(.5);
    }
}