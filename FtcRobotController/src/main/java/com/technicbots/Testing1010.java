package com.technicbots;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Jerry on 10/10/2015.
 */
public class Testing1010 extends OpMode{
    DcMotor DC_left;
    DcMotor DC_right;
    DcMotor Omni_left;
    DcMotor Omni_right;

    @Override
    public void init () {
        DC_left = hardwareMap.dcMotor.get("DC_left");
        DC_right = hardwareMap.dcMotor.get("DC_right");
        Omni_left = hardwareMap.dcMotor.get("Omni_left");
        Omni_right = hardwareMap.dcMotor.get("Omni_right");
    }

    @Override
    public void loop () {
        DC_left.setPower(0.5);
        DC_right.setPower(0.5);
        Omni_left.setPower(0.5);
        Omni_right.setPower(0.5);
    }

}
