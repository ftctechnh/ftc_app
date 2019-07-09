package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class ExampleHardwareMap {
    //Say what exists
    public DcMotor  Andy   = null;
    public Servo    Seral  = null;
    public CRServo CRSeral = null;

    //Random Garbage
    HardwareMap hwMap =  null;
    private ElapsedTime period  = new ElapsedTime();
    public ExampleHardwareMap(){}
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        //Name them
        Andy  = hwMap.get(DcMotor.class, "Andy");
        Andy.setDirection(DcMotor.Direction.FORWARD);

        // Set to 0
        Andy.setPower(0);

        // encoder?
        Andy.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        // Name Servos and set to 0
        Seral = hwMap.get(Servo.class, "Seral");
        Seral.setPosition(0);
        CRSeral = hwMap.get(CRServo.class, "CRSeral");
        CRSeral.setPower(0);
    }
}