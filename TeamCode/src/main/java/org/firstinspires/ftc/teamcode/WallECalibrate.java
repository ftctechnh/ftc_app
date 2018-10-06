package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "WallECalibrate")
public class WallECalibrate extends LinearOpMode
{
    ParadeBot wallE;

    public void runOpMode() throws InterruptedException
    {
        wallE = new ParadeBot(hardwareMap);
        waitForStart();
        wallE.driveStraight_In(2000,.5);

        while (!isStopRequested())
            wallE.stopDriveMotors();
    }
}
