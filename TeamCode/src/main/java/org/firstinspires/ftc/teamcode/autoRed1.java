package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by GJF on 1/28/2018.
 */
@Autonomous(name="Preciousss: AutoRed1", group="Preciousss")

public class autoRed1 extends superAuto {

    public void runOpMode() {
        boxOrder[0] = RelicRecoveryVuMark.LEFT;
        boxOrder[1] = RelicRecoveryVuMark.CENTER;
        boxOrder[2] = RelicRecoveryVuMark.RIGHT;

        iAmRed = true;

        setUp();
        jewel();
        pivotTo(-90);
    }
}