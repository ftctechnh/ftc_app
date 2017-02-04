package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="Autonomous - Blue Beacons", group = "Autonomous Group")

public class AutonomousBlueBeacons extends _AutonomousBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Drive a little ways from the wall.
        driveForTime(.4, 400);

        //Know how much this missed by since it can be made up later in a different turn.
        turnToHeading(50, TurnMode.LEFT, 2750);

        //Dash over across the diagonal of the corner goal.
        driveForTime(.4, 1500);

        //Turn back to become parallel with the wall.
        turnToHeading(0, TurnMode.RIGHT, 3000); //Take a bit of extra time on this turn.

        for (int i = 0; i < 2; i++) //Two beacons.
        {
            setMovementPower(.25);
            while (bottomColorSensor.alpha() < 3)
                adjustMotorPowersBasedOnGyroSensor();

            //We have reached the line and are parallel to the wall.
            outputNewLineToDrivers("Beacon reached, at " + frontRangeSensor.getDistance(DistanceUnit.CM) + " cm from the front and " + backRangeSensor.getDistance(DistanceUnit.CM) + " cm from the back.");

            //Drive backward beyond the first option.
            driveForTime(-0.2, 1500);

            //Check beacon.
            setMovementPower(.2);
            long startTime = System.currentTimeMillis(), permittedTime = 3000;
            while (rightColorSensor.blue() < 2 && System.currentTimeMillis() - startTime < permittedTime) //Looking at the blue one, so move forward a set distance.
            {
                adjustMotorPowersBasedOnGyroSensor();
                outputConstantDataToDrivers(new String[]
                        {
                                "Blue: " + rightColorSensor.blue(),
                                "Will stop in " + (permittedTime - (System.currentTimeMillis() - startTime)) + "ms by default"
                        });
            }
            stopDriving();

            if (rightColorSensor.blue() >= 2)
            {
                //Drive a little ways forward to place the pusher thingamabob next to the button.
                driveForTime(0.2, 600);

                //Determine the length to push the pusher out based on the distance from the wall (ONLY THING THE DARN RANGE SENSORS ARE USED FOR)
                double frontDist = frontRangeSensor.cmUltrasonic();
                double extendLength = 1000 * (frontDist == 255 ? backRangeSensor.cmUltrasonic() : frontDist) * 1.0/10;

                //Run the continuous rotation servo out to press, then back in.
                rightButtonPusher.setPosition(.2);
                sleep((long) (extendLength));
                rightButtonPusher.setPosition(.8);
                sleep((long) (extendLength));
                rightButtonPusher.setPosition(.5);
            }

            //If this is the first loop, drive a little ways forward before looking for the next line.
            if (i == 0)
                driveForTime(0.25, 1500);
        }
    }
}