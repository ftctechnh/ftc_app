package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="autonomousDrive_DropWithTouchSensor", group = "Testing")
public class autonomousDrive_DropWithTouchSensor extends LinearOpMode
{
    Bogg robot;
    Auto auto;
    Mode action;

    private enum Mode
    {
        Stop,
        Drop
    }


    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        auto = new Auto(robot);
        action = Mode.Stop;

        waitForStart();
        action = Mode.Drop;

        while (opModeIsActive())
        {
            switch(action)
            {
                case Drop:
                    if(auto.isDoneDropping())
                        action = Mode.Stop;
                    else
                        auto.drop();
                    break;


                default:
                    auto.stop();

            }

            // Display the current values
            telemetry.addData("time: ", auto.getTime());
            telemetry.addData("brake position: ", robot.brake.getPosition());
            telemetry.addData("touch ", robot.sensors.touchBottom.isPressed());
            telemetry.addData("mode", action);
            telemetry.update();
            idle();
        }
        auto.stop();
    }


}

