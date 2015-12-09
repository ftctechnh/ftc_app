package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Robomain on 11/3/2015.
 */
public class TeleOp extends OpMode {
    DcMotor rightmotor;
    DcMotor leftmotor;
    DcMotor leftmotor2;
    DcMotor rightmotor2;
    DcMotor arm;
    Servo servo;
    boolean locked;

    /**
     * @override for initiation
     * the main initalization loop for the robots autonomous mode
     *
     */
    @Override
    public void init()
    {

        leftmotor = hardwareMap.dcMotor.get("leftmotor");
        rightmotor =hardwareMap.dcMotor.get("rightmotor");
        leftmotor2 = hardwareMap.dcMotor.get("leftmotor2");
        rightmotor2 = hardwareMap.dcMotor.get("rightmotor2");
        servo = hardwareMap.servo.get("servo");
        rightmotor.setDirection(DcMotor.Direction.REVERSE);
        arm = hardwareMap.dcMotor.get("arm");
    }
    @Override

    public void loop()
    {
        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;
        float rightY2 = -gamepad1.right_stick_y;
        if(gamepad1.left_bumper)
        {
            leftmotor.setPower(.5*leftY);
            rightmotor.setPower(.5*rightY);
            leftmotor2.setPower(.5*leftY);
            rightmotor2.setPower(.5*rightY);
        }
        else {

            leftmotor2.setPower(leftY);
            rightmotor2.setPower(rightY);
            leftmotor.setPower(leftY);
            rightmotor.setPower(rightY);
        }
        if (gamepad1.y)
        {
            servo.setPosition(0);
        }
        else if(gamepad1.b) {
        servo.setPosition(.6);
        }
        else if(gamepad1.a) {
            servo.setPosition(.8);
        }
        if (gamepad2.a)
        {
            while (!( gamepad2.b))
            {
                leftmotor.setPower(0);
                rightmotor.setPower(0);
                leftmotor2.setPower(.0);
                rightmotor2.setPower(.0);
                locked = true;
                telemetry.addData("estop",locked);
            }
        }
        arm.setPower(rightY2);
        telemetry.addData("power right",rightY);
        telemetry.addData("power arm",rightY2);
        telemetry.addData("power left", leftY);
        telemetry.addData("is half power on", gamepad1.left_bumper);
        telemetry.addData("servo postion", servo.getPosition() );//may have to remove
        telemetry.addData("estop",locked);
    }
}
