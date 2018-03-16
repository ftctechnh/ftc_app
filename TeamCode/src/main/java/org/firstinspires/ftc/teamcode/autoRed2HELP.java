package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Josie on 2/23/2018.
 */
@Autonomous(name="Preciousss: AutoRed2HELP", group="Preciousss")

public class autoRed2HELP extends superAuto {

    public void runOpMode() {

        iAmRed = true;

        setUp();
        jewel();
        setUpVuforia();
        Wait(.5d);
        followHeading(0, .47f, 0f, .25f); //check that y is in right direction: drive forward
        Wait(.5d);
       // followHeading(0, .47f, -.25, 0f); //translate left, may already be lined up
        followHeading(-90, 1d, -.5f, 0f); //REPLACES findCrypto(-90,-.5f, 0f);

        flip();

    }
}