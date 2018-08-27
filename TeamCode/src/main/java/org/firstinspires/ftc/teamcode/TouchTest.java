package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;

public class TouchTest extends LinearOpMode {

    DigitalChannel digitaltouch;

    @Override
    public void runOpMode() {

        digitaltouch = hardwareMap.get(DigitalChannel.class,    "Touch");
        digitaltouch.setMode(DigitalChannel.Mode.INPUT);

     runOpMode();
     while (opModeIsActive()){

         if (digitaltouch.getState() == true){
             telemetry.addData("DigitalTouch",  "Is Pressed Mans");

         }else {
             telemetry.addData("Digital Touch",     "No Pressed MANZ");
         }

     }

    }
}
