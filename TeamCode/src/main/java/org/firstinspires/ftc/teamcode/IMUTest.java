package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Josie Miner on 2/9/2018.
 */
@Autonomous(name="Preciousss: IMUTest", group="Preciousss")

public class IMUTest extends superAuto {

    public void runOpMode() {
        setUp();
        readIMU();
        Wait(2);
        readIMU();
        Wait(2);
        readIMU();
        Wait(2);
        readIMU();
        Wait(2);
        readIMU();
        Wait(2);
        readIMU();
        Wait(2);

    }
}