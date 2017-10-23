package org.firstinspires.ftc.team2981.structural;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by 200462069 on 10/14/2017.
 */

public class Sensors {

    private ModernRoboticsI2cGyro gyro = null;
    private ModernRoboticsI2cColorSensor color = null;
    private HardwareMap map = null;

    public Sensors(HardwareMap map){
        this.map = map;
    }

    public void init(){
        gyro = (ModernRoboticsI2cGyro) map.gyroSensor.get("gyro");
        color = (ModernRoboticsI2cColorSensor) map.colorSensor.get("color");
    }

    public void calibrate(){
        gyro.calibrate();
    }

    public int getGyroHeading(){
        return gyro.getHeading();
    }

    public int[] getRGB(){
        return new int[] {color.red(), color.blue(), color.green()};
    }


}
