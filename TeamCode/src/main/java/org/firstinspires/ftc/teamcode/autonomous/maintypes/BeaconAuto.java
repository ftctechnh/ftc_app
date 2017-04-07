package org.firstinspires.ftc.teamcode.autonomous.maintypes;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.autonomous.OnAlliance;
import org.firstinspires.ftc.teamcode.debugging.ConsoleManager;
import org.firstinspires.ftc.teamcode.threading.ProgramFlow;

public abstract class BeaconAuto extends AutoBase implements OnAlliance
{
    //Called after runOpMode() has finished initializing by ImprovedOpModeBase.
    protected void driverStationSaysGO () throws InterruptedException
    {
        //The ideal distance @ which to drive.
        final double BEACON_DP = 1.5;

        //Results in a coefficient of 1 if doing blue, and -1 for red.
        boolean onBlueAlliance = (getAlliance () == Alliance.BLUE);
        int autonomousSign = (onBlueAlliance ? 1 : -1);

        /******** STEP 1: SHOOT, DRIVE, TURN TO BE PARALLEL WITH WALL ********/

        //Drive until we are just far enough from the cap ball to score reliably.
        ConsoleManager.outputNewLineToDrivers("Driving forward to the cap ball to score...");
        drive(SensorStopType.Ultrasonic, 39, PowerUnits.RevolutionsPerSecond, 1);

        //Shoot the balls into the center vortex.
        ConsoleManager.outputNewLineToDrivers("Shooting balls into center vortex...");
        shootBallsIntoCenterVortex ();

        //Turn to face the wall directly.
        ConsoleManager.outputNewLineToDrivers("Turning to face wall at an angle...");
        turnToHeading(73 * autonomousSign, TurnMode.BOTH, 3000);

        //Drive to the wall and stop once a little ways away.
        ConsoleManager.outputNewLineToDrivers ("Driving to the wall...");
        drive (SensorStopType.Ultrasonic, (onBlueAlliance ? 31 : 36), PowerUnits.RevolutionsPerSecond, 1);

        //Turn back to become parallel with the wall.
        ConsoleManager.outputNewLineToDrivers("Turning to become parallel to the wall...");
        turnToHeading(onBlueAlliance ? 0 : -180, TurnMode.BOTH, 3000);

        //For each of the two beacons.
        for (int currentBeacon = 1; currentBeacon <= 2; currentBeacon++)
        {
            /******** STEP 2: FIND AND CENTER SELF ON BEACON ********/

            //Drive up to the line.
            ConsoleManager.outputNewLineToDrivers ("Looking for beacon " + currentBeacon);
            drive(SensorStopType.BottomColorAlpha, 5, PowerUnits.RevolutionsPerSecond, BEACON_DP);

            //Extend the button pusher until the colors become distinctly visible.
            ConsoleManager.outputNewLineToDrivers ("Extending color sensing apparatus to see the beacon colors.");
            long extensionStartTime = System.currentTimeMillis ();
            rightButtonPusher.setServoPosition (0); //Start button pusher extension.

            do
            {
                updateColorSensorStates ();
                ProgramFlow.pauseForSingleFrame ();
            } while (!((option1Red && option2Blue) || (option1Blue && option2Red)));

            rightButtonPusher.setServoPosition (.5); //Stop button pusher.
            long timeTakenToSeeColors = System.currentTimeMillis () - extensionStartTime;

            //Press and retract as many times as necessary.
            long timeForEachPress = (long) (sideRangeSensor.getVALIDDistCM () * 67) - timeTakenToSeeColors;


            /******** STEP 3: PRESS AND VERIFY THE BEACON!!!!! ********/

            ConsoleManager.outputNewLineToDrivers ("Beacon spotted!  Initiating pressing steps...");

            //While the beacon is not completely blue (this is the verification step).
            int failedAttempts = 0; //The robot tries different drive lengths for each trial.
            updateColorSensorStates (); //Has to know the initial colors.

            while (onBlueAlliance ? (!(option1Blue && option2Blue)) : (!(option1Red && option2Red)))
            {
                ConsoleManager.outputNewLineToDrivers ("Beacon is not colored correctly, attempting to press the correct color!");

                //The possible events that could occur upon either verification or first looking at the beacon, Different drives are attempted for each trial.
                int driveDistance;
                double drivePower;
                if (option1Blue && option2Red)
                {
                    ConsoleManager.outputNewLineToDrivers ("Chose option 1");
                    driveDistance = (onBlueAlliance ? 90 : 60) + 20 * failedAttempts;
                    drivePower = BEACON_DP;
                }
                else if (option1Red && option2Blue)
                {
                    ConsoleManager.outputNewLineToDrivers ("Chose option 2");
                    driveDistance = (onBlueAlliance ? 150 : 130) + 20 * failedAttempts;
                    drivePower = -BEACON_DP;
                }
                else if (option1Blue ? (option1Red && option2Red) : (option1Blue && option2Blue))
                {
                    ConsoleManager.outputNewLineToDrivers ("Neither option is the correct color, toggling beacon!");
                    driveDistance = (onBlueAlliance ? 90 : 60) + 20 * failedAttempts;
                    drivePower = -BEACON_DP;
                }
                else
                {
                    failedAttempts = -1; //This will be incremented and returned to 0, fear not.
                    ConsoleManager.outputNewLineToDrivers ("Can't see the beacon clearly, so extending to see it.");
                    driveDistance = 100;
                    drivePower = BEACON_DP + .1;
                }

                //Drive to the values set forth previously by the if statement above and attempt to press the button.
                drive(SensorStopType.Distance, driveDistance, PowerUnits.RevolutionsPerSecond, drivePower);

                //Press the button.
                rightButtonPusher.setServoPosition (0);
                ProgramFlow.pauseForMS (timeForEachPress);
                rightButtonPusher.setServoPosition (1);
                ProgramFlow.pauseForMS (timeForEachPress - 200);

                //Drive back to the original position.
                drive(SensorStopType.BottomColorAlpha, 5, PowerUnits.RevolutionsPerSecond, -1 * Math.signum(drivePower) * BEACON_DP);

                //Update the number of trials completed so that we know the new drive distance and such.
                failedAttempts++;

                //Update beacon states to check loop condition.
                updateColorSensorStates ();
            }

            ConsoleManager.outputNewLineToDrivers ("Success!  The beacon is completely blue.");

            //Retract almost completely and then drive to the next one.
            ConsoleManager.outputNewLineToDrivers ("Retracting color sensing apparatus for the next beacon.");
            rightButtonPusher.setServoPosition (1);
            sleep (timeTakenToSeeColors);
            rightButtonPusher.setServoPosition (.5);

            drive (SensorStopType.Distance, 500, PowerUnits.RevolutionsPerSecond, autonomousSign * (BEACON_DP + 1));
        }


        /******** STEP 4: PARK AND KNOCK OFF THE CAP BALL ********/

        //Dash backward to the ramp afterward.
        ConsoleManager.outputNewLineToDrivers ("Knocking the cap ball off of the pedestal...");
        turnToHeading(36 * autonomousSign - (onBlueAlliance ? 0 : 180), TurnMode.BOTH, 2000);
        drive (SensorStopType.Distance, 3000, PowerUnits.RevolutionsPerSecond, -BEACON_DP);
    }
}