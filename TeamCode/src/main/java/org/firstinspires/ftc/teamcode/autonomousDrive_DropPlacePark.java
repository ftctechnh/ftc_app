package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="autonomousDrive_DropPlacePark", group = "Testing")
public class autonomousDrive_DropPlacePark extends LinearOpMode
{
    Bogg robot;
    Mode action;
    ElapsedTime timer;
    final double ticksPerRev = 2240;
    final double inPerRev = Math.PI * 5;
    double x;

    private enum Mode
    {
        Stop,
        Drop,
        Slide,
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


    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
        action = Mode.Stop;

        waitForStart();
        action = Mode.Drop;
        startPosition = StartPosition.BackBlue;
        boolean midtargetAchieved = false;
        timer = new ElapsedTime();
        timer.startTime();

        while (opModeIsActive())
        {
            double t = timer.seconds();
            double mobileDistance = robot.sensors.dMobile.getDistance(DistanceUnit.INCH);
            double fixedDistance = robot.sensors.dFixed.getDistance(DistanceUnit.INCH);
            switch(action)
            {
                case Drop:
                    if (t < 1) //for the first second
                    {
                        robot.lift(-0.7); //pull while we
                        robot.setBrake(false); //disengage the brake
                    } else if (mobileDistance > 2.8) //if the robot is still off the ground
                    {
                        robot.lift(.2); //push up, which drops the robot
                    }
                    else {
                        timer.reset();
                        action = Mode.Slide;
                    }
                case Slide:
                    if(t < .3) //for an additional .3 seconds
                    {
                        robot.lift(.2); //drop a bit more
                    }
                    else if(encoderTest(4)) //the back encoder has moved less than 4 inches
                    {
                        robot.lift(0); //stop the lift motor
                        robot.driveEngine.drive(.1,0); //drive to the side to unhook
                    }
                    else
                    {
                        if(t/1.5 - Math.floor(t)/1.5 < .60)  //rotates for 60% of 1.5 seconds
                            robot.driveEngine.rotate(.1);
                        else
                            robot.driveEngine.rotate(0);  //and stops so we can see the target

                        //robot.driveEngine.rotate(t/1.5 - Math.floor(t/1.5) < .60 ? .1 : 0);

                        if(robot.camera.targetVisible() != null) //if we see a camera (null = nothing)
                        {
                            double[] location = robot.camera.getLocation(); //get a location, looks like [5.65,-2.54]

                            double angle = Math.atan2(location[0], location[1]); //get the angle looking down on the field, lander to robot

                            if(angle < -Math.PI/2)                          //Quadrant 3
                                startPosition = StartPosition.BackBlue;
                            else if(angle < 0)                              //Quadrant 4
                                startPosition = StartPosition.BackRed;
                            else if(angle < Math.PI/2)                      //Quadrant 1
                                startPosition = StartPosition.FrontRed;
                            else // angle between pi/2 and pi               //Quadrant 2
                                startPosition = StartPosition.FrontBlue;
                            action = Mode.MoveToWall;
                        }
                    }
                    break;

                case MoveToWall:
                    if(robot.camera.targetVisible() == null){
                        //robot.driveEngine.rotate(t/1.5 - Math.floor(t/1.5) < .60 ? .1 : 0);
                    }

                    switch (startPosition)
                    {
                        case BackBlue:
                        case FrontRed:
                            robot.sensors.rotateMobileX(90);
                            break;
                        case BackRed:
                        case FrontBlue:
                            robot.sensors.rotateMobileX(-90);
                    }

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
                                if (robot.rotateToTargetAngle(90, accuracy_angle)) //align to the wall; if we're good then
                                    action = Mode.MoveToDepot;                            //move to the depot
                            }
                            break;
                        case FrontBlue:
                            if (!midtargetAchieved) {
                                if (robot.driveToTarget(-3, 0, moveSpeed, target_radius))
                                    midtargetAchieved = true;
                            } else if (robot.driveToTarget(-5.5, 0, moveSpeed, target_radius)) {
                                if (robot.rotateToTargetAngle(180, accuracy_angle))
                                    action = Mode.MoveToDepot;
                            }
                            break;
                        case BackRed:
                            if (!midtargetAchieved) {
                                if (robot.driveToTarget(3, 0, moveSpeed, target_radius))
                                    midtargetAchieved = true;
                            } else if (robot.driveToTarget(5.5, 0, moveSpeed, target_radius)) {
                                if (robot.rotateToTargetAngle(0, accuracy_angle))
                                    action = Mode.MoveToDepot;
                            }
                            break;
                        case BackBlue:
                            if (!midtargetAchieved) {
                                if (robot.driveToTarget(-3, 0, moveSpeed, target_radius))
                                    midtargetAchieved = true;
                            } else if (robot.driveToTarget(-5.5, 0, moveSpeed, target_radius)) {
                                if (robot.rotateToTargetAngle(270, accuracy_angle))
                                    action = Mode.MoveToDepot;
                            }
                            break;
                    }
                case MoveToDepot:
                    switch (startPosition)
                    {
                        case BackBlue:
                        case FrontRed:
                            x = .7;
                            break;
                        default:
                            x = -.7;
                            break;
                    }
                    double y = fixedDistance;
                    robot.driveEngine.drive(x,-(y-6)/6.0);

                    if(mobileDistance < 6) { //TODO: need new sensor somewhere or new servo
                        action = Mode.DropMarker;
                        robot.sensors.rotateMobileX(-90);
                        timer.reset();
                    }
                    break;

                case DropMarker:
                    robot.driveEngine.drive(0,0);
                    if(t > 4)
                        action = Mode.MoveToCrater;
                    break;

                case MoveToCrater:
                    switch (startPosition)
                    {
                        case BackBlue:
                        case FrontRed:
                            x = -.7;
                            break;
                        default:
                            x = .7;
                            break;
                    }
                    y = fixedDistance;
                    robot.driveEngine.drive(x,-(y-6)/6.0);

                    if(mobileDistance < 24) {
                        action = Mode.Stop;
                    }
                    break;


                default:
                    robot.driveEngine.drive(0,0);
                    robot.lift(0);

            }


            // Display the current values
            telemetry.addData("time: ", t);
            telemetry.addData("mobile distance: ", mobileDistance);
            telemetry.addData("fixed distance", fixedDistance);
            telemetry.addData("target seen", (robot.camera.targetVisible() == null) ? "N/A" : robot.camera.targetVisible().getName());
            telemetry.addData("brake position", robot.brake.getPosition());
            telemetry.addData("touch ", robot.sensors.touchBottom.isPressed());
            telemetry.addData("mode", action);
            telemetry.update();
            idle();
        }
    }

    public boolean encoderTest(double back_distance)
    {
        return robot.driveEngine.back.getCurrentPosition() / ticksPerRev * inPerRev < back_distance;
    }

}

