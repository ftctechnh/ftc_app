package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.bamboo.Root;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.LightSensor;

/**
 * Created by chsrobotics on 12/7/2015.
 */
public class UnitTests extends Root {

    public LightSensor lRight;
    public LightSensor lLeft;
    public GyroSensor gyro;

    public UnitTests()
    {
        lRight = hardwareMap.lightSensor.get("lightRight");
        lLeft = hardwareMap.lightSensor.get("lightLeft");
        gyro = hardwareMap.gyroSensor.get("gyro");

        lRight.enableLed(true);
        lLeft.enableLed(true);
    }

    @Override
    public void loop()
    {
        console.log("ls", "("+lRight.getLightDetected()+", "+lLeft.getLightDetected()+")");
        console.log("gy", gyro.getRotation()+"");
    }
}
