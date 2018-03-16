package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by GJF on 1/28/2018.
 */
@Autonomous(name="Preciousss: AutoBlue1HELP", group="Preciousss")

public class autoBlue1HELP extends superAuto {

    public void runOpMode() {

        iAmRed = false;

        setUp();
        jewel();
        setUpVuforia();
        Wait(.5d);
        followHeading(0, 1f, 0f, 0.5f);//back up
        Wait(.5d);
        pivotTo(90); //pivo 90 degrees to right clockwise
        Wait(.5d);
        followHeading(90, 1d, .5f,0f);//REPLACES cryptoState(90,.5f,0f)
        flip();
    }
}