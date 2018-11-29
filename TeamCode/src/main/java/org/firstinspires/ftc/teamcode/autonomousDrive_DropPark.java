package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="X: Drop and park", group = "Testing")
public class autonomousDrive_DropPark extends LinearOpMode
{
    Bogg robot;
    Auto auto;
    Mode action;

    private enum Mode
    {
        Stop,
        Drop,
        Slide,
        Park
    }


    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1);
        auto = new Auto(robot, telemetry);

        waitForStart();
        action = Mode.Drop;

        while (opModeIsActive())
        {
            switch(action)
            {
                case Drop:
                    if(auto.isDoneDropping())
                        action = Mode.Slide;
                    else
                        auto.drop();
                    break;
                case Slide:
                    if(auto.isDoneSliding())
                        action = Mode.Park;
                    else
                        auto.slide();
                    break;
                case Park:
                    if(auto.getTime() > 3)
                        action = Mode.Stop;
                    else
                        robot.driveEngine.drive(0, .7);
                default:
                    auto.stop();

            }

            // Display the current values
            telemetry.addData("time: ", auto.getTime());
            telemetry.addData("dMobile: ", robot.sensors.dMobile.getDistance(DistanceUnit.INCH));
            telemetry.addData("brake position: ", robot.brake.getPosition());
            telemetry.addData("touch bottom", robot.sensors.touchBottom.isPressed());
            telemetry.addData("touch top", robot.sensors.touchTop.isPressed());
            telemetry.addData("mode", action);
            telemetry.update();
            idle();
        }
    }



}

