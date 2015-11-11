package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;

/**
 * Created by Carlos on 11/11/2015.
 */
public class ClassifiedTeleOp extends OpMode{


    Lift lift = new Lift();
    Drivetrain drivetrain = new Drivetrain();

    @Override
    public void init() {
        lift.init(hardwareMap);
        drivetrain.init(hardwareMap);
    }

    @Override
    public void loop() {

        if(!lift.isLocked) {
            drivetrain.arcadeDrive(gamepad1.left_stick_y, gamepad1.right_stick_x);
        }

        if(!lift.isLocked) {
            lift.setSpeed(gamepad2.left_stick_y);
        }
        else
        {
            lift.setSpeed(gamepad2.left_stick_y * 3.0 / 4.0);

            drivetrain.arcadeDrive(-gamepad2.left_stick_y, 0);
        }

        if(gamepad2.dpad_up){
            lift.isLocked = true;

            drivetrain.arcadeDrive(0,0);
            lift.setSpeed(0);

            lift.setGear(2);
        }

        if(gamepad2.dpad_down){
            lift.isLocked = false;

            drivetrain.arcadeDrive(0, 0);
            lift.setSpeed(0);

            lift.setGear(1);
        }

        if(gamepad2.y){
            lift.armMotor.setPower(lift.armMotorForwardSpeed);
        }


        if(gamepad2.a){
            lift.armMotor.setPower(lift.armMotorBackwardSpeed);
        }


        if(!(gamepad2.y || gamepad2.a)){
            lift.armMotor.setPower(lift.armMotorStoppedSpeed);
        }

        if(gamepad2.right_bumper){
            lift.armServo.setPosition(lift.armServoUpwardSpeed);
        }

        if(gamepad2.right_trigger > 0.5){
            lift.armServo.setPosition(lift.armServoDownwardSpeed);
        }

        if(!(gamepad2.right_bumper || gamepad2.right_trigger > 0.5)){
            lift.armServo.setPosition(lift.armServoStoppedSpeed);
        }
    }
}

class Lift{
    public DcMotor rightMotor;
    public DcMotor leftMotor;

    public DcMotor armMotor;
    double armMotorForwardSpeed = 0.5;
    double armMotorStoppedSpeed = 0.0;
    double armMotorBackwardSpeed = -0.5;

    public Servo armServo;
    double armServoUpwardSpeed = 0.6;
    double armServoStoppedSpeed = 0.5;
    double armServoDownwardSpeed = 0.4;

    public Servo leftShifter;
    double leftShifterHighGear = 0.93;
    double leftShifterLowGear = 0.75;

    public Servo rightShifter;
    double rightShifterHighGear = 0.35;
    double rightShifterLowGear = 0.50;

    boolean isLocked = false;

    public Lift(){

    }

    public void init(HardwareMap hardwareMap) {
        leftMotor = hardwareMap.dcMotor.get("leftLiftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightLiftMotor");
        armMotor = hardwareMap.dcMotor.get("armMotor");

        leftShifter = hardwareMap.servo.get("leftLiftServo");
        rightShifter = hardwareMap.servo.get("rightLiftServo");
        armServo = hardwareMap.servo.get("armServo");

        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        leftShifter.setPosition(leftShifterHighGear);
        rightShifter.setPosition(rightShifterHighGear);

        armServo.setPosition(armServoStoppedSpeed);
    }

    public void setSpeed(double speed){
        leftMotor.setPower(speed);
        rightMotor.setPower(speed);
    }

    public void setGear(int gear){

        switch (gear) {
            case 1:
                leftShifter.setPosition(leftShifterLowGear);
                rightShifter.setPosition(rightShifterLowGear);
                break;
            case 2:
                leftShifter.setPosition(leftShifterHighGear);
                rightShifter.setPosition(rightShifterHighGear);
                break;
            default:
                break;
        }
    }
}

class Drivetrain{
    public DcMotor frontLeft;
    public DcMotor backLeft;
    public DcMotor frontRight;
    public DcMotor backRight;

    public Drivetrain(){

    }

    public void init(HardwareMap hardwareMap){
        frontLeft = hardwareMap.dcMotor.get("leftMotor1");
        frontRight = hardwareMap.dcMotor.get("leftMotor2");
        backLeft = hardwareMap.dcMotor.get("rightMotor1");
        backRight = hardwareMap.dcMotor.get("rightMotor2");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
    }

    public void tankDrive(double leftSpeed,double rightSpeed){
        frontLeft.setPower(leftSpeed);
        backLeft.setPower(leftSpeed);

        frontRight.setPower(rightSpeed);
        backRight.setPower(rightSpeed);
    }

    public void arcadeDrive(double throttle, double turn){
        frontLeft.setPower(throttle - turn);
        backLeft.setPower(throttle - turn);

        frontRight.setPower(throttle + turn);
        backRight.setPower(throttle + turn);
    }

}