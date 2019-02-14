package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="  float motors", group="Testing")
public class floatMotors extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = Bogg.determineRobot(hardwareMap, telemetry);

        waitForStart();

        while (opModeIsActive())
        {
            robot.floatMotors();

            telemetry.update();
            robot.update();
            idle();
        }
    }
}

