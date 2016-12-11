package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by 111 on 9/29/2016.
 */
@TeleOp(name = "AutonomousTestOp", group = "Autonomous")
public class AutonomousTestOp extends OpMode
{
    private OmniDriveBot robot = new OmniDriveBot();

    public void init()
    {
        robot.init(hardwareMap);
    }

    public void loop()
    {
        if (gamepad1.a)
            robot.driveStraight(24, 0);
        else if (gamepad1.b)
            robot.driveStraight(24,30);
        else if (gamepad1.x)
            robot.driveStraight(24, 60);
        else if (gamepad1.y)
            robot.driveStraight(24, 90);
        else if (gamepad1.left_bumper)
            robot.driveStraight(24, 120);
        else if (gamepad1.right_bumper)
            robot.driveStraight(24, 150);
        else if (gamepad2.a)
            robot.driveStraight(24, 180);
        else if (gamepad2.b)
            robot.driveStraight(24,210);
        else if (gamepad2.x)
            robot.driveStraight(24, 240);
        else if (gamepad2.y)
            robot.driveStraight(24, 270);
        else if (gamepad2.left_bumper)
            robot.driveStraight(24,300);
        else if (gamepad2.right_bumper)
            robot.driveStraight(24, 330);
        else if (gamepad1.start)
            robot.driveStraight(24, 360);
        else if (gamepad2.start)
            robot.driveStraight(24, 390);
    }
}

