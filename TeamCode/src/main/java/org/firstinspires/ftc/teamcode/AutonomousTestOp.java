package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by 111 on 9/29/2016.
 */
@Autonomous(name = "AutonomousTestOp", group = "Autonomous")
public class AutonomousTestOp extends LinearOpMode
{
    private OmniDriveBot robot = new OmniDriveBot();

    public void runOpMode() throws InterruptedException
    {
        robot.init(hardwareMap);
        waitForStart();
        robot.driveStraight(12, 45);
    }



}

