package com.qualcomm.ftcrobotcontroller.bamboo;

import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by alex on 12/21/15.
 */
public class Gyro {

    GyroSensor _gyro;

    public Gyro(String name, HardwareMap hwm)
    {
        _gyro = hwm.gyroSensor.get(name);
    }

    public double dps()
    {
        if(_gyro.getRotation() != 590 && _gyro.getRotation() != 591)
            return ((_gyro.getRotation()-590)/(1000));
        return 0;
    }

    public double rotation()
    {
        return _gyro.getRotation();
    }
}
