package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="autonomousDrop", group = "Testing")
public class autonomousDrop extends LinearOpMode
{
    Bogg robot;
    Mode action;
    ElapsedTime timer;

    private enum Mode
    {
        Stop,
        Drop,
    }

    private final double minServo = 40;
    private final double maxServo = 70;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        action = Mode.Stop;

        waitForStart();
        timer = new ElapsedTime();
        timer.startTime();
        action = Mode.Drop;
        boolean touchGround = false;

        while (opModeIsActive())
        {
            double t = timer.seconds();
            switch(action)
            {
                case Drop:
                    if(t < 1)
                    {
                        if(!robot.sensors.touchBottom.isPressed())
                            robot.lift(-0.7); //pull up to enable the brake to move
                        robot.setBrake(.6); //brake off
                    }
                    else if(robot.sensors.dMobile.getDistance(DistanceUnit.INCH) > 2.8)
                    {
                        robot.lift(.2); //push
                    }
                    else
                    {
                        action = Mode.Stop;
                    }

                    break;

                default:
                    robot.driveEngine.drive(0,0);
                    robot.lift(0);

            }

            // Display the current values
            telemetry.addData("time: ", t);
            telemetry.addData("touch ", robot.sensors.touchBottom.isPressed());
            telemetry.addData("mode", action);
            telemetry.update();
            idle();
        }
    }
}

