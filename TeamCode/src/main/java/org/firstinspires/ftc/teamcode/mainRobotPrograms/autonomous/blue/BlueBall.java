package org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.BallAutonomousBase;

@Autonomous(name="Blue Ball", group = "Auto Group")

public class BlueBall extends BallAutonomousBase
{
    //Called after runOpMode() has finished initializing by BaseFunctions.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Drive to the cap ball.
        outputNewLineToDrivers ("Driving to shooting position.");
        driveUntilDistanceFromObstacle (40);

        //Shoot the balls into the center vortex.
        outputNewLineToDrivers("Shooting balls into center vortex...");
        shootBallsIntoCenterVortex ();

        if (parkOnCenterVortex)
        {
            outputNewLineToDrivers ("Parking on center vortex.");
            driveForDistance (0.5, 1400);
            return; //End prematurely
        }

        if (getCapBall)
        {
            //Drive the remainder of the distance.
            outputNewLineToDrivers ("Knock the cap ball off of the pedestal.");
            driveForDistance (0.5, 1800);

            //Turn to face the ramp from the position that we drove.
            outputNewLineToDrivers ("Turning to the appropriate heading.");
            turnToHeading (110, TurnMode.BOTH, 3000);
        }
        else
        {
            //Turn to face the ramp from the position that we drove.
            outputNewLineToDrivers ("Turning to the appropriate heading.");
            turnToHeading (70, TurnMode.BOTH, 3000);
        }

        //Drive until we reach the appropriate position.
        outputNewLineToDrivers ("Driving to the ramp.");
        startDrivingAt (0.6);

        long startDriveTime = System.currentTimeMillis (); //Max time at 6 seconds.
        while (bottomColorSensor.red () <= 2.5 && (System.currentTimeMillis () - startDriveTime) < 6000)
            applySensorAdjustmentsToMotors (true, false, false);

        stopDriving ();
    }
}