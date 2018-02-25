package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by GJF on 1/28/2018.
 */
@Autonomous(name="Preciousss: AutoBlue1", group="Preciousss")

public class autoBlue1 extends superAuto {

    public void runOpMode() {

        iAmRed = false;
        iAmBlue = true;

        configureGyro();

        mapHardware();

        composeTelemetry();

        waitForStart();

        move(0f, 0.5f, .5f);

        Wait(.5);

       // pivotRight(.7f);

        Wait(1);

        move(0f,-.25f,.35f);

        Wait(1);

       // Conveyor(3f);

        Wait(1);

        move(0f,-.25f,.5f);

        Wait(1);

        move(0f,.25f,.25f);
    }
}