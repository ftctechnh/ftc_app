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

        followHeading(0, .5, 0f, -0.35f);

        Wait(.2d);

        pivotTo(-90);

        Wait(.2d);

        followHeading(-90, 1, -.5f, 0f);

    }
}