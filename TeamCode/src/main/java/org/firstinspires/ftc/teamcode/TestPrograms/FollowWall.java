package org.firstinspires.ftc.teamcode.TestPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.ParadeBot;

/**
 * Created by Sahithi Thumuluri on 10/5/18.
 */
@Autonomous(name = "FollowWall", group = "Auto")
@Disabled
public class FollowWall extends LinearOpMode
{
    ParadeBot paradeBot;
    public void runOpMode()
    {
        paradeBot = new ParadeBot(hardwareMap, this);
        double distRight = paradeBot.getDistFromRight_In();
        double distFront = paradeBot.getDistFromRight_In();
        telemetry.addData("distance right = ", distRight);
        telemetry.addData("distance front = ", distFront);
        telemetry.update();


    }
}
