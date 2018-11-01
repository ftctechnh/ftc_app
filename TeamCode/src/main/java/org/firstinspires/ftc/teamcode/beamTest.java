package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Recharged Orange on 10/25/2018.
 */

@TeleOp (name = "beam Test")

public class beamTest extends superClass {
public void runOpMode(){

 initialization(false);

 waitForStart();

 while (opModeIsActive()){

     if (beam.getState()){
         sweeper.setPower(1);
     }
     else sweeper.setPower(0);
 }

}




}
