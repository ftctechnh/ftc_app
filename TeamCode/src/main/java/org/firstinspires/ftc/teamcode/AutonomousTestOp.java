package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by 111 on 9/29/2016.
 */
@TeleOp(name = "AutonomousTestOp", group = "Autonomous")
public class AutonomousTestOp extends LinearOpMode
{
    private OmniDriveBot robot = new OmniDriveBot();

    public void runOpMode() throws InterruptedException
    {
        robot.init(hardwareMap);
        waitForStart();
        int i = 0;
        boolean aButton = false;
        while(i < 7)
        {
            if(gamepad1.a == true)
            {
                aButton = true;
            }
            if(aButton == true)
            {
                int degree = 15 * i;
                robot.driveStraight(12, degree);
                i++;
                aButton = false;
            }
        }
    }
}

