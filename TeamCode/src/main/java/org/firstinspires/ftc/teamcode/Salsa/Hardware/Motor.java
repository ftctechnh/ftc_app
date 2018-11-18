package org.firstinspires.ftc.teamcode.Salsa.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by adityamavalankar on 11/5/18.
 */

public abstract class Motor implements DcMotor {

    public HardwareMap hwmap;
    private DcMotor dcm;

    public void init(String hardwareName, HardwareMap inputMap) {

        hwmap = inputMap;

        this.dcm = hwmap.get(DcMotor.class, hardwareName);
    }

}
