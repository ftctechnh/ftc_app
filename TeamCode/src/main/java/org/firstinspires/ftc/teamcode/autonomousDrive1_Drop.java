package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="1: Drop", group = "Testing")
public class autonomousDrive1_Drop extends LinearOpMode
{
    private Auto auto;
    private Auto.Mode action;


    @Override
    public void runOpMode()
    {
        auto = new Auto(Bogg.Name.Bogg, hardwareMap, telemetry);

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
                    auto.robot.driveEngine.moveOnPath(true, true,
                            new double[]{0,0});
                    break;
                default:
                    auto.stop();
            }

            // Display the current values
            telemetry.addData("mode", action);         //put this before the things that break
            auto.update();
            idle();
        }
        auto.stop();
    }
}

