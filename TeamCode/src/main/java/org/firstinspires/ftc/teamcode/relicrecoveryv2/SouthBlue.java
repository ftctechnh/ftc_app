package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Nora on 1/19/2018.
 */

//@Autonomous(name="SouthBlue", group="AutoMode")
public class SouthBlue extends RelicAutoMode {
    @Override
    public void runOpMode() {

        waitForStartify();

        //Knocking the jewel over
        pengwinFin.moveFinDown();

        pengwinFin.moveFinUp();

        pengwinFin.moveFinSense();

        //Back up until vuforia but how then I sat and stared at code for countless hours because I did not know where it was. I got really confused so we are just going to skip this for now
        //Future Feature
        //Basically everything has to do with vuforia and the range sensor
        if(0>0){
       //Yay
        }

        int i = 0;
        int max = 5;

        while (i < max) {
            i++;
        }

    }
}
