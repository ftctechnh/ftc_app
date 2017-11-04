package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 11/3/2017.
 */

public class ColorSensor9330 {
    Hardware9330 hwMap;
    ColorSensor cs;
    boolean bledOn = true;

    public ColorSensor9330(Hardware9330 robotMap){
        hwMap = robotMap;
        cs = hwMap.armCS;
        cs.enableLed(bledOn);
    }


    public float r() {
        return cs.red();
    }

    public float g() {

        return cs.green();
    }

    public float b() {

        return cs.blue();
    }

}
