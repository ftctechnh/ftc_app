package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Sahithi on 1/3/18.
 */
@Autonomous(name = "Test Stall",group = "Auto")
public class TestStall extends LinearOpMode
{
    NewRobotFinal newRobot;
    public void runOpMode()
    {
        newRobot = new NewRobotFinal(hardwareMap);
        newRobot.initVuforia(hardwareMap);
        {
            newRobot.driveStraight_In(20);
        }
    }
}
