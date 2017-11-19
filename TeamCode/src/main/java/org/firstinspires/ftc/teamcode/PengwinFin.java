package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by thund on 11/11/2017.
 */

public class PengwinFin {
    static final double FIN_UP_POSITION = .7;
    static final double FIN_DOWN_POSITION = 0.1;
    Servo fin;
    ColorSensor colorSensor;

    public PengwinFin(HardwareMap hardwareMap){
        fin = hardwareMap.servo.get("fin");
        colorSensor = hardwareMap.colorSensor.get("sitefy");
    }

    public void moveFinDown(){
            fin.setPosition(FIN_DOWN_POSITION);
    }

    public void moveFinUp(){
            fin.setPosition(FIN_UP_POSITION);
    }


    public boolean doesColorSensorSeeBlueJewel(){
        return colorSensor.blue() > colorSensor.red();
    }
}

