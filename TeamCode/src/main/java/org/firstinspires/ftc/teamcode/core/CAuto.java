package org.firstinspires.ftc.teamcode.core;

import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous (name = "Crater", group = "Nessie")

public class CAuto extends LinearOpMode {

    Functions Func;
    ResourceAPI RA;
    int tryCount;

    @Override
    public void runOpMode() throws InterruptedException {

        Func = new Functions(telemetry, hardwareMap, this);
        RA = new ResourceAPI();
        tryCount = 0;

        RA.enableDetection();
        //come off of lander
        //scan for number 1-3 from jewels
        TryAgain();

        Func.move(-30, 1);
        Func.PlaceMarker();
        Func.move(90, 1);
    }

    private void TryAgain()
    {

        SamplingOrderDetector.GoldLocation location = RA.getCurrentOrder();

        switch (location) {
            case LEFT:
                Func.turn(-30, 1);
                Func.move(33, 1);
                Func.move(-9, 1);
                Func.turn(-30, 1);
                Func.move(43, 1);
                Func.turn(-30, 1);
                Func.move(35, 1);
                Func.turn(180, 1);
                break;
            case CENTER:
                Func.move(24, 1);
                Func.move(-6, 1);
                Func.turn(-5, 1);
                Func.move(-50, 1);
                Func.turn(-45, 1);
                Func.move(38, 1);
                Func.turn(180, 1);
                break;
            case RIGHT:
                Func.move(28, 1);
                Func.move(-9, 1);
                Func.turn(-20, 1);
                Func.move(57, 1);
                Func.turn(-35, 1);
                Func.move(38, 1);
                Func.turn(180, 1);
                break;

            case UNKNOWN:

                if(tryCount > 3)
                    return;

                tryCount++;

                sleep(667);

                TryAgain();

                break;
        }
    }
}

