package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Drive Backward", group = "Utility Group")

public class DriveBackward extends _AutonomousBase
{
    //Custom initialization
    @Override
    protected void driverStationSaysINITIALIZE()
    {
        //Set the motor powers.
        setLeftPower(.4);
        setRightPower(-.4);
    }

    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Set the motor powers.
        setLeftPower(-.6);
        setRightPower(-.6);

        while (true)
            idle();
    }
}