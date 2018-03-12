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
        followHeading(0, .47f, 0f, -0.25f);
        Wait(.8);
        pivotTo(-90);
        //Wait(5);
        //findCrypto(-90,-.5f, 0f);
    }
}