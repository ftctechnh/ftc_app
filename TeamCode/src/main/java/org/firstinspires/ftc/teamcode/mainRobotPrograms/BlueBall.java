package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Blue Ball", group = "Auto Group")

public class BlueBall extends _AutonomousBase
{
    //Called after runOpMode() has finished initializing by BaseFunctions.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Drive to the cap ball.
        outputNewLineToDrivers ("Driving to shooting position.");
        driveForDistance (0.5, 2000);

        //Shoot the balls into the center vortex.
        outputNewLineToDrivers("Shooting balls into center vortex...");
        flywheels.setPower(0.3);
        sleep(300);
        harvester.setPower(-1.0);
        sleep(2500);
        flywheels.setPower(0);
        harvester.setPower(0);

        //Drive the remainder of the distance.
        outputNewLineToDrivers ("Knock the cap ball off of the pedestal.");
        driveForDistance(0.7, 1800);

        //Turn to face the ramp from the position that we drove.
        outputNewLineToDrivers ("Turning to the appropriate heading.");
        turnToHeading (105, TurnMode.BOTH, 3000);

        //Drive until we reach the appropriate position.
        outputNewLineToDrivers ("Drive to the ramp, stopping upon bttom color sensor reaches the blue region on the ramp.");
        while (bottomColorSensor.blue () <= 3)
            adjustMotorPowersBasedOnGyroSensor ();
        stopDriving ();
    }
}