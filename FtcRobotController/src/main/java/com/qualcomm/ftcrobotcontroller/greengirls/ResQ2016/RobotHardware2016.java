package com.greengirls.ResQ2016;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ServoController;
/**
 * Created by Dell User on 10/25/2015.
 */
public class RobotHardware2016 extends OpMode{

    //define Motors and MotorControllers
    private DcMotorController rightMotorController;
    private DcMotor rightFrontMotor;
    private DcMotorController leftMotorController;
    private DcMotor leftFrontMotor;
    private DcMotor rightBackMotor;
    private DcMotor leftBackMotor;
    private DcMotorController attachmentMotorController;
    private DcMotor liftMotorRight;
    private DcMotor liftMotorLeft;

    @Override public void init() {


        //Map hardware for Right motor controller
        rightMotorController = hardwareMap.dcMotorController.get("right_drive_controller");
        rightFrontMotor = hardwareMap.dcMotor.get("right_front_motor");
        rightBackMotor = hardwareMap.dcMotor.get("right_back_motor");

        //Map hardware for Left motor controller
        leftMotorController = hardwareMap.dcMotorController.get("left_drive_controller");
        leftFrontMotor = hardwareMap.dcMotor.get("left_front_motor");
        leftBackMotor = hardwareMap.dcMotor.get("left_back_motor");

        //Map hardware for attachment motor controller
        attachmentMotorController = hardwareMap.dcMotorController.get("attachment_controller");
        liftMotorRight = hardwareMap.dcMotor.get("lift_right_motor");
        liftMotorLeft = hardwareMap.dcMotor.get("lift_left_motor");

    }

    //get the power for both right motors
    public double getRightMotors(){
        return rightFrontMotor.getPower();
    }

    //set get power for both left motors
    public double getLeftMotors(){
        return leftFrontMotor.getPower();
    }

    //set power to right motors
    public void setRightMotors(double power){
        rightFrontMotor.setPower(power);
        rightBackMotor.setPower(power);
    }

    //set power to left motors
    public void setLeftMotors(double power){
        leftFrontMotor.setPower(power);
        leftBackMotor.setPower(power);
    }

    //get the power to right lift motor
    public double getLiftMotorRight(){
        return liftMotorRight.getPower();
    }

    //set the power to right lift motor
    public void setLiftMotorRight(double power) {
        liftMotorRight.setPower(power);
    }

    //get the power to left lift motor
    public double getLiftMotorLeft(){
        return liftMotorLeft.getPower();
    }

    //set the power to collector motor
    public void setLiftMotorLeft(double power) {
        liftMotorLeft.setPower(power);
    }

    @Override public void loop() {

    }
}
