package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;

import java.util.ArrayList;

@Autonomous(name="The Blue Blur", group = "Beta Group")

public class TheBlueBlur extends _AutonomousBase
{
    //This value will be set a couple times to verify that we don't go too far.
    long startTime = 0;

    //Called after runOpMode() has finished initializing by BaseFunctions.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Drive until we are just far enough from the cap ball to score reliably.
        outputNewLineToDrivers("Driving forward to the cap ball to score...");
        setMovementPower(.3);
        while(frontRangeSensor.cmUltrasonic() > 40)
            adjustMotorPowersBasedOnGyroSensor();
        stopDriving();

        //Shoot the balls into the center vortex.
        outputNewLineToDrivers("Shooting balls into center vortex...");
        flywheels.setPower(0.4);
        sleep(300);
        harvester.setPower(1.0);
        sleep(3000);
        flywheels.setPower(0);
        harvester.setPower(0);

        //Drive a bit further forward toward the cap ball so that when we turn we don't end up crashing into the corner vortex.
        outputNewLineToDrivers("Driving forward toward the cap ball before turn...");
        setMovementPower(.4);
        while(frontRangeSensor.cmUltrasonic() > 20)
            adjustMotorPowersBasedOnGyroSensor();
        stopDriving();

        //Turn to face the wall directly.
        outputNewLineToDrivers("Turning to face wall...");
        turnToHeading(90, TurnMode.BOTH, 4000);

        //Drive to the wall and stop once a little ways away.
        outputNewLineToDrivers("Driving to wall before turn...");
        while (frontRangeSensor.cmUltrasonic() > 45)
            adjustMotorPowersBasedOnGyroSensor();
        stopDriving();

        //Turn back to become parallel with the wall.
        outputNewLineToDrivers("Turning to become parallel to the wall...");
        turnToHeading(0, TurnMode.RIGHT, 4000);

        //For each of the two beacons.
        for (int i = 0; i < 2; i++)
        {
            outputNewLineToDrivers ("Doing beacon " + (i + 1));
            outputNewLineToDrivers ("Looking for the blue beacon...");

            setMovementPower(0.3);
            while (rightColorSensor.blue() < 2)
                adjustMotorPowersBasedOnGyroSensor();
            stopDriving();
            outputNewLineToDrivers ("Ahoy there!  Blue beacon spotted!");

            //Now that we've seen the blue beacon, we stop and try to press it immediately.
            driveForTime(0.2, 600);

            pressButton ();
        }


        //Now, dash backward to the corner goal before time runs out, while verifying that all beacons are blue.
        outputNewLineToDrivers ("Verifying that both beacons were pressed, and en route to corner goal.");

        //Until we've passed both white lines.
        for (int i = 0; i < 2; i++)
        {
            setMovementPower (-0.5);
            //Verify the beacon.
            //Drive to the first option.
            boolean firstWasBlue = false, secondWasBlue = false;

            while (rightColorSensor.blue () < 2 || rightColorSensor.red () < 2)
                adjustMotorPowersBasedOnGyroSensor ();

            if (rightColorSensor.red() >= 2) //It wasn't blue, so check to see if the other one is blue.
            {
                outputNewLineToDrivers ("Determined that this first option is NOT BLUE!");
                firstWasBlue = false;
            }
            else
            {
                outputNewLineToDrivers ("Determined that the first option is blue.");
                firstWasBlue = true;
            }

            //Get to the middle line before driving a little ways to check the next option.
            while (bottomColorSensor.alpha () < 2)
                adjustMotorPowersBasedOnGyroSensor ();

            //Drive to the second option.
            driveForTime (-0.4, 600);

            if (rightColorSensor.red () >= 2)
            {
                outputNewLineToDrivers ("The second option is not blue!");
                secondWasBlue = false;
            }
            else
            {
                outputNewLineToDrivers ("The second option is blue!");
                secondWasBlue = true;
            }

            //Possible scenarios that we observe.
            if (firstWasBlue && !secondWasBlue)
            {
                outputNewLineToDrivers ("Since the first option was blue, but the second wasn't, pressing button.");
                pressButton ();
            }
            else if (!firstWasBlue && secondWasBlue)
            {
                //Drive back to the first option, and then press that button.
                outputNewLineToDrivers ("Since the first option wasn't blue, but the second was, going back to press first button.");

                setMovementPower (0.3);
                while (rightColorSensor.red() < 2)
                    adjustMotorPowersBasedOnGyroSensor ();
                stopDriving ();

                pressButton ();

                driveForTime (-0.6, 1000);
            }
            else if (!firstWasBlue && !secondWasBlue)
            {
                //By some crazy situation, we durn f***ed up and pressed the wrong alliance button.  Toggle the current color, then.
                outputNewLineToDrivers ("Crap.  We must have pressed the wrong button, toggling beacon to blue by pressing any button.");

                pressButton ();
            }

            //Drive to get set for the next option.
            if (i == 0)
                driveForTime (-0.6, 2000);
        }
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