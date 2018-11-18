package org.firstinspires.ftc.teamcode.Salsa.Hardware.Subcomponents;

import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by adityamavalankar on 11/5/18.
 */

public abstract class ColorSensor implements com.qualcomm.robotcore.hardware.ColorSensor {

    private com.qualcomm.robotcore.hardware.ColorSensor cs;
    public HardwareMap hwmap;

    public void init(String hardwareName, HardwareMap inputMap) {

        hwmap = inputMap;

        this.cs = hwmap.get(com.qualcomm.robotcore.hardware.ColorSensor.class, hardwareName);
    }



}
