package org.firstinspires.ftc.teamcode.Salsa.Hardware;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorREV2mDistance;

/**
 * Created by adityamavalankar on 11/5/18.
 */

public class RangeSensor2m {

    public SensorREV2mDistance rs2m;
    public HardwareMap hwmap;

    public void init(String hardwareName, HardwareMap inputMap) {

        hwmap = inputMap;

        rs2m = hwmap.get(SensorREV2mDistance.class, hardwareName);
    }



}
