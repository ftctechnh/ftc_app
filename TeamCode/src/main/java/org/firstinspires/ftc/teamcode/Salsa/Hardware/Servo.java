package org.firstinspires.ftc.teamcode.Salsa.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by adityamavalankar on 11/5/18.
 */

public class Servo {

    public com.qualcomm.robotcore.hardware.Servo s;
    public HardwareMap hwmap;

    public void init(String hardwareName, HardwareMap inputMap) {

        hwmap = inputMap;

        s = hwmap.get(com.qualcomm.robotcore.hardware.Servo.class, hardwareName);
    }

}
