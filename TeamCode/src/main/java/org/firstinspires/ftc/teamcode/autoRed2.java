package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Josie on 2/23/2018.
 */
@Autonomous(name="Preciousss: AutoRed1", group="Preciousss")

public class autoRed2 extends superAuto {

    public void runOpMode() {

        iAmRed = true;

        configureGyro();

        mapHardware();

        composeTelemetry();

        waitForStart();

        move(0f, -0.5f, .35f);

        Wait(1);

        move(-.75f,0f,1.0f);

        Wait(1);

        Conveyor(3f);

        Wait(1);

        move(0f,-.25f,.5f);

        Wait(1);

        move(0f,.25f,.25f);
    }
}