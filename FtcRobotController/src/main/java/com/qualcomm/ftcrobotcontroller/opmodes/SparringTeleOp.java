package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Jerry on 11/7/2015.
 */
//4 wheeled drive, works with blue team robot, has servoes
public class SparringTeleOp extends OpMode {
    DcMotor DC_left;
    DcMotor DC_right;
    DcMotor Omni_left;
    DcMotor Omni_right;
    Servo Servo_left;
  //  Servo Servo_right;
    final double LeftClosed = 0;
    final double LeftOpen = 0.3;
    final double RightClosed = 0;
    final double RightOpen = 0.3;

    @Override
    public void init() {
        DC_left = hardwareMap.dcMotor.get("DC_left");
        DC_right = hardwareMap.dcMotor.get("DC_right");
        Omni_left = hardwareMap.dcMotor.get("Omni_left");
        Omni_right = hardwareMap.dcMotor.get("Omni_right");
        Servo_left = hardwareMap.servo.get("Servo_left");
//        Servo_right = hardwareMap.servo.get("Servo_right");
    }

    @Override
    public void loop() {
        //Movement- tank drive
        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;

        DC_left.setPower(leftY);
        DC_right.setPower(rightY);
        Omni_left.setPower(leftY);
        Omni_right.setPower(rightY);

        //Servo
        if (gamepad1.left_bumper) {
            Servo_left.setPosition(LeftOpen);
        }
        if (gamepad1.left_trigger > 0){
            Servo_left.setPosition(LeftClosed);
        }
        /*if (gamepad1.right_bumper) {
            Servo_left.setPosition(RightOpen);
        }
        if (gamepad1.right_trigger > 0){
            Servo_left.setPosition(RightClosed);
        }*/

        //Telemetry
        telemetry.addData("Left Motor Power", leftY);
        telemetry.addData("Right Motor Power", rightY);
    }
}