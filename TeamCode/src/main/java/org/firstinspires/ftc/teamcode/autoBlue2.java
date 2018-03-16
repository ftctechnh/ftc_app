package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by GJF on 1/28/2018.
 */
@Autonomous(name="Preciousss: AutoBlue2", group="Preciousss")

public class autoBlue2 extends superAuto {

    public void runOpMode() {

        iAmRed = false;

        setUp();
        jewel();
        setUpVuforia();
        followHeading(0, 1f, 0f, 0.5f);//back up
        Wait(1);
        pivotTo(-179); //pivot
        Wait(1);
        cryptoState(-179,.5f, 0f);
        flip();
    }
}