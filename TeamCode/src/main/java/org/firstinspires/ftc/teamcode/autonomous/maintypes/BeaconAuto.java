/**
 * This class is the base class for red and blue beacon autonomouses.
 *
 * Since this class implements the OnAlliance interface, it can be guaranteed that we determine which alliance we are running on (as selected on the driver station), which then executes the correct code accordingly.
 *
 * The many ternary operators within the code indicate the different steps that the code would take depending on the alliance in question.
 *
 * Threads enable adjusting drives and such without the need for constantly calling a single method.  Crash prone though.
 */

package org.firstinspires.ftc.teamcode.autonomous.maintypes;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.autonomous.OnAlliance;
import org.firstinspires.ftc.teamcode.console.NiFTConsole;
import org.firstinspires.ftc.teamcode.threads.NiFTAsyncTask;
import org.firstinspires.ftc.teamcode.threads.NiFTFlow;

public abstract class BeaconAuto extends AutoBase implements OnAlliance
{
    //The task which will be used to control harvesting particles while we drive.
    private final class PickUpAndAutoRejectParticles extends NiFTAsyncTask
    {
        public PickUpAndAutoRejectParticles()
        {
            super("Particle Pick Up Task");
        }

        private int pickedUpParticles = 0;

        @Override
        protected void onBeginTask () throws InterruptedException
        {
            //Set initial harvester power.
            harvester.setDirectMotorPower (.8);
            flywheels.setDirectMotorPower (-.4);

            //Start the task which actually looks at the color of the sensors.
            while (true)
            {
                if (getAlliance () == Alliance.BLUE)
                {
                    if (particleColorSensor.sensor.blue () > 3)
                        pickedUpParticles++;
                    else if (particleColorSensor.sensor.red () > 3)
                    {
                        harvester.setDirectMotorPower (-1);
                        NiFTFlow.pauseForMS (1500);
                        harvester.setDirectMotorPower (.8);
                    }
                }
                else
                {
                    if (particleColorSensor.sensor.red () > 3)
                        pickedUpParticles++;
                    else if (particleColorSensor.sensor.blue () > 3)
                    {
                        harvester.setDirectMotorPower (-1);
                        NiFTFlow.pauseForMS (1500);
                        harvester.setDirectMotorPower (.8);
                    }
                }

                processConsole.updateWith (
                        "Currently picked up = " + pickedUpParticles
                );

                //Pause for a frame.
                NiFTFlow.pauseForSingleFrame ();
            }
        }

        @Override
        protected void onQuitTask ()
        {
            harvester.setDirectMotorPower (0);
            flywheels.setDirectMotorPower (0);
        }

        public int getPickedUpParticles()
        {
            return pickedUpParticles;
        }
    }

