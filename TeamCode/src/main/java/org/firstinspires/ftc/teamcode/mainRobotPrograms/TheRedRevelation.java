package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="The Red Revelation", group = "Beta Group")

public class TheRedRevelation extends _AutonomousBase
{

    //Called after runOpMode() has finished initializing by BaseFunctions.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Drive until we are just far enough from the cap ball to score reliably.
        outputNewLineToDrivers("Driving forward to the cap ball to score...");
        setMovementPower(.7);
        while(frontRangeSensor.cmUltrasonic() > 40)
            adjustMotorPowersBasedOnGyroSensor();
        stopDriving();

        //Shoot the balls into the center vortex.
        outputNewLineToDrivers("Shooting balls into center vortex...");
        flywheels.setPower(0.3);
        sleep(300);
        harvester.setPower(1.0);
        sleep(2500);
        flywheels.setPower(0);
        harvester.setPower(0);

        //Turn to face the wall directly.
        outputNewLineToDrivers("Turning to face wall at an angle...");
        turnToHeading(-82, TurnMode.BOTH, 3000);

        //Drive to the wall and stop once a little ways away.
        setMovementPower (0.7);
        outputNewLineToDrivers("Driving to wall before turn...");
        while (frontRangeSensor.cmUltrasonic () > 70)
            adjustMotorPowersBasedOnGyroSensor ();
        setMovementPower(0.4);
        while (frontRangeSensor.cmUltrasonic () > 44)
            adjustMotorPowersBasedOnGyroSensor ();
        stopDriving ();

        //Turn back to become parallel with the wall.
        outputNewLineToDrivers("Turning to become parallel to the wall...");
        turnToHeading(-180, TurnMode.BOTH, 4000);

        //For each of the two beacons.
        for (int i = 0; i < 2; i++)
        {
            outputNewLineToDrivers ("Looking for beacon " + (i + 1));

            //Set movement speed.
            setMovementPower(-0.26);

            //Variables required for range sensor adjustment.
            long lastAdjustTime = System.currentTimeMillis ();
            int idealDistance = 15;

            //Drive until centered on the beacon.
            while (bottomColorSensor.alpha() <= 5)
            {
                //Adjust gyro based on range sensor.
                double currentDist = sideRangeSensor.cmUltrasonic ();
                if (Math.abs (currentDist - idealDistance) >= 2)
                {
                    if (System.currentTimeMillis () - lastAdjustTime > 1000)
                    {
                        gyroAdjustFactor -= -Math.signum (currentDist - idealDistance);
                        lastAdjustTime = System.currentTimeMillis ();
                        outputNewLineToDrivers ("Adjusted gyro factor, too far or close to wall, now is " + gyroAdjustFactor);
                    }
                }

                //Adjust motors based on gyro to remain parallel to wall.
                adjustMotorPowersBasedOnGyroSensor ();
            }

            //Stop driving when centered.
            stopDriving ();

            //Changed as the loop below progresses.
            boolean option1Red = option1ColorSensor.red () >= 2,
                    option1Blue = option1ColorSensor.blue () >= 2,
                    option2Red = option2ColorSensor.red () >= 2,
                    option2Blue = option2ColorSensor.blue () >= 2;

            outputNewLineToDrivers ("Ahoy there!  Beacon spotted!  Option 1 is " + (option1Blue ? "blue" : "red") + " and option 2 is " + (option2Blue ? "blue" : "red"));

            //While the beacon is not completely blue (this is the verification step).
            int failedAttempts = 0; //The robot tries different drives for each trial.
            while (! (option1Red && option2Red))
            {
                outputNewLineToDrivers ("Beacon is not completely blue, attempting to press the correct color!");

                boolean driveBackwardsToRecenter = true, runProvidedWeirdValues = false;

                //The possible events that could occur upon either verification or first looking at the beacon.
                //Different drives are attempted for each trial.
                if (option1Red && option2Blue)
                {
                    outputNewLineToDrivers ("Chose option 1");
                    //Use the option 1 button pusher.
                    driveForDistance (0.25, 80 + 10 * failedAttempts);
                    pressButton();
                    driveBackwardsToRecenter = true;
                }
                else if (option1Blue && option2Red)
                {
                    outputNewLineToDrivers ("Chose option 2");
                    //Use the option 2 button pusher.
                    driveForDistance (-0.25, 130 + 10 * failedAttempts);
                    pressButton();
                    driveBackwardsToRecenter = false;
                }
                else if (option1Blue && option2Blue)
                {
                    failedAttempts = 0;
                    outputNewLineToDrivers ("Neither option is blue, toggling beacon!");
                    //Toggle beacon.
                    driveForDistance (0.25, 80 + 10 * failedAttempts);
                    pressButton();
                    driveBackwardsToRecenter = true;
                }
                else
                {
                    outputNewLineToDrivers("Run provided weird values!");
                    runProvidedWeirdValues = true;
                }

                //On occasion this does happen for some reason, in which all are false or something.  Sometimes they shift back to being valid, however.
                if (!runProvidedWeirdValues) {
                    //Set the movement power based on the direction we have to return to.
                    setMovementPower((driveBackwardsToRecenter ? -1 : 1) * 0.25);
                    while (bottomColorSensor.alpha() <= 5)
                        adjustMotorPowersBasedOnGyroSensor();
                    stopDriving();

                    //Update the number of trials completed so that we know the new drive distance and such.
                    failedAttempts++;
                }

                idle();

                //Update beacon states to check loop condition.
                option1Red = option1ColorSensor.red () >= 2;
                option1Blue = option1ColorSensor.blue () >= 2;
                option2Red = option2ColorSensor.red () >= 2;
                option2Blue = option2ColorSensor.blue () >= 2;
            }

            outputNewLineToDrivers ("Success!  Beacon is completely blue.");

            //Drive a bit forward from the white line to set up for the next step.
            if (i == 0)
                driveForDistance (-0.4, 500);
        }

        //Dash backward to the ramp afterward.
        turnToHeading(-228, TurnMode.BOTH, 2200);
        driveForDistance(1.0, 3200); //CARAZZY FAST

    }

    private void pressButton() throws InterruptedException
    {
        //Determine the length to push the pusher out based on the distance from the wall.
        double extendLength = 100 * sideRangeSensor.cmUltrasonic();
        extendLength = Range.clip(extendLength, 0, 3000);
        outputNewLineToDrivers ("Extending the button pusher for " + extendLength + " ms.");

        //Run the continuous rotation servo out to press, then back in.
        rightButtonPusher.setPosition(.2);
        sleep((long) (extendLength));
        rightButtonPusher.setPosition(.8);
        sleep((long) (extendLength));
        rightButtonPusher.setPosition(.5);
    }
}