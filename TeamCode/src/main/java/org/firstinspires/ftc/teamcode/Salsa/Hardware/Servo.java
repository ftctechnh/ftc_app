package org.firstinspires.ftc.teamcode.Salsa.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by adityamavalankar on 11/5/18.
 */

public class Servo {

    public Servo s;
    HardwareMap hwmap;

    public void init(String hardwareName, HardwareMap inputMap) {

        hwmap = inputMap;

        s = hwmap.get(Servo.class, hardwareName);
    }



}
