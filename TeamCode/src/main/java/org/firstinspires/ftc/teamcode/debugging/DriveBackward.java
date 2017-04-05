package org.firstinspires.ftc.teamcode.debugging;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MainRobotBase;
import org.firstinspires.ftc.teamcode.threading.ProgramFlow;

@Autonomous(name="Drive Backward", group = "Utility Group")

public class DriveBackward extends MainRobotBase
{
    //Custom initialization
    @Override
    protected void driverStationSaysINITIALIZE()
    {
        //Set the motor powers.
        leftDrive.setDirectMotorPower (-.5);
        rightDrive.setDirectMotorPower (.5);
    }

    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Set the motor powers.
        leftDrive.setDirectMotorPower (-.8);
        rightDrive.setDirectMotorPower (-.8);

        while (true)
            ProgramFlow.pauseForSingleFrame ();
    }
}