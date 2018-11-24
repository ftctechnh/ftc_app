package org.firstinspires.ftc.teamcode.TestPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.ParadeBot;

@Autonomous(name = "WallECalibrate")
@Disabled
public class WallECalibrate extends LinearOpMode
{
    ParadeBot wallE;

    public void runOpMode() throws InterruptedException
    {
        wallE = new ParadeBot(hardwareMap, this);
        boolean buttonPressed = false;
        waitForStart();
        while (opModeIsActive())
        {
            if (gamepad1.a)
            {
                wallE.pivot(-25);
                wallE.pivot(25);
            }
            else if (gamepad1.b)
            {
                wallE.pivot(25);
            }
        }
    }
}
