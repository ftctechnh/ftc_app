package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Red Ball", group = "Auto Group")

public class RedBall extends _AutonomousBase
{
    //Called after runOpMode() has finished initializing by BaseFunctions.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Drive to the cap ball.
        outputNewLineToDrivers ("Driving to shooting position.");
        driveForDistance (0.3, 2000);

        //Shoot the balls into the center vortex.
        outputNewLineToDrivers("Shooting balls into center vortex...");
        flywheels.setPower(0.35);
        sleep(300);
        harvester.setPower(-1.0);
        sleep(2500);
        flywheels.setPower(0);
        harvester.setPower(0);

        //Drive the remainder of the distance.
        outputNewLineToDrivers ("Knock the cap ball off of the pedestal.");
        driveForDistance(0.4, 1800);

        //Turn to face the ramp from the position that we drove.
        outputNewLineToDrivers ("Turning to the appropriate heading.");
        turnToHeading (110, TurnMode.BOTH, 3000);

        //Drive until we reach the appropriate position.
        outputNewLineToDrivers ("Drive to the ramp, stopping upon bttom color sensor reaches the blue region on the ramp.");
        startToDriveAt (0.25);
        long startDriveTime = System.currentTimeMillis (); //Max time at 6 seconds.
        while (bottomColorSensor.red () <= 2.5 && (System.currentTimeMillis () - startDriveTime) < 6000)
            adjustMotorPowersBasedOnGyroSensor ();
        stopDriving ();
    }
}