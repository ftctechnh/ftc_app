package org.firstinspires.ftc.teamcode.debugging.programs;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;

@Autonomous(name="Drive Forward", group = "Utility Group")

public class DriveForward extends AutoBase
{
    @Override
    protected void driverStationSaysINITIALIZE()
    {
        //Set the motor powers.
        leftDrive.setRPS (.3);
        rightDrive.setRPS (-.3);
    }

    protected void driverStationSaysGO() throws InterruptedException
    {
        //Set the motor powers.
        leftDrive.setRPS (.6);
        rightDrive.setRPS (.6);

        while (true)
            idle();
    }
}