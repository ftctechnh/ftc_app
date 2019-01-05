package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Josie on 10/5/2018.
 */
@Autonomous(name="Preciousss: autoTest", group="Preciousss")

public class autoTest extends superAuto {

    public void runOpMode() {

        iAmRed = false;
        iAmBlue = true;

        setUp();
        configVuforiaRoverRuckus();
        lowerRobot(6.9, 1);
        translate(-1,0,.5, 0.5);
        pivotToVuforia(-45);

        goToPoint(1200,-1200);
    }
}