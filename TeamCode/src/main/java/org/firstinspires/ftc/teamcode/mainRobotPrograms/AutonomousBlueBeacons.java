package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="Autonomous - Blue Beacons", group = "Autonomous Group")

public class AutonomousBlueBeacons extends _AutonomousBase
{
    long startTime = 0, permittedTime = 1000;

    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Drive a little ways from the wall.
        setMovementPower(.3);
        while(frontRangeSensor.cmUltrasonic() > 40)
            adjustMotorPowersBasedOnGyroSensor();
        stopDriving();

        //Shoot the balls into the center vortex
        flywheels.setPower(0.4);
        sleep(300);
        harvester.setPower(1.0);
        sleep(3000);
        flywheels.setPower(0);
        harvester.setPower(0);

        //Drive a bit further forward toward the cap ball
        setMovementPower(.4);
        while(frontRangeSensor.cmUltrasonic() > 20)
            adjustMotorPowersBasedOnGyroSensor();
        stopDriving();

        //Know how much this missed by since it can be made up later in a different turn.
        turnToHeading(90, TurnMode.BOTH, 4000);

        //Dash over across the diagonal of the corner goal.
        while (frontRangeSensor.cmUltrasonic() > 45)
            adjustMotorPowersBasedOnGyroSensor();
        stopDriving();

        //Turn back to become parallel with the wall.
        turnToHeading(0, TurnMode.RIGHT, 4000); //Take a bit of extra time on this turn.

        for (int i = 0; i < 2; i++) //Two beacons.
        {
            setMovementPower(.25);
            while (bottomColorSensor.alpha() < 3)
                adjustMotorPowersBasedOnGyroSensor();

            //Drive backward beyond the first option.
            driveForTime(-0.25, 1500);

            //Check beacon.
            setMovementPower(.22);
            startTime = System.currentTimeMillis();
            permittedTime = 3000;
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
                double extendLength = 1000 * backRangeSensor.cmUltrasonic() * 1.0/10;

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