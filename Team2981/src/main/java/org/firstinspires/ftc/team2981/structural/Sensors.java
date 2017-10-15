package org.firstinspires.ftc.team2981.structural;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by 200462069 on 10/14/2017.
 */

public class Sensors {

    private ModernRoboticsI2cGyro gyro = null;
    private HardwareMap map = null;

    public Sensors(HardwareMap map){
        this.map = map;
        gyro = (ModernRoboticsI2cGyro) map.gyroSensor.get("gyro");
    }

    public void calibrate(){
        gyro.calibrate();
    }

    public int getGyroHeading(){
        return gyro.getHeading();
    }


}
