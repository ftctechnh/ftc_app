package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by GJF on 1/28/2018.
 */
@Autonomous(name="Preciousss: AutoRed1", group="Preciousss")

public class autoRed1 extends superAuto {

    public void runOpMode() {

        iAmRed = true;

        configureGyro();

        mapHardware();

        composeTelemetry();

        waitForStart();

        pivotTo(-90);
    }
}
