package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="5: Ambition", group = "Testing")
public class autonomousDrive5_SortPlacePark extends LinearOpMode
{
    Bogg robot;
    Auto auto;
    Auto.Mode action;


    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, telemetry, Bogg.Name.Bogg);
        auto = new Auto(robot, hardwareMap, telemetry);
        action = Auto.Mode.Drop;
        waitForStart();

        while (opModeIsActive())
        {
            switch(action)
            {
                case Drop:
                    action = auto.drop();
                    break;
                case LookForMinerals:
                    if(auto.camera.canUseTFOD) {
                        action = auto.lookForMinerals();
                        break;
                    }
                case Slide1:
                    action = auto.slide1();
                    break;
                case PushGold:
                    if(auto.camera.canUseTFOD)
                        action = auto.pushGold();
                    else
                        action = auto.pushGoldNoCamera();
                    break;
                case Slide2:
                    action = auto.slide2();
                    break;
                case TurnByCamera:
                    action = auto.turnByCamera();
                    break;
                case MoveToDepot:
                    action = auto.moveToDepot();
                    break;
                case DropMarker:
                    action = auto.dropMarker();
                case MoveToCrater:
                    action = auto.moveToCrater();
                default:
                    auto.stop();
            }

            // Display the current values
            telemetry.addData("mode:", action);
            telemetry.update();
            robot.update();
            idle();
        }
        auto.stop();
    }
}

