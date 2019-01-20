package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Disabled
@Autonomous(name="1: Drop with touch sensor", group = "Testing")
public class autonomousDrive1_DropWithTouchSensor extends LinearOpMode
{
    Bogg robot;
    Auto auto;
    Auto.Mode action;


    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, telemetry);
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

                default:
                    auto.stop();
            }

            // Display the current values
            telemetry.addData("mode", action);
            telemetry.update();
            robot.update();
            idle();
        }
        auto.stop();
    }
}

