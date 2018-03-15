package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Preciousss: autoPlay", group="Preciousss")

public class autoPlay extends superAuto {

    public void runOpMode() {
        iAmRed = true;
        setUp();
        //jewel();
        setUpVuforia();
       // Wait(3d);
       // followHeading(0, .47f, 0f, -0.25f);
       // Wait(3);
        //pivotTo(-90);
        Wait(3);
        findCrypto(-90,-.5f, 0f);
        telemetry.addData("We have gotten past findCrypto", iAmRed);
        flip();
    }
}