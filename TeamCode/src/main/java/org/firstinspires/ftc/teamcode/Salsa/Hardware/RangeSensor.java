package org.firstinspires.ftc.teamcode.Salsa.Hardware;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by adityamavalankar on 11/5/18.
 */

public class RangeSensor {

    public DistanceSensor rs;
    public HardwareMap hwmap;

    public void init(String hardwareName, HardwareMap inputMap) {

        hwmap = inputMap;

        rs = hwmap.get(DistanceSensor.class, hardwareName);
    }



}
