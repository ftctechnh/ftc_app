package org.firstinspires.ftc.teamcode.Salsa.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by adityamavalankar on 11/5/18.
 */

public class CRServo {

    public com.qualcomm.robotcore.hardware.CRServo crs;
    HardwareMap hwmap;

    public void init(String hardwareName, HardwareMap inputMap) {

        hwmap = inputMap;

        crs = hwmap.get(com.qualcomm.robotcore.hardware.CRServo.class, hardwareName);
    }



}
