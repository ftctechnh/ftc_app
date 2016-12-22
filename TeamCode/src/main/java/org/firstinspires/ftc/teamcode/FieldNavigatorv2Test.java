package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Peter G on 12/22/2016.
 */
//@Autonomous (name = "FieldNavigatorv2Test", group = "Test")
public class FieldNavigatorv2Test extends LinearOpMode
{
    OmniDriveBot robot = new OmniDriveBot();
    FieldNavigatorv2 navigator = new FieldNavigatorv2(robot);
    public void runOpMode()
    {
        boolean targetDetected = false;
        navigator.setupVuforia();
        waitForStart();

        while(opModeIsActive())
        {
            for (short i = 0; i < 200; i++) {
                navigator.visionTrack();
                if (navigator.isDetectingTarget()) {
                    telemetry.addData("Can see target", null);
                    telemetry.update();
                    targetDetected = true;
                    break;
                }
                sleep(10);
            }

            if (targetDetected) {
                telemetry.addData("X Position", navigator.getCurrentX());
                telemetry.addData("Y Position", (1.2357 * navigator.getCurrentY()) + 0.8357);
                telemetry.addData("Current Degree", navigator.getCurrentDeg());
            } else {
                telemetry.addData("Error", null);
            }
            sleep(1000);
        }
    }
}
