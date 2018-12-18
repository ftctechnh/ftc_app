package org.firstinspires.ftc.teamcode.core;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous (name = "DropZone", group = "Nessie")

public class DZAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Functions Func = new Functions();

        int classification = 1;


        //come off of lander
        //scan for number 1-3 from jewels (We need a vision system)
        switch(classification){
            case 1:
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
            case 2:
                Func.move(39,1);
                Func.move(-20,1);
                Func.turn(90,1);
                Func.move(39,1);
                Func.turn(60,1);
                Func.move(57,1);
                break;
            case 3:
                Func.turn(30,1);
                Func.move(-39,1);
                Func.turn(-60,1);
                Func.move(9,1);
                Func.turn(180,1);
                Func.move(96,1);
                break;
        }
        Func.move(-44,1);
        Func.PlaceMarker();
        Func.move(90, 1);
    }
}