package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="autoTestRotateAndTranslate", group = "Testing")
public class autoTestRotateAndTranslate extends LinearOpMode {
    Bogg robot;
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, telemetry, Bogg.Name.Bogg);
        waitForStart();

        while (opModeIsActive())
        {
            if(robot.driveEngine.moveOnPath("hi", DriveEngine.Positioning.Absolute,
                    new double[]{0,5,Math.PI}))
                robot.driveEngine.moveOnPath(DriveEngine.Positioning.Absolute, false,
                        new double[]{0,0, Math.PI});

            robot.update();
            idle();
        }
    }
}
