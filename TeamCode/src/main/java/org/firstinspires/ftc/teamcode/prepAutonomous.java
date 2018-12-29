package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="prepAutonomous", group = "Testing")
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
        waitForStart();
        action = Mode.Drop;

        robot.dropMarker(Bogg.Direction.Up);
        timer = new ElapsedTime();

        while (opModeIsActive())
        {
            double t = timer.seconds();
            switch(action)
            {
                case Drop:
                    if(t < 3) //for three seconds
                    {
                        robot.lift(-.3); //pull up
                        robot.rotateMobile(Bogg.Direction.Straight);
                    }
                    else if (t < 5)
                    {
                        robot.lift(-.5);
                    }
                    else if(t < 7)
                    {
                        robot.setBrake(true);
                    }
                    else if(t < 9) //wait for brake to engage
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
            telemetry.addLine("Push down on lift to ensure the brake catches");
            telemetry.addLine("Position block so the servo will hold it (9s)");
            telemetry.addLine("Don't let it catch your fingers!");
            telemetry.addData("time: ", t);
            telemetry.addData("brake position: ", robot.brake.getPosition());
            telemetry.addData("touch ", robot.sensors.touchBottom.isPressed());
            telemetry.addData("mode", action);
            telemetry.update();
            idle();
        }
    }


}

