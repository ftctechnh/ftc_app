package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by butterss21317 on 9/19/2017.
 */

public class TestBotHardware {
public ColorSensor colorSensor = null;
    public void init (HardwareMap hwMap){
        colorSensor = hwMap.colorSensor.get("color_sensor");
    }
}

