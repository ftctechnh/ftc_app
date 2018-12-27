package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="3: Drop and move to wall", group = "Testing")
public class autonomousDrive_DropMoveToWall extends LinearOpMode
{
    Bogg robot;
    private Auto auto;
    private Auto.Mode action;


    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        auto = new Auto(robot, telemetry);

        waitForStart();
        action = Auto.Mode.Drop;

        while (opModeIsActive())
        {
            switch(action)
            {
                case Drop:
                    action = auto.drop();
                    break;
                case Slide:
                    action = auto.slide();
                    break;
                case Spin:
                    action = auto.spin();
                    break;
                case MoveByEncoder:
                    action = auto.moveByEncoder();
                    break;
                default:
                    auto.stop();
            }

            // Display the current values
            // Removes the null checks by setting initial values; these checks sometimes caused NullPointerExceptions.

            telemetry.addData("time", auto.getTime());
            telemetry.addData("mode", action);         //put this before the things that break
            telemetry.addData("Drive x:", robot.driveEngine.xOut);
            telemetry.addData("Drive y:", robot.driveEngine.yOut);
            telemetry.addData("location", robot.camera.getLocation() != null ? robot.camera.getLocation()[0]: "N/A");
            telemetry.update();
            idle();
        }
        auto.stop();
    }
}

