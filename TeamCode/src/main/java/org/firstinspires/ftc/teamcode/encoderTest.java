package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="  encoderTest", group="Testing")
public class encoderTest extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        waitForStart();

        while (opModeIsActive())
        {
            if (robot.driveEngine.moveOnPath(
                    new double[]{-6, 0},
                    new double[]{0, 4},
                    new double[]{6, 0},
                    new double[]{0, 24},
                    new double[]{Math.PI / 4},
                    new double[]{-24, 24}))
            {
                robot.driveEngine.drive(0,0);
            }
            telemetry.update();
            idle();
        }
    }
}

