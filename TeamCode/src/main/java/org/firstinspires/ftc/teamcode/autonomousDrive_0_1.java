package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

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

    private enum StartPosition
    {
        FrontBlue,
        BackBlue,
        FrontRed,
        BackRed
    }

    private final double ftPerInch = 1.0/12.0;
    private final double tilesPerInch = 1.0/24.0;

    private final double minServo = 40;
    private final double maxServo = 70;


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
                    //do something
                    if(false) //if condition
                    {
                        action = Mode.MoveToWall;
                    }
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

            // Display the current values
            telemetry.addData("leftx: ", gamepad1.left_stick_x);
            telemetry.update();
            idle();
        }
    }


}

