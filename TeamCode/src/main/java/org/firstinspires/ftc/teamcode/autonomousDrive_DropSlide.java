package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="autonomousDrive_DropSlide", group = "Testing")
public class autonomousDrive_DropSlide extends LinearOpMode
{
    Bogg robot;
    Auto auto;
    Mode action;

    private enum Mode
    {
        Stop,
        Slide,
        Drop
    }


    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        auto = new Auto(robot);
        robot.sensors.rotateMobileX(0);

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
                        action = Mode.Stop;
                    else
                        auto.slide();
                    break;
                default:
                    auto.stop();

            }

            // Display the current values
            telemetry.addData("time: ", auto.getTime());
            telemetry.addData("dMobile: ", robot.sensors.dMobile.getDistance(DistanceUnit.INCH));
            telemetry.addData("brake position: ", robot.brake.getPosition());
            telemetry.addData("target seen", (robot.camera.targetVisible() == null) ? "N/A" : robot.camera.targetVisible().getName());
            telemetry.addData("touch ", robot.sensors.touchBottom.isPressed());
            telemetry.addData("mode", action);
            telemetry.update();
            idle();
        }
    }



}

