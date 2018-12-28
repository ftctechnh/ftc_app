package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="3: Drop and move to wall", group = "Testing")
public class autonomousDrive3_DropMoveToWall extends LinearOpMode
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
                case LookForMinerals:
                case Slide1:
                    action = auto.slide1();
                    break;
                case PushGold:
                case Slide2:
                    action = auto.slide2();
                    break;
                case TurnByCamera:
                    action = auto.turnByCamera();
                    break;
                default:
                    auto.stop();
            }

            // Display the current values
            telemetry.addData("mode", action);         //put this before the things that break
            telemetry.update();
            idle();
        }
        auto.stop();
    }
}