    //Called after runOpMode() has finished initializing by ImprovedOpModeBase.
    protected void driverStationSaysGO () throws InterruptedException
    {
        //The ideal distance @ which to drive.
        final double BEACON_RPS = 1.5;

        //Results in a coefficient of 1 if doing blue, and -1 for red.
        boolean onBlueAlliance = (getAlliance () == Alliance.BLUE);
        int autonomousSign = (onBlueAlliance ? 1 : -1);

        /******** STEP 1: SHOOT, DRIVE, TURN TO BE PARALLEL WITH WALL ********/

        //Start the flywheels.
        NiFTConsole.outputNewSequentialLine ("Started flywheels...");
        flywheels.startPIDTask ();
        flywheels.setRPS (18.2);

        //Drive until we are just far enough from the cap ball to score reliably.
        NiFTConsole.outputNewSequentialLine("Driving forward to the cap ball to score...");
        drive(SensorStopType.Ultrasonic, 39, PowerUnits.RevolutionsPerSecond, 1);

        //Shoot the balls into the center vortex.
        NiFTConsole.outputNewSequentialLine("Shooting balls into center vortex...");
        harvester.setDirectMotorPower (1);
        NiFTFlow.pauseForMS (2200);
        harvester.setDirectMotorPower (0);
        flywheels.stopPIDTask ();
        flywheels.setRPS (0);

        //Turn to face the wall directly.
        NiFTConsole.outputNewSequentialLine("Turning to face wall at an angle...");
        turnToHeading(73 * autonomousSign, TurnMode.BOTH, 3000);

        //Start the harvesting task.
        NiFTConsole.outputNewSequentialLine ("Starting harvesting task...");
        PickUpAndAutoRejectParticles pickUpTask = new PickUpAndAutoRejectParticles ();
        pickUpTask.run();

        //Drive to the wall and stopEasyTask once a little ways away.
        NiFTConsole.outputNewSequentialLine ("Driving to the wall...");
        drive (SensorStopType.Ultrasonic, (onBlueAlliance ? 31 : 36), PowerUnits.RevolutionsPerSecond, 1);

        //Turn back to become parallel with the wall.
        NiFTConsole.outputNewSequentialLine("Turning to become parallel to the wall...");
        turnToHeading(onBlueAlliance ? 0 : -180, TurnMode.BOTH, 3000);

        //Extend pusher so that we are very close to the colors themselves.
        NiFTConsole.outputNewSequentialLine ("Extending to become parallel to the wall.");
        double distFromWall = sideRangeSensor.validDistCM (20, 2000); //Make sure valid.
        int gapBetweenWallAndSensorApparatus = 12;
        long extensionTime = (long) ((distFromWall - gapBetweenWallAndSensorApparatus) * 67);
        rightButtonPusher.setToUpperLim ();
        NiFTFlow.pauseForMS (extensionTime);
        rightButtonPusher.setServoPosition (0.5); //Stop vex motor.

        //For each of the two beacons.
        for (int currentBeacon = 1; currentBeacon <= 2; currentBeacon++)
        {
            /******** STEP 2: FIND AND CENTER SELF ON BEACON ********/

            //Drive up to the line.
            NiFTConsole.outputNewSequentialLine ("Looking for beacon " + currentBeacon);
            SelfAdjustingDriveTask drivingTask = new SelfAdjustingDriveTask (BEACON_RPS, true);
            drivingTask.run();
            while (bottomColorSensor.sensor.alpha () <= 4)
            {
                NiFTFlow.pauseForSingleFrame ();
            }
            drivingTask.stop();

            /******** STEP 3: PRESS AND VERIFY THE BEACON!!!!! ********/

            NiFTConsole.outputNewSequentialLine ("Beacon spotted!  Initiating pressing steps...");

            //While the beacon is not completely blue (this is the verification step).
            int failedAttempts = 0; //The robot tries different drive lengths for each trial.
            updateColorSensorStates (); //Has to know the initial colors.

            while (onBlueAlliance ? (!(option1Blue && option2Blue)) : (!(option1Red && option2Red)))
            {
                NiFTConsole.outputNewSequentialLine ("Beacon is not colored correctly, attempting to press the correct color!");

                //The possible events that could occur upon either verification or first looking at the beacon, Different drives are attempted for each trial.
                int driveDistance;
                double drivePower;
                if (option1Blue && option2Red)
                {
                    NiFTConsole.outputNewSequentialLine ("Chose option 1");
                    driveDistance = (onBlueAlliance ? 90 : 60) + 20 * failedAttempts;
                    drivePower = BEACON_RPS;
                }
                else if (option1Red && option2Blue)
                {
                    NiFTConsole.outputNewSequentialLine ("Chose option 2");
                    driveDistance = (onBlueAlliance ? 150 : 130) + 20 * failedAttempts;
                    drivePower = -BEACON_RPS;
                }
                else if (onBlueAlliance ? (option1Red && option2Red) : (option1Blue && option2Blue))
                {
                    NiFTConsole.outputNewSequentialLine ("Neither option is the correct color, toggling beacon!");
                    driveDistance = (onBlueAlliance ? 90 : 60) + 20 * failedAttempts;
                    drivePower = -BEACON_RPS;
                }
                else
                {
                    failedAttempts = -1; //This will be incremented and returned to 0, fear not.
                    NiFTConsole.outputNewSequentialLine ("Can't see the beacon clearly, so extending to see it.");
                    driveDistance = 100;
                    drivePower = BEACON_RPS + .1;
                }

                //Drive to the values set forth previously by the if statement above and attempt to press the button.
                drive(SensorStopType.Distance, driveDistance, PowerUnits.RevolutionsPerSecond, drivePower);

                if (failedAttempts != -1)
                {
                    //Press the button.
                    rightButtonPusher.setToUpperLim ();
                    NiFTFlow.pauseForMS (gapBetweenWallAndSensorApparatus * 67);
                    rightButtonPusher.setServoPosition (1);
                    NiFTFlow.pauseForMS (gapBetweenWallAndSensorApparatus * 67 - 300);
                }

                //Drive back to the original position.
                drivingTask = new SelfAdjustingDriveTask (BEACON_RPS, true);
                drivingTask.run();
                while (bottomColorSensor.sensor.alpha () <= 4 || !((option1Red || option1Blue) && (option2Red || option2Blue)))
                {
                    updateColorSensorStates ();
                    NiFTFlow.pauseForSingleFrame ();
                }
                drivingTask.stop();

                //Update the number of trials completed so that we know the new drive distance and such.
                failedAttempts++;

                //Update beacon states to check loop condition.
                updateColorSensorStates ();
            }

            NiFTConsole.outputNewSequentialLine ("Success!  The beacon is completely blue.");

            //Drive a little ways beyond the white line.
            drive (SensorStopType.Distance, 500, PowerUnits.RevolutionsPerSecond, autonomousSign * (BEACON_RPS + 1));
        }


        /******** STEP 4: PARK AND KNOCK OFF THE CAP BALL ********/

        //Dash backward to the ramp afterward.
        NiFTConsole.outputNewSequentialLine ("Knocking the cap ball off of the pedestal...");
        turnToHeading(onBlueAlliance ? 36 : -216, TurnMode.BOTH, 2000);
        drive (SensorStopType.Distance, 3000, PowerUnits.RevolutionsPerSecond, -BEACON_RPS);
    }
}