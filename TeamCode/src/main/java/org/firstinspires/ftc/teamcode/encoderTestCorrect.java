package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp(name="  encoderTestCorrect", group="Testing")
public class encoderTestCorrect extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, telemetry);
        waitForStart();

        while (opModeIsActive())
        {
            if (robot.driveEngine.moveOnPath(false, true,
                    new double[]{-6, 0},
                    new double[]{0, 4},
                    new double[]{6, 0},
                    new double[]{0, 24},
//                    new double[]{17, 0},
//                    new double[]{0, 12},
//                    new double[]{0, -12},
//                    new double[]{-17, 0},
//                    new double[]{0, 12},
//                    new double[]{0, -12},
//                    new double[]{-17, 0},
//                    new double[]{0, 12},
//                    new double[]{0, -12},
//                    new double[]{-17, 0},
                    new double[]{-34, 0},
                    new double[]{Math.PI / 4}))
            {
                robot.driveEngine.drive(0,0);
            }
            telemetry.update();
            robot.update();
            idle();
        }
    }
}

