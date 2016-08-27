package com.qualcomm.ftcrobotcontroller.opmodes.FreshClasses;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Naisan on 4/16/2016.
 */
public class FreshSensors {
     TouchSensor T_Lift;
     ColorSensor colorSensorLeft,
            colorSensorRight;
     OpticalDistanceSensor distanceSensor;
     IrSeekerSensor irSensor;

        public FreshSensors(TouchSensor T_Lift, ColorSensor colorSensorLeft, ColorSensor colorSensorRight, OpticalDistanceSensor distanceSensor,IrSeekerSensor irSensor) {
            this.T_Lift = T_Lift;
            this.colorSensorLeft = colorSensorLeft;
            this.colorSensorRight = colorSensorRight;
            this.distanceSensor = distanceSensor;
            this.irSensor = irSensor;
        }
}
