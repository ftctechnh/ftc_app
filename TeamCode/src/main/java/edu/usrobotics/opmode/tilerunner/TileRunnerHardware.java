package edu.usrobotics.opmode.tilerunner;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;

import edu.usrobotics.opmode.BaseHardware;


/**
 * Created by mborsch19 & dsiegler19 on 10/13/16.
 */
public class TileRunnerHardware extends BaseHardware {

    public DcMotor leftMotor1;
    public DcMotor leftMotor2;
    public DcMotor rightMotor1;
    public DcMotor rightMotor2;

    public DcMotor gripperLift;

    public Servo ballKnocker;

    public Servo gripperLeft;
    public Servo gripperRight;

    @Override
    public void getDevices() {

        leftMotor1 = hardwareMap.dcMotor.get("l1");
        leftMotor2 = hardwareMap.dcMotor.get("l2");
        rightMotor1 = hardwareMap.dcMotor.get("r1");
        rightMotor2 = hardwareMap.dcMotor.get("r2");

        gripperLift = hardwareMap.dcMotor.get("gripperlift");

        ballKnocker = hardwareMap.servo.get("bk");

        gripperLeft = hardwareMap.servo.get("gl");
        gripperRight = hardwareMap.servo.get("gr");

        leftMotor1.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotor2.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor1.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotor2.setDirection(DcMotorSimple.Direction.REVERSE);

        gripperLift.setDirection(DcMotorSimple.Direction.FORWARD);

        ballKnocker = hardwareMap.servo.get("bk");

    }

    public void closeGripper(){

        gripperRight.setPosition(0.53f);
        gripperLeft.setPosition(0.45f);

    }

    public void openGripper(){

        gripperRight.setPosition(0.75f);
        gripperLeft.setPosition(0.36f);

    }

}
