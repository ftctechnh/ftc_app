package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Paul Lackey on 7/15/2017.
 */

@Autonomous(name = "spin to win", group = "test")
public class PaulTest extends LinearOpMode {

    public void runOpMode () {

        telemetry.addLine("Initializing hardware... do not press play!");
        telemetry.update();
        TileRunner hardware = new TileRunner(); // hardwareTileRunner
        telemetry.addLine("Hardware initialized. Press play to start.");
        telemetry.update();

        waitForStart();

        telemetry.addLine("paul is here");
        telemetry.update(); // :   ^}
        sleep(1000);

        if(hardware.particleServo != null) {
            hardware.particleServo.setPosition(0.4);
        } else {
            telemetry.addLine("particleServo is null");
            telemetry.update();
        }

        sleep(2000);

        while (opModeIsActive()) {

        }
    }
}
