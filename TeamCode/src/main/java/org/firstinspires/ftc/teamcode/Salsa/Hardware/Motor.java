package org.firstinspires.ftc.teamcode.Salsa.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by adityamavalankar on 11/5/18.
 */

public class Motor {

    public DcMotor m;
    HardwareMap hwmap;

    public void init(String hardwareName, HardwareMap inputMap) {

        hwmap = inputMap;

        m = hwmap.get(DcMotor.class, hardwareName);
    }

}
