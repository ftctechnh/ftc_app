package org.firstinspires.ftc.teamcode.debugging.programs;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;

@Autonomous(name="Drive Backward", group = "Utility Group")

public class DriveBackward extends AutoBase
{
    //Custom initialization
    @Override
    protected void driverStationSaysINITIALIZE()
    {
        //Set the motor powers.
        leftDrive.setRPS (.6);
        rightDrive.setRPS (-.6);
    }

    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Set the motor powers.
        leftDrive.setRPS (-.8);
        rightDrive.setRPS (-.8);

        while (true)
            idle();
    }
}