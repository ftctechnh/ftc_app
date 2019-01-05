package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Josie on 10/5/2018.
 */
@Autonomous(name="Preciousss: lower", group="Preciousss")

public class lower extends superAuto {

    public void runOpMode() {

        iAmRed = false;
        iAmBlue = true;

        setUp();
        configVuforiaRoverRuckus();
        lowerRobot(7.2, -1);
    }
}