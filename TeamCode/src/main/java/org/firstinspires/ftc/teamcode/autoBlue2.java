package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by GJF on 1/28/2018.
 */
@Autonomous(name="Preciousss: AutoRed1", group="Preciousss")

public class autoBlue2 extends superAuto {

    public void runOpMode() {

        iAmRed = false;
        iAmBlue = true;

        configureGyro();

        mapHardware();

        composeTelemetry();

        waitForStart();

        move(0f, 0.5f, .23f);

        Wait(1);

        move(-.7f,0f,1f);

        Wait(1);

        pivotRight(1.735f);

        Wait(1);

        move(0f,-.25f,.2f);

        Wait(1);

        Conveyor(3f);

        Wait(1);

        move(0f,.25f,.35f);

        Wait(1);

        move(0f,-.25f,.5f);
    }
}