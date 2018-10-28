package org.firstinspires.ftc.teamcode.Avocado;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class hardwareprofileHdrive {

    public DcMotor topLeftMotor = null;
    public DcMotor topRightMotor = null;
    public DcMotor middleMotor = null;

    HardwareMap hwmap = null;

    public void init(HardwareMap ahwmap) {
        // Save reference to Hardware map
        hwmap = ahwmap;

        // Hardware map

        //Drivetrain
        topLeftMotor = hwmap.get(DcMotor.class, "dttopleft");
        topRightMotor = hwmap.get(DcMotor.class, "dttopright");
        middleMotor = hwmap.get(DcMotor.class, "dtmiddle");

    }



}

