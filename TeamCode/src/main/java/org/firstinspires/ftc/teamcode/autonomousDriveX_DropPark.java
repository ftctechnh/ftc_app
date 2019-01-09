package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="X: Drop and park", group = "Testing")
public class autonomousDriveX_DropPark extends LinearOpMode
{
    Bogg robot;
    Auto auto;
    Auto.Mode action;


    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, telemetry);
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
                case PushGold: // ==Park
                    if(robot.driveEngine.moveOnPath(
                            new double[]{0,36},
                            new double[]{Math.PI / 2}))
                    {
                        robot.dropMarker(Bogg.Direction.Right);
                        robot.driveEngine.drive(0, 0);
                        action = Auto.Mode.Stop;
                    }
                    break;
                default:
                    auto.stop();

            }

            // Display the current values
            telemetry.addData("mode", action);
            telemetry.update();
            robot.update();
            idle();
        }
    }
}

