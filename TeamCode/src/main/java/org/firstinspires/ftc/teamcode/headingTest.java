package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Josie Miner on 2/9/2018.
 */
@Autonomous(name="Preciousss: headingTest", group="Preciousss")

public class headingTest extends superAuto {

    public void runOpMode() {
        setUp();
        fancyGyroPivot(90);
        Wait(2);setUp();
        fancyGyroPivot(-45);
        Wait(2);setUp();
        fancyGyroPivot(-165);
        Wait(2);
        fancyGyroPivot(135);
        Wait(2);
        fancyGyroPivot(0);
        Wait(2);
        fancyGyroPivot(180);
        Wait(2);

    }
}