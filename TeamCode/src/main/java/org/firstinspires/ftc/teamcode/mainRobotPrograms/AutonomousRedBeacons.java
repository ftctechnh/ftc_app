package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="Autonomous - Red Beacons", group = "Autonomous Group")

public class AutonomousRedBeacons extends _AutonomousBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        driveForTime(.3, 700); //Drive a little ways from the wall.

        //Know how much this missed by since it can be made up later in a different turn.
        turnToHeading(50, TurnMode.LEFT, 4000);

        driveForTime(.5, 1200); //Dash over across the corner goal.

        //Turn back to become parallel with the wall.
        turnToHeading(0, TurnMode.RIGHT, 4500); //Take a bit of extra time on this turn.

        for (int i = 0; i < 2; i++) //Two beacons.
        {
            setMovementPower(.3);
            while (bottomColorSensor.alpha() < 3)
                adjustMotorPowersBasedOnRangeSensors();

            //We have reached the line and are parallel to the wall.
            outputNewLineToDrivers("Beacon reached, at " + frontRangeSensor.getDistance(DistanceUnit.CM) + " cm from the front and " + backRangeSensor.getDistance(DistanceUnit.CM) + " cm from the back.");

            double startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 300)
                adjustMotorPowersBasedOnRangeSensors();

            //Check beacon.
            if (leftColorSensor.blue() < 2 && leftColorSensor.red() > 2) //Looking at the blue one, so move forward a set distance.
            {
                startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 700)
                    adjustMotorPowersBasedOnRangeSensors();
            }
            stopDriving();

            //Run the continuous rotation servo out to press, then back in.
            leftButtonPusher.setPosition(.8);
            sleep(1000);
            leftButtonPusher.setPosition(.2);
            sleep(600);
            leftButtonPusher.setPosition(.5);

            if (i == 0)
            {
                setMovementPower(.7);
                startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 3000) //Three seconds parallel to wall.
                    adjustMotorPowersBasedOnRangeSensors();
            }
        }
    }
}