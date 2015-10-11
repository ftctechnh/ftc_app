package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.codelib.ArcadeDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by JackV on 9/12/15.
 */
public class TestOpMode extends OpMode{

    // MOTOR VALUES
    DcMotor motorRightBack;
    DcMotor motorLeftBack;
    DcMotor motorRightFront;
    DcMotor motorLeftFront;

    // CONSTRUCTOR
    public TestOpMode() {

    }

    @Override
    public void init() {
        motorLeftFront = hardwareMap.dcMotor.get("motor_1");
        motorRightFront = hardwareMap.dcMotor.get("motor_2");
        motorLeftBack = hardwareMap.dcMotor.get("motor_3");
        motorRightBack = hardwareMap.dcMotor.get("motor_4");
    }

    @Override
    public void loop() {
        motorLeftFront.setPower(-gamepad1.left_stick_y);
        motorLeftBack.setPower(-gamepad1.left_stick_y);

        motorRightFront.setPower(gamepad1.right_stick_y);
        motorRightBack.setPower(gamepad1.right_stick_y);
    }

    @Override
    public void stop() {
        stopRobot();
    }

    void stopRobot() {
        motorRightFront.setPower(0);
        motorLeftFront.setPower(0);
        motorRightBack.setPower(0);
        motorLeftBack.setPower(0);
    }
}