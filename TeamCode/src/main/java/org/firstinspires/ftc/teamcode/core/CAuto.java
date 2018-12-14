package org.firstinspires.ftc.teamcode.core;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous (name = "Crater", group = "Nessie")

public class CAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        int classification = 1;
        Functions.OffLander(0.3);
        //scan for number 1-3 from jewels
        switch(classification){
            case 1:
                Functions.turn(-30,1);
                Functions.move(33,1);
                Functions.move(-9,1);
                Functions.turn(-30,1);
                Functions.move(43,1);
                Functions.turn(-30,1);
                Functions.move(35,1);
                Functions.turn(180,1);
                break;
            case 2:
                Functions.move(24,1);
                Functions.move(-6,1);
                Functions.turn(-5,1);
                Functions.move(-50,1);
                Functions.turn(-45,1);
                Functions.move(38,1);
                Functions.turn(180,1);
                break;
            case 3:
                Functions.move(28,1);
                Functions.move(-9,1);
                Functions.turn(-20,1);
                Functions.move(57,1);
                Functions.turn(-35,1);
                Functions.move(38,1);
                Functions.turn(180,1);
                break;
        }
        Functions.move(-30,1);
        Functions.PlaceMarker();
        Functions.move(90,1);
    }
}