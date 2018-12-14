package org.firstinspires.ftc.teamcode.core;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous (name = "DropZone", group = "Nessie")

public class DZAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        int classification = 1;
        Functions.OffLander(.5);
        //scan for number 1-3 from jewels (We need a vision system, Ethan has one)
        //These cases need to be tested
        //Once hardware is fixed, this will need some changes
        switch(classification){
            case 1:
                Functions.turn(-30,1);
                Functions.move(33,1);
                Functions.turn(30,1);
                Functions.move(18,1);
                Functions.move(-21,1);
                Functions.turn(45,1);
                Functions.move(78,1);
                Functions.turn(45,1);
                Functions.move(45,1);
                break;
            case 2:
                Functions.move(39,1);
                Functions.move(-20,1);
                Functions.turn(90,1);
                Functions.move(39,1);
                Functions.turn(60,1);
                Functions.move(57,1);
                break;
            case 3:
                Functions.turn(30,1);
                Functions.move(-39,1);
                Functions.turn(-60,1);
                Functions.move(9,1);
                Functions.turn(180,1);
                Functions.move(96,1);
                break;
        }
        Functions.move(-44,1);
        Functions.PlaceMarker();
        Functions.move(90, 1);
    }
}