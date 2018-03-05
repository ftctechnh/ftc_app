package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by Josie on 2/23/2018.
 */
@Autonomous(name="Preciousss: AutoRed2", group="Preciousss")

public class autoRed2 extends superAuto {

    public void runOpMode() {
        boxOrder[0] = RelicRecoveryVuMark.LEFT;
        boxOrder[1] = RelicRecoveryVuMark.CENTER;
        boxOrder[2] = RelicRecoveryVuMark.RIGHT;

        iAmRed = true;

       /* setUp();

        jewel();

        move(0f, -0.5f, .35f);

        Wait(1);

        move(-.75f,0f,1.0f);

        Wait(1);

        //Conveyor(3f);

        Wait(1);

        move(0f,-.25f,.5f);

        Wait(1);

        move(0f,.25f,.25f);*/
    }
}