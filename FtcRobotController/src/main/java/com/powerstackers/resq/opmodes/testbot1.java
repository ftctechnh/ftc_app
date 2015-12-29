package com.powerstackers.resq.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.swerverobotics.library.interfaces.TeleOp;

/**
 *
 * @author Derek Helm
 *
 */
@TeleOp
public class testbot1 extends OpMode{

    DcMotor mototBrush;
    DcMotor motorLift;
    DcMotor motorFRight;
    DcMotor motorFLeft;
    DcMotor motorBRight;
    DcMotor motorBLeft;


    @Override
    public void init() {

        mototBrush = hardwareMap.dcMotor.get("motorBrush");
        motorLift = hardwareMap.dcMotor.get("motorLift");
    }

    @Override
    public void loop() {

    }
}
