package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by alex on 11/10/15.
 */
public class AUTOOPPP extends OpMode {

    public DcMotor right, left;


    @Override
    public void init()
    {
        right = hardwareMap.dcMotor.get("right");
        left = hardwareMap.dcMotor.get("left");
        left.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop()
    {
        left.setPower(1);
        right.setPower(1);
    }
}
