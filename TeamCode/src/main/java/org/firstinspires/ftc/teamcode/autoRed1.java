package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

@Autonomous(name="Preciousss: AutoRed1", group="Preciousss")

public class autoRed1 extends superAuto {

    public void runOpMode() {
        iAmRed = true;

        setUp();
        jewel();
        setUpVuforia();
        Wait(.5);
        followHeading(0, .45f, 0f, -0.25f);
        Wait(.5);
        pivotTo(-90);
        Wait(.5);
        //move Rught
        followHeading(-90, .37, .5f, 0f);
        Wait(.5);
        distCorrector(19.5);
        cryptoState(-90,-.5f, 0f);
        Wait(.5);
        followHeading(-90, .1f, .35f, 0);
        Wait(.5);
        flip();

    }
}