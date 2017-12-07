package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Sahithi on 12/6/17.
 */
@Autonomous(name = "FinalRedAutoAudi", group = "Auto")
public class FinalRedAutoAudi extends LinearOpMode
{
    private NewRobotFinal newRobot;

    public void runOpMode()
    {
        newRobot = new NewRobotFinal(hardwareMap);
        waitForStart();
        newRobot.openOrCloseDoor(true);
        //raise lift slightly off ground

    }
}
