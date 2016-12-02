package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by 111 on 9/29/2016.
 */
@TeleOp(name = "AutonomousTestOp", group = "Autonomous")
public class AutonomousTestOp extends LinearOpMode
{
    private OmniDriveBot robot = new OmniDriveBot();
    private FieldNavigator nav = new FieldNavigator(robot);

    public void runOpMode()
    {
        robot.init(hardwareMap);
        nav.setupVuforia();

        waitForStart();
        nav.setRobotLocation(0,0,0);
        nav.moveToPosition(12,12,0);
    }
}

