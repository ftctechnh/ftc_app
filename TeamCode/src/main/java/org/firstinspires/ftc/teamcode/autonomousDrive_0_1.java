package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.time.Clock;

@Autonomous(name="autonomousDrive_0_1", group = "Testing")
public class autonomousDrive_0_1 extends LinearOpMode
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
        timer = new ElapsedTime();
        timer.startTime();

        while (opModeIsActive())
        {
            double t = timer.seconds();
            switch(action) //TODO: complete actions and conditions
            {
                case Drop:
                    if(t < 1)
                    {
                        if(!robot.sensors.touchBottom.isPressed())
                            robot.lift(-0.7); //pull
                        robot.setBrake(false);
                    }
                    else if(t < 6)
                    {
                        robot.lift(.2); //push
                    }
                    else if(t < 9)
                    {
                        robot.lift(0);
                        robot.driveEngine.drive(.8,0);
                    }

                    else if(t > 9) //if condition
                    {
                        robot.driveEngine.rotate(-.2);
                    }
                    if(robot.camera.targetVisible() != null)
                    {
                        double[] location = robot.camera.getLocation();

                        if(location[0] > 0 && location[1] > 0)
                            startPosition = StartPosition.FrontRed;
                        else if(location[0] < 0 && location[1] > 0)
                            startPosition = StartPosition.FrontBlue;
                        else if(location[0] > 0 && location[1] < 0)
                            startPosition = StartPosition.BackRed;
                        else // location[0] < 0 && location[1] < 0
                            startPosition = StartPosition.BackBlue;
                        action = Mode.MoveToWall;
                    }
                    break;

                case MoveToWall:
                    switch(startPosition)
                    {
                        case FrontRed:
                            if(!midtargetAchieved) {
                                if(robot.driveToTarget(0, 3, .6, 4))
                                    midtargetAchieved = true;
                            }
                            else
                                if(robot.driveToTarget(0,5.5,.6,4))
                                {
                                    if(robot.rotateToTarget(90, 5))
                                    action = Mode.MoveToDepot;
                                }
                            break;
                        case FrontBlue:
                            if(!midtargetAchieved) {
                                if(robot.driveToTarget(-3, 0, .6, 4))
                                    midtargetAchieved = true;
                            }
                            else
                            if(robot.driveToTarget(-5.5,0,.6,4))
                                {
                                    if(robot.rotateToTarget(0, 5))
                                        action = Mode.MoveToDepot;
                                }
                            break;
                        case BackRed:
                            if(!midtargetAchieved) {
                                if(robot.driveToTarget(3, 0, .6, 4))
                                    midtargetAchieved = true;
                            }
                            else
                            if(robot.driveToTarget(5.5,0,.6,4))
                                {
                                    if(robot.rotateToTarget(270, 5))
                                        action = Mode.MoveToDepot;
                                }
                            break;
                        case BackBlue:
                            if(!midtargetAchieved) {
                                if(robot.driveToTarget(-3, 0, .6, 4))
                                    midtargetAchieved = true;
                            }
                            else
                            if(robot.driveToTarget(-5.5,0,.6,4))
                                {
                                    if(robot.rotateToTarget(180, 5))
                                        action = Mode.MoveToDepot;
                                }
                            break;
                    }
                    if(robot.sensors.dFixed.getDistance(DistanceUnit.INCH) < 4) //condition
                        action = Mode.MoveToDepot;
                    break;

                case MoveToDepot:
                    double x = .7;
                    double y = robot.sensors.dFixed.getDistance(DistanceUnit.INCH);
                    robot.driveEngine.drive(x,y/6.0);

                    if(robot.sensors.dMobile.getDistance(DistanceUnit.INCH) < 6) {
                        action = Mode.DropMarker;
                        timer.reset();
                    }
                    break;

                case DropMarker:
                    robot.driveEngine.drive(0,0);
                    if(t > 4) //if condition
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
            telemetry.addData("time: ", t);
            telemetry.addData("servo x: ", x);
            telemetry.addData("servo power", robot.brake.getPosition());
            telemetry.addData("touch ", robot.sensors.touchBottom.isPressed());
            telemetry.addData("mode", action);
            telemetry.update();
            idle();
        }
    }


}

