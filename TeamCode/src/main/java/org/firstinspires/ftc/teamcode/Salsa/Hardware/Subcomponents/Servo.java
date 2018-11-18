package org.firstinspires.ftc.teamcode.Salsa.Hardware.Subcomponents;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by adityamavalankar on 11/5/18.
 */

public abstract class Servo implements com.qualcomm.robotcore.hardware.Servo {

    /** This is a slightly modified version of the {com.qualcomm.robotcore.hardware.Servo} class
     *  It is meant to interface better with the object oriented nature of the Team Salsa code
     *  In this case, all of the functions are inherited from the {com.qualcomm.robotcore.hardware.Servo} class
     *  The init() function here is meant to HardwareMap() more effectively, without much extra work
     */

    private com.qualcomm.robotcore.hardware.Servo s;
    public HardwareMap hwmap;

    public void init(String hardwareName, HardwareMap inputMap) {

        hwmap = inputMap;

        s = hwmap.get(com.qualcomm.robotcore.hardware.Servo.class, hardwareName);
    }

}
