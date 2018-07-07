package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Josie 7/6/18
 */
@Autonomous(name="Preciousss: autoTestFunctions", group="Preciousss")

public class autoTestFunctions extends superAuto {

    public void runOpMode() {
        iAmRed = false;
        iAmBlue = true;
        setUp();
        goToPoint(-700, -700);
    }
}