package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

 /* Created by GJF on 1/28/2018.
 */
@Autonomous(name="Preciousss: AutoBlue2", group="Preciousss")

public class autoBlue2 extends superAuto {

    public void runOpMode() {

        iAmRed = false;

        setUp();
        jewel();
       /* setUpVuforia();
        Wait(1);
        motorFL.setPower(.25);//pivot
        followHeading(0, .6f, 0f, 0.25f);//back up
        motorFR.setPower(-.25);
        motorBL.setPower(.25);
        motorBR.setPower(-.25);
        Wait(5.8);
        distCorrector(19.5);
        cryptoState(-178,.4f,0f);
        Wait(2);
        flip();*/
    }
}