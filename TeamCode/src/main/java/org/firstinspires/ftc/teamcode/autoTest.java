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
        goToPoint(-850,-850);

    }
}