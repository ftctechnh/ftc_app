package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="X: Drop and park", group = "Testing")
public class autonomousDrive_DropPark extends LinearOpMode
{
    Bogg robot;
    Auto auto;
    Auto.Mode action;


    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1);
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
                case Spin: // ==Park
                    if(auto.getTime() > 3)
                        action = Auto.Mode.Stop;
                    else
                        robot.driveEngine.drive(0, .7);
                    break;
                default:
                    auto.stop();

            }

            // Display the current values
            telemetry.addData("time: ", auto.getTime());
            telemetry.addData("mode", action);
            telemetry.addData("Drive x:", robot.driveEngine.xOut);
            telemetry.addData("Drive y:", robot.driveEngine.yOut);
            telemetry.addData("brake position: ", robot.brake.getPosition());
            telemetry.addData("touch bottom", robot.sensors.touchBottom.isPressed());
            telemetry.addData("touch top", robot.sensors.touchTop.isPressed());
            telemetry.update();
            idle();
        }
    }



}

