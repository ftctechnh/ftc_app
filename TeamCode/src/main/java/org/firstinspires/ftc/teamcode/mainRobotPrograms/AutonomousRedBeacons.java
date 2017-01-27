package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="Autonomous - Red Beacons", group = "Autonomous Group")

public class AutonomousRedBeacons extends _AutonomousBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        driveForTime(.6, 700); //Drive a little ways from the wall.

        setPrecisionFactor(10); //We can afford to be a bit inaccurate for this turn.
        turnToHeading(-45, TurnMode.LEFT);

        driveForTime(.8, 1500); //Dash over across the corner goal.

        //Turn until the range sensors detect that we are parallel with the wall.
        turnToHeading(45, TurnMode.RIGHT);

        for (int i = 0; i < 2; i++) //Two beacons.
        {
            setMovementPower(.3);
            while (bottomColorSensor.alpha() < 3)
                updateMotorPowersBasedOnRangeSensors();

            //We have reached the line and are parallel to the wall.
            outputNewLineToDrivers("Beacon reached, at " + frontRangeSensor.getDistance(DistanceUnit.CM) + " cm from the front and " + backRangeSensor.getDistance(DistanceUnit.CM) + " cm from the back.");

            double startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 300)
                updateMotorPowersBasedOnRangeSensors();

            //Check first color.
            if (rightColorSensor.blue() < 2 && rightColorSensor.red() > 2) //Looking at the blue one, so move forward a set distance.
            {
                startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 700)
                    updateMotorPowersBasedOnRangeSensors();
            }

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
                    updateMotorPowersBasedOnRangeSensors();
            }
        }
    }
}