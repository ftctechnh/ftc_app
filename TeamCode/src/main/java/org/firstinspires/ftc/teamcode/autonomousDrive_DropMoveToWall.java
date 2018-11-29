package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="3: Drop and move to wall", group = "Testing")
public class autonomousDrive_DropMoveToWall extends LinearOpMode
{
    Bogg robot;
    Auto auto;
    Mode action;

    private enum Mode
    {
        Stop,
        Drop,
        Slide,
        Spin,
        MoveToWall
    }


    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1);
        auto = new Auto(robot, telemetry);

        waitForStart();
        action = Mode.Drop;

        while (opModeIsActive())
        {
            switch(action)
            {
                case Drop:
                    if(auto.isDoneDropping())
                        action = Mode.Slide;
                    else
                        auto.drop();
                    break;
                case Slide:
                    if(auto.isDoneSliding())
                        action = Mode.Spin;
                    else
                        auto.slide();
                    break;
                case Spin:
                    if(auto.isDoneSpinning())
                        action = Mode.MoveToWall;
                    else
                        auto.spin();
                case MoveToWall:
                    if(auto.isDoneMovingToWall())
                        action = Mode.Stop;
                    else
                        auto.moveToWall();

                default:
                    auto.stop();

            }

            // Display the current values
            if(robot.camera != null) {
                if(robot.camera.targetVisible())
                {
                    //telemetry.addData("target seen", (robot.camera.targetVisible()) ? "N/A" : robot.camera.targetVisible().getName());
                    telemetry.addData("startPosition", auto.startPosition != null ? auto.startPosition : "N/A");
                    telemetry.addData("location", robot.camera.getLocation() == null ? "N/A" : robot.camera.getLocation());
                }
            }
            telemetry.addData("time", auto.getTime());
            telemetry.addData("mode", action);
            telemetry.update();
            idle();
        }
        auto.stop();
    }
}

