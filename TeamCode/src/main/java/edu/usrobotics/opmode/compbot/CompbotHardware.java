package edu.usrobotics.opmode.compbot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import edu.usrobotics.opmode.BaseHardware;


/**
 * Created by mborsch19 & dsiegler19 on 10/13/16.
 */
public class CompbotHardware extends BaseHardware {

    public DcMotor frontRight;
    public DcMotor frontLeft;
    public DcMotor backRight;
    public DcMotor backLeft;

    public DcMotor harvester;

    public DcMotor lift;

    public DcMotor shooterRight;
    public DcMotor shooterLeft;

    public boolean frCorrectDirection = true;
    public boolean flCorrectDirection = true;
    public boolean brCorrectDirection = false;
    public boolean blCorrectDirection = true;

    public boolean harvesterCorrectDirection = false;

    public boolean liftCorrectDirection = true;

    public boolean rightShooterCorrectDirection = true;
    public boolean leftShooterCorrectDirection = false;

    public float wheelDiameter = 4.0f;
    public float wheelRadius = wheelDiameter / 2f;
    public float wheelCircumference = 2f * (float)(Math.PI) * wheelRadius;

    @Override
    public void getDevices() {

        frontRight = hardwareMap.dcMotor.get ("fr");
        frontLeft = hardwareMap.dcMotor.get ("fl");
        backRight = hardwareMap.dcMotor.get ("br");
        backLeft = hardwareMap.dcMotor.get ("bl");

        harvester = hardwareMap.dcMotor.get("harvester");

        lift = hardwareMap.dcMotor.get("lft");

        shooterRight = hardwareMap.dcMotor.get("sr");
        shooterLeft = hardwareMap.dcMotor.get("sl");

        harvester.setDirection(harvesterCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        shooterRight.setDirection(rightShooterCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        shooterLeft.setDirection(leftShooterCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

        lift.setDirection(liftCorrectDirection ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);

    }

    public int inchesToEncoderTicks(float inches){

        return (int) (inches / wheelCircumference * 360f * 3.12);

    }

}
