package org.firstinspires.ftc.teamcode;
import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;



public class NewHardware {
    public DcMotor leftDrive;
    public DcMotor rightDrive;
    public DcMotor liftBot;
    public DcMotor liftBot2;
    public DcMotor mineralCollect;
    public DcMotor mineralArm;
    //public NormalizedColorSensor sensor;
    public Servo totemServo;
    public DcMotor spin;
    public Servo craterArm;

    HardwareMap hwMap;

    public void Initialize(HardwareMap hwMap)
    {
        this.hwMap = hwMap;

        leftDrive = hwMap.get(DcMotor.class,"leftDrive");
        rightDrive = hwMap.get(DcMotor.class,"rightDrive");
        liftBot = hwMap.get(DcMotor.class,"liftBot");
        liftBot2 = hwMap.get(DcMotor.class,"liftBot2");
        mineralCollect = hwMap.get(DcMotor.class,"mineralCollect");
        mineralArm = hwMap.get(DcMotor.class,"mineralArm");
        spin = hwMap.get(DcMotor.class,"spin");
        craterArm = hwMap.get(Servo.class,"craterArm");
        //sensor = hwMap.get(NormalizedColorSensor.class,"sensor");
        /*leftDrive.setPower(0);
        rightDrive.setPower(0);
        liftBot.setPower(0);
        liftBot2.setPower(0);*/
        //mineralCollect.setPower(0);
        totemServo = hwMap.get(Servo.class,"totemServo");


        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        liftBot.setDirection(DcMotorSimple.Direction.FORWARD);
        liftBot2.setDirection(DcMotorSimple.Direction.FORWARD);
    }
}



