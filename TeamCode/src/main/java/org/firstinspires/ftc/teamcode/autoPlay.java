package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Preciousss: autoPlay", group="Preciousss")

public class autoPlay extends superAuto {

    public void runOpMode() {
        iAmRed = true;
        setUp();
        distCorrector(23);
    }
}