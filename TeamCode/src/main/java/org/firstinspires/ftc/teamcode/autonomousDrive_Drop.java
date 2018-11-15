package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="autonomousDrive_Drop", group = "Testing")
public class autonomousDrive_Drop extends LinearOpMode
{
    Bogg robot;
    Mode action;
    ElapsedTime timer;

    private enum Mode
    {
        Stop,
        Drop
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

        while (opModeIsActive())
        {
            double t = timer.seconds();
            switch(action)
            {
                case Drop:
                    if (t < 4) //for the first second
                    {
                        robot.lift(-0.7); //pull while we
                        robot.setBrake(false); //disengage the brake
                    } else if (robot.sensors.dMobile.getDistance(DistanceUnit.INCH) > 2.8) //if the robot is still off the ground
                    {
                        robot.lift(.2); //push up, which drops the robot
                    }
                    else //if we are touching the ground
                        action = Mode.Stop;
                    break;


                default: //if action !Drop e.g. Stop
                    robot.driveEngine.drive(0,0); //Stop driving
                    robot.lift(0); //Stop Lifting

            }

            // Display the current values
            telemetry.addData("time: ", t);
            telemetry.addData("brake position: ", robot.brake.getPosition());
            telemetry.addData("touch ", robot.sensors.touchBottom.isPressed());
            telemetry.addData("mode", action);
            telemetry.update();
            idle();
        }
    }


}

