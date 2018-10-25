package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "WallECalibrate")
public class WallECalibrate extends LinearOpMode
{
    ParadeBot wallE;

    public void runOpMode() throws InterruptedException
    {
        wallE = new ParadeBot(hardwareMap, this);
        waitForStart();
        wallE.getDriveRightOne().setPower(1);
        wallE.getDriveLeftOne().setPower(1);
        sleep(2000);
    }
}
