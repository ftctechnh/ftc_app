package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Disabled
@Autonomous(name="2: Drop and slide", group = "Testing")
public class autonomousDrive2_DropSlide extends LinearOpMode
{
    Auto auto;
    Auto.Mode action;

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
                    action = auto.slide1();
                    break;
                default:
                    auto.stop();
            }

            // Display the current values
            telemetry.addData("mode", action);
            telemetry.update();
            auto.update();
            idle();
        }
    }



}

