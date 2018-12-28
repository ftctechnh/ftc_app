package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="2: Drop and slide", group = "Testing")
public class autonomousDrive_DropSlide extends LinearOpMode
{
    Bogg robot;
    Auto auto;
    Auto.Mode action;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        auto = new Auto(robot, telemetry);

        waitForStart();
        action = Auto.Mode.Drop;

        while (opModeIsActive())
        {
            switch(action)
            {
                case Drop:
                    action = auto.drop();
                    break;
                case LookForMinerals:
                case Slide1:
                    action = auto.slide1();
                    break;
                default:
                    auto.stop();
            }

            // Display the current values
            telemetry.addData("back encoder inches", robot.driveEngine.back.getCurrentPosition() * DriveEngine.inPerTicks);
            telemetry.addData("mode", action);
            telemetry.addData("time: ", auto.getTime());
            telemetry.addData("touch top", robot.sensors.touchTop.isPressed());
            telemetry.update();
            idle();
        }
    }



}

