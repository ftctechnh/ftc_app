package org.firstinspires.ftc.teamcode.core;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous (name = "Crater", group = "Nessie")

public class CAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Functions Func = new Functions();

        int classification = 1;


        //come off of lander
        //scan for number 1-3 from jewels
        switch(classification){
            case 1:
                Func.turn(-30,1);
                Func.move(33,1);
                Func.move(-9,1);
                Func.turn(-30,1);
                Func.move(43,1);
                Func.turn(-30,1);
                Func.move(35,1);
                Func.turn(180,1);
                break;
            case 2:
                Func.move(24,1);
                Func.move(-6,1);
                Func.turn(-5,1);
                Func.move(-50,1);
                Func.turn(-45,1);
                Func.move(38,1);
                Func.turn(180,1);
                break;
            case 3:
                Func.move(28,1);
                Func.move(-9,1);
                Func.turn(-20,1);
                Func.move(57,1);
                Func.turn(-35,1);
                Func.move(38,1);
                Func.turn(180,1);
                break;
        }
        Func.move(-30,1);
        Func.PlaceMarker();
        Func.move(90,1);
    }
}