package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
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
        robot = new Bogg(hardwareMap, telemetry, Bogg.Name.Bogg);
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
                        robot.endEffector.pivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                        robot.lift(-.3); //pull up
                        robot.dropMarker(Bogg.Direction.Up);
                    }
                    else if (t < 5)
                    {
                        robot.lift(-.5);
                    }
                    else if(t < 7)
                    {
                        robot.endEffector.pivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                        robot.setBrake(Bogg.Direction.On);
                    }
                    else if(t < 9) //wait for brake to engage
                    {
                        robot.lift(0); //relax, which drops the robot
                    }
                    else //if t > 9
                        action = Mode.Stop;
                    break;


                default: //if action !Drop e.g. Stop
                    robot.driveEngine.stop(); //Stop driving
                    robot.lift(0); //Stop Lifting

            }

            // Display the current values
            telemetry.addLine("Push down on lift to ensure the brake catches");
            telemetry.addLine("Position block so the servo will hold it (9s)");
            telemetry.addLine("Don't let it catch your fingers!");
            telemetry.addData("time: ", t);
            telemetry.addData("touch ", robot.sensors.touchBottomIsPressed());
            telemetry.addData("mode", action);
            telemetry.update();
            robot.update();
            idle();
        }
    }


}

