package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by kawaiiPlat on 6/10/2017.
 */

@Autonomous(name = "Test Autonomous", group = "Testing")
public class TestAutonomous extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addLine("Press the play button when ready.");
        telemetry.update();

        // Wait for the driver to press the play button.
        waitForStart();

        telemetry.addLine("Finished.");
        telemetry.update();

        // 1000 millisecond wait
        Thread.sleep(1000);

        while(opModeIsActive()) {
            telemetry.addLine("In end loop.");
            telemetry.addLine("PaulWuzHere");
            telemetry.update();
        }
    }
}