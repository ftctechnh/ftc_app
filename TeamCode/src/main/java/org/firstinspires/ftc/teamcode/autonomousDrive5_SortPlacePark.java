package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="5: Ambition", group = "Testing")
public class autonomousDrive5_SortPlacePark extends LinearOpMode
{
    Auto auto;
    Auto.Mode action;


    @Override
    public void runOpMode()
    {
        auto = new Auto(Bogg.Name.Bogg, hardwareMap, telemetry);
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
                    break;
                case MoveToCrater:
                    action = auto.moveToCrater();
                    break;
                default:
                    action = auto.stop();
            }

            // Display the current values
            telemetry.addData("mode:", action);
            auto.update();
            idle();
        }
        auto.stop();
    }
}

