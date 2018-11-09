package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="autonomousDrive_DropPlacePark", group = "Testing")
public class autonomousDrive_DropPark extends LinearOpMode
{
    Bogg robot;
    Mode action;
    ElapsedTime timer;

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
    public StartPosition startPosition;


    private final double ftPerInch = 1.0/12.0;
    private final double tilesPerInch = 1.0/24.0;

    private final double minServo = 40;
    private final double maxServo = 70;

    double x = .5;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        action = Mode.Stop;
        robot.sensors.rotateMobile(0);
        waitForStart();
        action = Mode.Drop;
        startPosition = StartPosition.BackBlue;
        boolean midtargetAchieved = false;
        boolean touchGround = false;
        timer = new ElapsedTime();
        timer.startTime();

        while (opModeIsActive())
        {
            double t = timer.seconds();
            switch(action)
            {
                case Drop:
                    if(t < 1) //for the first second
                    {
                        robot.lift(-0.7); //pull while we
                        robot.setBrake(.6); //disengage the brake
                    }
                    else if(robot.sensors.dMobile.getDistance(DistanceUnit.INCH) > 2.8) //if the robot is still off the ground
                    {
                        robot.lift(.2); //push up, which drops the robot
                    }
                    else if(!touchGround) //if this variable hasn't been set to true yet
                    {
                        touchGround = true; //set it to true
                        timer.reset();  //reset the timer (needed because we don't know how long the first section will take)
                    }
                    else if(t < .1) //for an additional .1 seconds
                    {
                        robot.lift(.2); //drop a bit more
                    }
                    else if(t < .6) //for the next .5 seconds
                    {
                        robot.lift(0); //stop the lift motor
                        robot.driveEngine.drive(.2,0); //drive to the side to unhook
                    }

                    else if(t < 4) //for the next 3.4 seconds
                    {
                        robot.driveEngine.drive(-.4,.4); //drive diagonally

                    }
                    else //if t > 4
                        action = Mode.Stop;
                    break;

                default: //if action !Drop e.g. Stop
                    robot.driveEngine.drive(0,0); //Stop driving
                    robot.lift(0); //Stop Lifting

            }

//            if(gamepad1.right_bumper){timer.addTime(.0001);}
//            if(gamepad1.left_bumper){timer.addTime(-.0001);}

            // Display the current values
            telemetry.addData("time: ", t);
            telemetry.addData("brake position: ", robot.brake.getPosition());
            telemetry.addData("target seen", (robot.camera.targetVisible() == null) ? "N/A" : robot.camera.targetVisible().getName());
            telemetry.addData("brake position", robot.brake.getPosition());
            telemetry.addData("touch ", robot.sensors.touchBottom.isPressed());
            telemetry.addData("mode", action);
            telemetry.update();
            idle();
        }
    }


}

