package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by GJF on 1/28/2018.
 */
@Autonomous(name="Preciousss: AutoBlue1", group="Preciousss")

public class autoBlue1 extends superAuto {

    public void runOpMode() {

        iAmRed = false;
        iAmBlue = true;

        setUp();
        jewel();
        setUpVuforia();
        followHeading(0, .37f, 0f, 0.5f);//back up
        Wait(1);
        pivotTo(-90); //pivot 90 degrees to right clockwise
        Wait(1);
        distCorrector(19.5);
        cryptoState(-90,.5f, 0f);
        followHeading(-90, .1, 0f, .35f);
        flip();
    }
}