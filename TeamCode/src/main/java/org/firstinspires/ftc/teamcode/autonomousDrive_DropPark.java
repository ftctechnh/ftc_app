package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="autonomousDrive_DropPark", group = "Testing")
public class autonomousDrive_DropPark extends LinearOpMode
{
    Bogg robot;
    Mode action;
    ElapsedTime timer;
    final double ticksPerRev = 2240;
    final double inPerRev = Math.PI * 5;

    private enum Mode
    {
        Stop,
        Slide,
        Drop,
        Park
    }


    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        robot.driveEngine.driveAtAngle(Math.PI);
        action = Mode.Stop;

        waitForStart();
        action = Mode.Drop;
        timer = new ElapsedTime();
        timer.startTime();

        while (opModeIsActive())
        {
            double t = timer.seconds();
            switch(action)
            {
                case Drop:
                    if (t < 1) //for the first second
                    {
                        robot.lift(-0.7); //pull while we
                        robot.setBrake(false); //disengage the brake
                    } else if (robot.sensors.dMobile.getDistance(DistanceUnit.INCH) > 2.8) //if the robot is still off the ground
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
                    else{ //if the robot has unhooked
                        timer.reset();
                        action = Mode.Park;
                    }
                    break;
                case Park:
                    if(t < 3.5)
                    {
                        robot.driveEngine.drive(0,.4);
                    }
                    else //if t > 3.5
                        action = Mode.Stop;
                    break;

                default: //if action !(Drop || Park) e.g. Stop
                    robot.driveEngine.drive(0,0); //Stop driving
                    robot.lift(0); //Stop Lifting

            }

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
    public boolean encoderTest(double back_distance)
    {
        return robot.driveEngine.back.getCurrentPosition() / ticksPerRev * inPerRev < back_distance;
    }

}

