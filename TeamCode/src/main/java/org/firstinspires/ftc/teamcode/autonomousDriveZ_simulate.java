package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

@Autonomous(name="Z: Fake", group = "Testing")
public class autonomousDriveZ_simulate extends LinearOpMode
{
    Auto auto;
    Auto.Mode action;


    @Override
    public void runOpMode()
    {
        auto = new Auto(hardwareMap, telemetry);
        gamepad1 = new Gamepad();
        gamepad1.setGamepadId(5);

        waitForStart();
        action = Auto.Mode.Drop;

        while (opModeIsActive())
        {
            gamepad1 = new Gamepad();
            gamepad1.setGamepadId(5);
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
                case MoveToDepot:
                    action = auto.moveToDepot();
                    break;
                case DropMarker:
                case MoveToCrater:
                    action = auto.moveToCrater();
                    break;
                default:
                    action = auto.stop();
            }

            // Display the current values
            telemetry.addData("mode", action);
            auto.update();
            idle();
        }
    }
}

