package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class chassis {
    HardwareMap map;
    DcMotor FR;
    DcMotor BR;
    DcMotor FL;
    DcMotor BL;

    public void init(HardwareMap hwmap) {
        map = hwmap;
        FR = map.dcMotor.get("FR");
        BR = map.dcMotor.get("BR");
        FL = map.dcMotor.get("FL");
        BL = map.dcMotor.get("BL");
    }

    public void move(double fr, double br, double fl, double bl) {
        FR.setPower(fr);
        BR.setPower(br);
        FL.setPower(fl);
        BL.setPower(bl);
    }

    public void forward(double power) {
        move(power, power, power, power);
    }

    public void turn(double power) {
        move(-power, power, -power, power);
    }
}
