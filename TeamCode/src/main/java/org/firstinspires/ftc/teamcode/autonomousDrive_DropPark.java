package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="X: Drop and park", group = "Testing")
public class autonomousDrive_DropPark extends LinearOpMode
{
    Bogg robot;
    Auto auto;
    Auto.Mode action;


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
                case PushGold: // ==Park
                    double inchesMovedX = robot.driveEngine.xDist();
                    double inchesMovedY = robot.driveEngine.yDist();
                    if(inchesMovedY < 4 * 12) {
                        robot.driveEngine.drive(0, .3);
                        robot.driveEngine.resetDistances();
                    }
                    else if(inchesMovedX < Math.PI * 1 /2){
                        robot.driveEngine.rotate(.2);
                    }
                    else{
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
            idle();
        }
    }



}

