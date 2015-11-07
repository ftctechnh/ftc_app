package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by sam on 31-Oct-15.
 */
public class EncoderTest extends OpMode {
    public DcMotor FL;
    public DcMotor FR;
    public DcMotor OtherMotor;
    public DcMotor BL;
    public DcMotor BR;
    public ServoController sr;
    public Servo Lbump;
    public Servo Rbump;
    DcMotorController dc = OtherMotor.getController();

    @Override
    public void init() {
        BL = hardwareMap.dcMotor.get("m1");
        FL = hardwareMap.dcMotor.get("m2");
        BR = hardwareMap.dcMotor.get("m3");
        FR = hardwareMap.dcMotor.get("m4");
        OtherMotor = hardwareMap.dcMotor.get("m5");
        Rbump = hardwareMap.servo.get("s1");
        Lbump = hardwareMap.servo.get("s2");
    }

    @Override
    public void loop() {
       // OtherMotor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        long encoderRead = OtherMotor.getCurrentPosition();
        double throttle = gamepad1.right_stick_y;
        Range.clip(throttle, -1, 1);
        OtherMotor.setPower(throttle);
        telemetry.addData("Encoder", encoderRead);
    }
}
