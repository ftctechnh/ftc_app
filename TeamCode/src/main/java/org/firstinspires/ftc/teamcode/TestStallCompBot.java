package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by Jeremy on 1/14/2018.
 */
@TeleOp(name = "Test Stall on Comp Bot")
public class TestStallCompBot extends LinearOpMode
{
    NewRobotFinal newRobot;
    public void runOpMode()
    {
        newRobot = new NewRobotFinal(hardwareMap);
        newRobot.initVuforia(hardwareMap);
        waitForStart();
        newRobot.driveStraight_In(33);
        newRobot.pivot(-90);
        newRobot.driveStraight_In_Stall(54, .5, telemetry);
        while(!gamepad1.a);

    }
}
