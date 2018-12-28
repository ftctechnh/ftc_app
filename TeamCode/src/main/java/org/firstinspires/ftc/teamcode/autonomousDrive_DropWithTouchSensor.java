package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="1: Drop with touch sensor", group = "Testing")
public class autonomousDrive_DropWithTouchSensor extends LinearOpMode
{
    Bogg robot;
    Auto auto;
    Auto.Mode action;


    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        auto = new Auto(robot, telemetry);
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
            idle();
        }
        auto.stop();
    }
}

