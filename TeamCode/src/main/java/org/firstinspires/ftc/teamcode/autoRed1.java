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
        // We may need to put a loop around the code that reads the vuMark (inside setupvuforia)
        // to read until we get something other than UNKNOWN.
        // It should not be an unlimited loop, since we could get stuck there forever.
        followHeading(0, 1f, 0f, 0.5f);
        pivotTo(-90);
        findCrypto(-90,-.5f, 0f);
    }
}