package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="Autonomous - Red Beacons Edition", group = "Autonomous Group")

public class AutonomousRedBeacons extends _AutonomousBase
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
        turnToHeading(-90, TurnMode.BOTH, 4000);

        //Drive to the wall and stop once a little ways away.
        outputNewLineToDrivers("Driving to wall before turn...");
        while (frontRangeSensor.cmUltrasonic() > 40)
            adjustMotorPowersBasedOnGyroSensor();
        stopDriving();

        //Turn back to become parallel with the wall.
        outputNewLineToDrivers("Turning to become parallel to the wall...");
        turnToHeading(-180, TurnMode.LEFT, 4000);

        //For both beacons.
        for (int i = 0; i < 2; i++)
        {
            //Output beacon number.
            outputNewLineToDrivers("");
            outputNewLineToDrivers("Doing beacon " + (i + 1));

            //Drive to the white line in front of the beacon.
            outputNewLineToDrivers("Driving to the white line...");
            setMovementPower(-.25);
            while (bottomColorSensor.alpha() < 3)
                adjustMotorPowersBasedOnGyroSensor();

            //Drive backward beyond the first option.
            outputNewLineToDrivers("Backing up behind the beacon...");
            driveForTime(0.25, 1500);

            //Drive forward until the right color sensor sees the appropriate color.  If we don't see it in a few seconds, we'll stop looking for it.
            outputNewLineToDrivers("Looking for the blue beacon option...");
            setMovementPower(-.22);
            startTime = System.currentTimeMillis();
            long permittedTime = 3000;
            while (rightColorSensor.red() < 2 && System.currentTimeMillis() - startTime < permittedTime)
            {
                adjustMotorPowersBasedOnGyroSensor();
                outputConstantDataToDrivers(new String[]
                        {
                                "Red: " + rightColorSensor.red(),
                                "Will stop search in " + (permittedTime - (System.currentTimeMillis() - startTime)) + "ms by default"
                        });
            }
            stopDriving();

            //If we successfully reached the right color...
            if (rightColorSensor.red() >= 2)
            {
                outputNewLineToDrivers("Red beacon found, extending pusher thingamabob...");

                //Drive a little ways forward to place the pusher thingamabob next to the button.
                driveForTime(0.2, 600);

                //Determine the length to push the pusher out based on the distance from the wall
                double extendLength = 100 * sideRangeSensor.cmUltrasonic();
                extendLength = Range.clip(extendLength, 0, 3000);

                //Run the continuous rotation servo out to press, then back in.
                rightButtonPusher.setPosition(.2);
                sleep((long) (extendLength));
                rightButtonPusher.setPosition(.8);
                sleep((long) (extendLength));
                rightButtonPusher.setPosition(.5);
            }
            else
            {
                outputNewLineToDrivers("Did not find red beacon.");
            }

            //If this is the first beacon, drive a little ways forward before looking for the next line so that we don't mistake this line for the next one.
            if (i == 0)
            {
                outputNewLineToDrivers("Since this is beacon 1, drove a bit forward beyond the first white line.");
                driveForTime(-0.25, 2000);
            }
        }
    }
}