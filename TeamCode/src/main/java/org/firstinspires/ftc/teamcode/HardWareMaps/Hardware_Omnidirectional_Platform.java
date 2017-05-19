package org.firstinspires.ftc.teamcode.HardWareMaps;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Hardware_Omnidirectional_Platform {
    public DcMotor Base_FL = null;
    public DcMotor Base_FR = null;
    public DcMotor Base_BL = null;
    public DcMotor Base_BR = null;

    //public Servo LightClick_A = null;
    // public Servo LightClick_B = null;

    public void init(HardwareMap hardwareMap){
        Base_BL = hardwareMap.dcMotor.get("Base_BL");
        Base_BR = hardwareMap.dcMotor.get("Base_BR");

        Base_FL = hardwareMap.dcMotor.get("Base_FL");
        Base_FR = hardwareMap.dcMotor.get("Base_FR");

        // LightClick_A = hardwareMap.servo.get("LightClick_A");
        // LightClick_B = hardwareMap.servo.get("LightClick_B");

        Base_BR.setPower(0.00);
        Base_BL.setPower(0.00);

        Base_FR.setPower(0.00);
        Base_FL.setPower(0.00);

        Base_BR.setDirection(DcMotor.Direction.REVERSE);
        Base_FR.setDirection(DcMotor.Direction.REVERSE);

        // LightClick_A.setPosition(0.00);
        // LightClick_B.setPosition(0.00);
    }
}
