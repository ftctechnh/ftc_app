package org.firstinspires.ftc.team2993.structural;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Sensors
{

    private ModernRoboticsI2cColorSensor color;
    private HardwareMap map;

    public Sensors(HardwareMap map){
        this.map = map;
    }

    public void init()
    {
        color = (ModernRoboticsI2cColorSensor) map.colorSensor.get("color");
    }

    public int getRGB(){
        return color.red();
    }


}
