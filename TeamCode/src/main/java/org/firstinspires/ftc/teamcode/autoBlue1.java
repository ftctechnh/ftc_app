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
        followHeading(0, .47f, 0f, 0.5f);//back up
        Wait(1);
        pivotTo(-90); //pivo 90 degrees to right clockwise
        Wait(1);
        cryptoState(-90,.5f, 0f);
        flip();
}
}