package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="autonomousDrive_DropPlacePark", group = "Testing")
public class prepAutonomous extends LinearOpMode
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
        action = Mode.Stop;
        robot.sensors.rotateMobileX(0);
        robot.sensors.rotateMobileZ(-90);
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
                    if(t < 5) //for the two seconds
                    {
                        robot.lift(-.3); //pull up
                        //robot.sensors.dServoX.setPosition(0);
                    }
                    else if (t < 7)
                    {
                        robot.lift(-.5);
                    }
                    else if(t < 9)
                    {
                        robot.setBrake(true);
                        //robot.sensors.dServoZ.setPosition(-1);
                    }
                    else if(t < 11) //wait for brake to engage
                    {
                        robot.lift(0); //relax, which drops the robot
                    }
                    else //if t > 9
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

