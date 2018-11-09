package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.time.Clock;

@Autonomous(name="autonomousDrive_DropPlacePark", group = "Testing")
public class autonomousDrive_DropPlacePark extends LinearOpMode
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

                    else if(t >= .6)
                    {
                        robot.driveEngine.rotate(.05);

                        if(robot.camera.targetVisible() != null)
                        {
                            double[] location = robot.camera.getLocation();

                            double angle = Math.atan2(location[0], location[1]);

                            if(angle < -Math.PI/2)
                                startPosition = StartPosition.BackBlue;
                            else if(angle < 0)
                                startPosition = StartPosition.BackRed;
                            else if(angle < Math.PI/2)
                                startPosition = StartPosition.FrontRed;
                            else // angle between pi/2 and pi
                                startPosition = StartPosition.FrontBlue;
                            action = Mode.MoveToWall;
                        }
                    }
                    break;

                case MoveToWall:
                    double moveSpeed = .6;
                    double target_radius = 4; //inches
                    double accuracy_angle = 5;
                    switch(startPosition) {
                        case FrontRed:
                            if (!midtargetAchieved) { //if we haven't achieved the midtarget
                                if (robot.driveToTarget(0, 3, moveSpeed, target_radius))  //keep driving to the midtarget, also returns if we have, if so
                                    midtargetAchieved = true; //say that we have acheived the midtarget
                            } else if (robot.driveToTarget(0, 5.5, moveSpeed, target_radius)) //drive to the wall
                            {
                                if (robot.rotateToTarget(90, accuracy_angle)) //align to the wall; if we're good then
                                    action = Mode.MoveToDepot;                            //move to the depot
                            }
                            break;
                        case FrontBlue:
                            if (!midtargetAchieved) {
                                if (robot.driveToTarget(-3, 0, moveSpeed, target_radius))
                                    midtargetAchieved = true;
                            } else if (robot.driveToTarget(-5.5, 0, moveSpeed, target_radius)) {
                                if (robot.rotateToTarget(0, accuracy_angle))
                                    action = Mode.MoveToDepot;
                            }
                            break;
                        case BackRed:
                            if (!midtargetAchieved) {
                                if (robot.driveToTarget(3, 0, moveSpeed, target_radius))
                                    midtargetAchieved = true;
                            } else if (robot.driveToTarget(5.5, 0, moveSpeed, target_radius)) {
                                if (robot.rotateToTarget(270, accuracy_angle))
                                    action = Mode.MoveToDepot;
                            }
                            break;
                        case BackBlue:
                            if (!midtargetAchieved) {
                                if (robot.driveToTarget(-3, 0, moveSpeed, target_radius))
                                    midtargetAchieved = true;
                            } else if (robot.driveToTarget(-5.5, 0, moveSpeed, target_radius)) {
                                if (robot.rotateToTarget(180, accuracy_angle))
                                    action = Mode.MoveToDepot;
                            }
                            break;
                    }
                case MoveToDepot:
                    double x = 0;
                    switch (startPosition)
                    {
                        case BackBlue:
                            x = .7;
                            break;
                        case BackRed:
                            x = -.7;
                            break;
                        case FrontRed:
                            x = .7;
                            break;
                        case FrontBlue:
                            x = -.7;
                            break;
                    }
                    double y = robot.sensors.dFixed.getDistance(DistanceUnit.INCH);
                    robot.driveEngine.drive(x,y/6.0);

                    if(robot.sensors.dMobile.getDistance(DistanceUnit.INCH) < 6) { //TODO: need new sensor somewhere
                        action = Mode.DropMarker;
                        robot.sensors.rotateMobile(-90);
                        timer.reset();
                    }
                    break;

                case DropMarker:
                    robot.driveEngine.drive(0,0);
                    if(t > 4)
                        action = Mode.MoveToCrater;
                    break;

                case MoveToCrater:
                    x = -.7;
                    y = robot.sensors.dFixed.getDistance(DistanceUnit.INCH);
                    robot.driveEngine.drive(x,y/6.0);

                    if(robot.sensors.dMobile.getDistance(DistanceUnit.INCH) < 24) {
                        action = Mode.Stop;
                    }
                    break;


                default:
                    robot.driveEngine.drive(0,0);
                    robot.lift(0);

            }

//            if(gamepad1.right_bumper){timer.addTime(.0001);}
//            if(gamepad1.left_bumper){timer.addTime(-.0001);}

            // Display the current values
            telemetry.addData("time: ", t);
            telemetry.addData("brake x: ", x);
            telemetry.addData("target seen", (robot.camera.targetVisible() == null) ? "N/A" : robot.camera.targetVisible().getName());
            telemetry.addData("brake position", robot.brake.getPosition());
            telemetry.addData("touch ", robot.sensors.touchBottom.isPressed());
            telemetry.addData("mode", action);
            telemetry.update();
            idle();
        }
    }


}

