package org.firstinspires.ftc.teamcode.core;

import com.disnodeteam.dogecv.detectors.roverrukus.SamplingOrderDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous (name = "DropZone", group = "Nessie")

public class DZAuto extends LinearOpMode {

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
        //scan for number 1-3 from jewels (We need a vision system)
        TryAgain();

        Func.move(-44,1);
        Func.PlaceMarker();
        Func.move(90, 1);
    }

    private void TryAgain()
    {

        SamplingOrderDetector.GoldLocation location = RA.getCurrentOrder();

        switch(location){
            case LEFT:
                Func.turn(-30,1);
                Func.move(33,1);
                Func.turn(30,1);
                Func.move(18,1);
                Func.move(-21,1);
                Func.turn(45,1);
                Func.move(78,1);
                Func.turn(45,1);
                Func.move(45,1);
                break;
            case CENTER:
                Func.move(39,1);
                Func.move(-20,1);
                Func.turn(90,1);
                Func.move(39,1);
                Func.turn(60,1);
                Func.move(57,1);
                break;
            case RIGHT:
                Func.turn(30,1);
                Func.move(-39,1);
                Func.turn(-60,1);
                Func.move(9,1);
                Func.turn(180,1);
                Func.move(96,1);
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