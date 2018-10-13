package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Josie on 10/5/2018.
 */
@Autonomous(name="Preciousss: auto_test_18_19_20:14", group="Preciousss")

public class auto_test_18_19 extends superAuto {

    public void runOpMode() {

        iAmRed = false;
        iAmBlue = true;

        setUp();
        configVuforiaRoverRuckus();
        goToPoint(-850,850);

    }
}