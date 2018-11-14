package org.firstinspires.ftc.teamcode.Avocado.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class hardwaremap_TeleOpMecanum {

    // Gamepad 1
    public DcMotor topLeftMotor;
    public DcMotor bottomLeftMotor;
    public DcMotor topRightMotor;
    public DcMotor bottomRightMotor;

    //Gamepad 2
    public DcMotor hanger;
    public DcMotor hanger2;
    public DcMotor claw;
    public DcMotor tiltMotor;

    HardwareMap hwmap = null;

    public void init(HardwareMap ahwmap) {
        // Save reference to Hardware map
        hwmap = ahwmap;

        // Hardware map

        topLeftMotor = hwmap.dcMotor.get("topLeftMotor");
        bottomLeftMotor = hwmap.dcMotor.get("bottomLeftMotor");
        topRightMotor = hwmap.dcMotor.get("topRightMotor");
        bottomRightMotor = hwmap.dcMotor.get("bottomRightMotor");


    }



}