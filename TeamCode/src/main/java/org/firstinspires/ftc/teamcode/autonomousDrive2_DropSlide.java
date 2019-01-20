package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Disabled
@Autonomous(name="2: Drop and slide", group = "Testing")
public class autonomousDrive2_DropSlide extends LinearOpMode
{
    Bogg robot;
    Auto auto;
    Auto.Mode action;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, telemetry);
        auto = new Auto(robot, hardwareMap, telemetry);

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
            telemetry.addData("mode", action);
            telemetry.update();
            robot.update();
            idle();
        }
    }



}

