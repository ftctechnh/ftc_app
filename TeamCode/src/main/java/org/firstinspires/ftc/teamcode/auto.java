package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by Josie on 10/5/2018.
 */
@Autonomous(name="Preciousss: auto", group="Preciousss")

public class auto extends superAuto {

    public void runOpMode() {
        setUp();
        configVuforiaRoverRuckus();
        getQuadrant();


        //ALL AUTOS DO:
        //Drop down
        //Go straight
        //Knock correct mineral
        //Take left
        //else Stuff

        if(quadrantOdd) {
            //Turn 270 degrees
            // move close, not a long time
        }


        else {
            //turn 15 degrees
            // move far, very long time
        }
        //ALL AUTOS DO:
        //Deposit Claimer
        //Move backwards full speed into crater



    }
}