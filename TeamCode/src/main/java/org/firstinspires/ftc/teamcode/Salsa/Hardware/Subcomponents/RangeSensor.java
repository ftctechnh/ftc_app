package org.firstinspires.ftc.teamcode.Salsa.Hardware.Subcomponents;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by adityamavalankar on 11/5/18.
 */

public abstract class RangeSensor implements DistanceSensor {

    /** This is a slightly modified version of the {com.qualcomm.robotcore.hardware.DistanceSensor} class
     *  It is meant to interface better with the object oriented nature of the Team Salsa code
     *  In this case, all of the functions are inherited from the {com.qualcomm.robotcore.hardware.DistanceSensor} class
     *  The init() function here is meant to HardwareMap() more effectively, without much extra work
     */

    private DistanceSensor rs;
    public HardwareMap hwmap;

    public void init(String hardwareName, HardwareMap inputMap) {

        hwmap = inputMap;

        rs = hwmap.get(DistanceSensor.class, hardwareName);
    }



}
