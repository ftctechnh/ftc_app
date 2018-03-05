package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by GJF on 1/28/2018.
 */
@Autonomous(name="Preciousss: AutoBlue1 16:43", group="Preciousss")

public class autoBlue1 extends superAuto {

    public void runOpMode() {
        boxOrder[0] = RelicRecoveryVuMark.RIGHT;
        boxOrder[1] = RelicRecoveryVuMark.CENTER;
        boxOrder[2] = RelicRecoveryVuMark.LEFT;

        iAmRed = false;

        setUp();
        jewel();
        setUpVuforia();
        followHeading(0, 1f, 0f, 0.5f);
        pivotTo(-90);
        findCrypto(-90,-.5f, 0f);
    }
}