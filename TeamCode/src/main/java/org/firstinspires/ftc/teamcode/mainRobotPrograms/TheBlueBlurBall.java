package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="The Blue Blur - Ball", group = "Auto Group")

public class TheBlueBlurBall extends _AutonomousBase
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
        outputNewLineToDrivers("Driving forward to hit cap ball...");
        setMovementPower(1.0);
        while(frontRangeSensor.cmUltrasonic() > 20)
            adjustMotorPowersBasedOnGyroSensor();
        stopDriving();


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