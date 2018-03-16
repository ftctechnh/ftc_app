package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Preciousss: AutoRed1HELP", group="Preciousss")

public class autoRed1HELP extends superAuto {

    public void runOpMode() {
        iAmRed = true;

        setUp();
        jewel();
        setUpVuforia();
        Wait(.5d);
        followHeading(0, .47f, 0f, -0.25f);
        Wait(.5d);
        pivotTo(-90);
        Wait(.5d);
        followHeading(-90, 1f, -.5f, 0f);//REPLACES cryptoState(-90,-.5f, 0f);
        flip();
    }
}