package com.greengirls;

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
    private DcMotor liftMotor;

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
        liftMotor = hardwareMap.dcMotor.get("lift_motor");

    }

    @Override public void loop() {

    }
}
