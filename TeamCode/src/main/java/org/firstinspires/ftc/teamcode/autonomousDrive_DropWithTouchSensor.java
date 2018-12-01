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
        robot = new Bogg(hardwareMap, gamepad1);
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
            telemetry.addData("touch top", robot.sensors.touchTop.isPressed());
            telemetry.addData("brake position: ", robot.brake.getPosition());
            telemetry.addData("touch bottom", robot.sensors.touchBottom.isPressed());
            telemetry.addData("lift power", robot.liftAve);
            telemetry.addData("time: ", auto.getTime());
            telemetry.addData("mode", action);
            telemetry.update();
            idle();
        }
        auto.stop();
    }
}

