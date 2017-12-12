package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Mahim on 12/4/2017.
 */

public class MecanumDriveSystem {
    private DcMotor frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor;
    private HardwareMap hardwareMap;
    private Gamepad gamepad;

    public MecanumDriveSystem (HardwareMap hardwareMap, Gamepad gamepad) {
        this.hardwareMap = hardwareMap;
        this.gamepad = gamepad;
        this.frontLeftMotor = hardwareMap.get(DcMotor.class,"front left motor");
        this.rearLeftMotor = hardwareMap.get(DcMotor.class,"rear left motor");
        this.frontRightMotor = hardwareMap.get(DcMotor.class,"front right motor");
        this.rearRightMotor = hardwareMap.get(DcMotor.class, "rear right motor");
    }


}
