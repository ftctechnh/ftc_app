package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by akhil on 9/19/2015.
 */
public class OmniBotUrOp extends OpMode{

    @Override
    public void init() {
    }

    UltrasonicSensor ultrasonicSensor;
    @Override
    public void start(){
        
        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("ursensor") ;
        }


    @Override
    public void loop() {
    telemetry.addData("ursensor","ursensor is" +String.format("%d","ursensor"));
    }
}
