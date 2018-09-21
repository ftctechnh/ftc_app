package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp

public class MainTeleOp extends LinearOpMode{
    // Motors
    protected DcMotor rightDriveMotor;
    protected DcMotor leftDriveMotor;
    protected DcMotor intakeSlideMotor;
    protected DcMotor liftSlideMotor;
    protected DcMotor intake0Motor;
    protected DcMotor intake1Motor;

    // Servos
    protected Servo depositServo;

    // Color sensors
    protected LynxI2cColorRangeSensor sampleSensor;

    private void initOpMode() {
        //initialize all the motors
        rightDriveMotor = hardwareMap.get(DcMotor.class, "rightDriveMotor");
        leftDriveMotor = hardwareMap.get(DcMotor.class, "leftDriveMotor");
        intakeSlideMotor = hardwareMap.get(DcMotor.class, "intakeSlideMotor");
        liftSlideMotor = hardwareMap.get(DcMotor.class, "liftSlideMotor");
        intake0Motor = hardwareMap.get(DcMotor.class, "intake0Motor");
        intake1Motor = hardwareMap.get(DcMotor.class, "intake1Motor");

        //initialize the servos
        depositServo = hardwareMap.get(Servo.class, "depositServo");

        // Sensors initialization
        sampleSensor = hardwareMap.get(LynxI2cColorRangeSensor.class, "sampleSensor");


    }

    public void runOpMode() {
        initOpMode();
        waitForStart();
        while(opModeIsActive()) {
            drive();
            intake();
            lift();
            deposit();

        }
    }

    // slow variable to allow for 'slowmode' - allowing the robot to go slower.
    double slow = 1.66;

//controller 1
    private void drive() {
        if (gamepad1.right_bumper) {
            slow = 1;
        }
        if (gamepad1.left_bumper) {
            slow = 1.66;
        }

        rightDriveMotor.setPower(gamepad1.left_stick_y / slow);
        leftDriveMotor.setPower(-1 * gamepad1.left_stick_y / slow);

        if (gamepad1.right_stick_x != 0) {
            rightDriveMotor.setPower(gamepad1.right_stick_x / slow * 2);
            leftDriveMotor.setPower(gamepad1.right_stick_x / slow * 2);
        }
    }

//controller 2
    private void intake() {
        //Skeleton is there, but I don't know how the args in the setPower() function work so that's up to someone else
        /*if (gamepad2.left_stick_y != 0) {
            intakeSlideMotor.setPower(/*gamepad2.left_stick_y / slow * 2);

        if (gamepad2.right_trigger != 0) {
            intake0Motor.setPower(/*gamepad1.right_trigger / slow * 2);
            intake1Motor.setPower(/*gamepad1.right_trigger / slow * 2);*/

    }

//controller 2
    private void lift() {

    }

//controller 2
    private void deposit() {

    }
}
