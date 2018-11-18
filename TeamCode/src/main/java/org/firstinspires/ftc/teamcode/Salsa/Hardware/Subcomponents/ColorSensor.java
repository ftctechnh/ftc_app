package org.firstinspires.ftc.teamcode.Salsa.Hardware.Subcomponents;

import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by adityamavalankar on 11/5/18.
 */

public abstract class ColorSensor implements com.qualcomm.robotcore.hardware.ColorSensor {

    /** This is a slightly modified version of the {com.qualcomm.robotcore.hardware.ColorSensor} class
     *  It is meant to interface better with the object oriented nature of the Team Salsa code
     *  In this case, all of the functions are inherited from the {com.qualcomm.robotcore.hardware.ColorSensor} class
     *  The init() function here is meant to HardwareMap() more effectively, without much extra work
     */

    private com.qualcomm.robotcore.hardware.ColorSensor cs;
    public HardwareMap hwmap;

    public void init(String hardwareName, HardwareMap inputMap) {

        hwmap = inputMap;

        this.cs = hwmap.get(com.qualcomm.robotcore.hardware.ColorSensor.class, hardwareName);
    }



}
