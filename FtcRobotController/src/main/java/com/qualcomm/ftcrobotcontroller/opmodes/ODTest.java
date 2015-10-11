package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * This is for testing the Optical Distance Sensor from Modern Robotics. Black is 0, white is 1.
 */
public class ODTest extends OpMode{

    OpticalDistanceSensor opticalDistanceSensor;

    @Override
    public void init (){
        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");
    }

    @Override
    public void loop (){
        double reflectance = opticalDistanceSensor.getLightDetected();
        telemetry.addData("Reflectance Value", reflectance);

    }


}
