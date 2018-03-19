package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.ObjectOriented;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

enum colorState {
    RED,
    BLUE,
    OTHER;
}

public class REV_ColorSensor {

    public ColorSensor colorSensor;

    /*Default constructor*/
    public REV_ColorSensor(ColorSensor colorSensor) {
        this.colorSensor = colorSensor;
    }

    /*public void initiate(String hardwareConfig){
        colorSensor = hardwareMap.get(ColorSensor.class, hardwareConfig);
    }*/

    public colorState getValue() {
        if (colorSensor.red() > colorSensor.blue()) {return colorState.RED;}
        else if (colorSensor.red() > colorSensor.blue()) {return colorState.BLUE;}
        else {return colorState.OTHER;}
    }
}
