package org.firstinspires.ftc.teamcode;
import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class NewHardware {
    public DcMotor leftDrive;
    public DcMotor rightDrive;
    public DcMotor liftBot;
    public DcMotor liftBot2;
    //public Servo latch;
    //public ColorSensor sensor;

    HardwareMap hwMap;

    public void initialize(HardwareMap hwMap)
    {
        this.hwMap = hwMap;

        leftDrive = hwMap.get(DcMotor.class,"leftDrive");
        rightDrive = hwMap.get(DcMotor.class,"rightDrive");
        liftBot = hwMap.get(DcMotor.class,"liftBot");
        liftBot2 = hwMap.get(DcMotor.class,"liftBot2");
        //latch = hwMap.get(Servo.class,"latch");
       // sensor = hwMap.get(ColorSensor.class,"sensor");*/
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        liftBot.setPower(0);
        liftBot2.setPower(0);
        leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
    }
}



