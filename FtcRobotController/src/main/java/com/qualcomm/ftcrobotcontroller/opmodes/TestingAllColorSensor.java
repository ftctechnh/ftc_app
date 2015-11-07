package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * Created by sam on 26-Oct-15.
 */
public class TestingAllColorSensor extends OpMode {
    public DcMotor FL;
    public DcMotor FR;
    public DcMotor OtherMotor;
    public DcMotor BL;
    public DcMotor BR;
    public ServoController sr;
    public Servo Lbump;
    public Servo Rbump;
    public ColorSensor CL;

    public void init() {
        BL = hardwareMap.dcMotor.get("m1");
        FL = hardwareMap.dcMotor.get("m2");
        BR = hardwareMap.dcMotor.get("m3");
        FR = hardwareMap.dcMotor.get("m4");
        OtherMotor = hardwareMap.dcMotor.get("m5");
        Rbump = hardwareMap.servo.get("s1");
        Lbump = hardwareMap.servo.get("s2");
        CL = hardwareMap.colorSensor.get("Color");
    }

    public void loop() {
        if (gamepad1.a)
            CL.enableLed(false);
        if (gamepad1.b)
            CL.enableLed(true);
        telemetry.addData("Red", CL.red());
        telemetry.addData("Green", CL.green());
        telemetry.addData("Blue", CL.blue());
    }
}