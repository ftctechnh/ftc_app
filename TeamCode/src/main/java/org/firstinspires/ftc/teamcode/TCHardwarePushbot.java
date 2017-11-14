/*
 Created by Nightmaze on 10/17/17.
 */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//Defines motors and servos
public class TCHardwarePushbot {
    public DcMotor rDrive;
    public DcMotor lDrive;
    public DcMotor cDrive;
    public DcMotor fLift;

    public DcMotor arm1;
    public DcMotor arm2;
    public Servo fs1;
    public Servo fs2;
    public Servo fs3;
    public Servo fs4;
    public Servo jko;
    public Servo claw;

    //Creates the hardware map
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();


    //Maps the hardware to ahwMap
    public void init(HardwareMap hwMap) {
        // Save reference to Hardware map

        // Define and Initialize Motors
        rDrive = hwMap.dcMotor.get("rDrive");
        lDrive = hwMap.dcMotor.get("lDrive");
        cDrive = hwMap.dcMotor.get("cDrive");


        fLift = hwMap.dcMotor.get("fLift");
        arm1 = hwMap.dcMotor.get("arm1");
        arm2 = hwMap.dcMotor.get("arm2");
        fs1 = hwMap.servo.get("fs1");
        fs2 = hwMap.servo.get("fs2");
        fs3 = hwMap.servo.get("fs3");
        fs4 = hwMap.servo.get("fs4");
        jko = hwMap.servo.get("jko");
        claw = hwMap.servo.get("claw");
        lDrive.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rDrive.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        arm1.setDirection(DcMotorSimple.Direction.REVERSE);
        // Set all motors to zero power to get ready for start
        rDrive.setPower(0);
        lDrive.setPower(0);
        cDrive.setPower(0);
       /* fLift.setPower(0);
        arm1.setPower(0);
        arm2.setPower(0);*/

    }


}
