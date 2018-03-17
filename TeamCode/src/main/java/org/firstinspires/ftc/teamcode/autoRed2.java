package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by Josie on 2/23/2018.
 */
@Autonomous(name="Preciousss: AutoRed2", group="Preciousss")

public class autoRed2 extends superAuto {

    public void runOpMode() {

        iAmRed = true;

        setUp();
        jewel();
        setUpVuforia();
        Wait(.5);
        followHeading(0, 1f, 0f, -.25f); //check that y is in right direction: drive forward
       // followHeading(0, .47f, -.25, 0f); //translate left, may already be lined up
        Wait(.5);
        distCorrector(18);
        cryptoState(-90,.5f, 0f);
        flip();
        Wait(1);
        distCorrector(20);
        Wait(1);
        distCorrector(18);
    }
}