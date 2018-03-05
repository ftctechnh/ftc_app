package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Josie Miner on 2/9/2018.
 */
@Autonomous(name="Preciousss: translateTest", group="Preciousss")

public class headingTest extends superAuto {

    public void runOpMode() {
        configureGyro();

        mapHardware();

        composeTelemetry();

        waitForStart();

      findCrypto(0, -.5f, 0f);

    }
}