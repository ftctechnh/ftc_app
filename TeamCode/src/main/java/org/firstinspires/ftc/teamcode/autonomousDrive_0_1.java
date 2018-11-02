package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.time.Clock;

@Autonomous(name="autonomousDrive_0_1", group = "Testing")
public class autonomousDrive_0_1 extends LinearOpMode
{
    Bogg robot;
    Mode action;

    private enum Mode
    {
        Stop,
        Drop,
        MoveToWall,
        MoveToDepot,
        DropMarker,
        MoveToCrater
    }

    private final double ftPerInch = 1.0/12.0;
    private final double tilesPerInch = 1.0/24.0;

    private final double minServo = 40;
    private final double maxServo = 70;

    double x = .5;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1);
        action = Mode.Stop;
        waitForStart();
        action = Mode.Drop;

        while (opModeIsActive())
        {
            switch(action) //TODO: complete actions and conditions
            {
                case Drop:
                    //if timer<.3 and touch !pressed, motor down
                    robot.setBrake(false);
                    if(robot.sensors.touchBottom.isPressed()) //if condition
                        robot.lift(0);
                    break;

                case MoveToWall:
                    //do something
                    if(false) //condition
                        action = Mode.MoveToDepot;
                    break;

                case MoveToDepot:
                    //do something
                    if(false) //condition
                        action = Mode.DropMarker;
                    break;

                case DropMarker:
                    //do something
                    if(false) //if condition
                        action = Mode.MoveToCrater;
                    break;

                case MoveToCrater:
                    //do something
                    if(false) //condition
                        action = Mode.Stop;
                    break;

                default:
                    //do something (actually nothing but we need to tell the robot that.)

            }
            if(gamepad1.dpad_up)
            {
                x+=.001;
            }

            if(gamepad1.dpad_down)
            {
                x-=.001;
            }
            robot.setBrake(x);

            // Display the current values
            telemetry.addData("leftx: ", gamepad1.left_stick_x);
            telemetry.addData("servo x: ", x);
            telemetry.addData("servo diff: ", x-robot.brake.getPosition());
            telemetry.addData("touch ", robot.sensors.touchBottom.isPressed());
            telemetry.addData("mode", action);
            telemetry.update();
            idle();
        }
    }


}

