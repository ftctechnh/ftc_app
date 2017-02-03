package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="Autonomous - Blue Beacons", group = "Autonomous Group")

public class AutonomousBlueBeacons extends _AutonomousBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        driveForTime(.4, 400); //Drive a little ways from the wall.

        //Know how much this missed by since it can be made up later in a different turn.
        turnToHeading(50, TurnMode.LEFT, 2750);

        driveForTime(.4, 1500); //Dash over across the corner goal.

        //Turn back to become parallel with the wall.
        turnToHeading(0, TurnMode.RIGHT, 3000); //Take a bit of extra time on this turn.

        for (int i = 0; i < 2; i++) //Two beacons.
        {
            setMovementPower(.25);
            while (bottomColorSensor.alpha() < 3)
                adjustMotorPowersBasedOnGyroSensor();

            //We have reached the line and are parallel to the wall.
            outputNewLineToDrivers("Beacon reached, at " + frontRangeSensor.getDistance(DistanceUnit.CM) + " cm from the front and " + backRangeSensor.getDistance(DistanceUnit.CM) + " cm from the back.");

            //Drive forward.
            double startTime = System.currentTimeMillis();
            setMovementPower(-0.2);
            while (System.currentTimeMillis() - startTime < 1500)
                adjustMotorPowersBasedOnGyroSensor();

            //Check beacon.
            setMovementPower(.2);
            startTime = System.currentTimeMillis();
            while (rightColorSensor.blue() < 2 && System.currentTimeMillis() - startTime < 3000) //Looking at the blue one, so move forward a set distance.
            {
                adjustMotorPowersBasedOnGyroSensor();
                outputConstantDataToDrivers(new String[]
                        {
                                "Blue: " + rightColorSensor.blue()
                        });
            }
            stopDriving();

            if (rightColorSensor.blue() >= 2)
            {
                startTime = System.currentTimeMillis();
                while(System.currentTimeMillis() - startTime < 600)
                    adjustMotorPowersBasedOnGyroSensor();
                stopDriving();

                double frontDist = frontRangeSensor.cmUltrasonic();
                double extendLength = 1000 * (frontDist == 255 ? backRangeSensor.cmUltrasonic() : frontDist) * .1;

                //Run the continuous rotation servo out to press, then back in.
                rightButtonPusher.setPosition(.2);
                sleep((long) (extendLength));
                rightButtonPusher.setPosition(.8);
                sleep((long) (extendLength));
                rightButtonPusher.setPosition(.5);
            }

            if (i == 0)
            {
                setMovementPower(.25);
                startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < 1500) //Three seconds parallel to wall.
                    adjustMotorPowersBasedOnGyroSensor();
            }
        }
    }
}