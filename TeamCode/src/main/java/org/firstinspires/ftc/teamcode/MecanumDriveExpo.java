package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Kaden on 10/20/2017.
 */

@TeleOp(name = "MecanumDriveExpo", group = "linear OpMode")
@Disabled
public class MecanumDriveExpo extends OpMode {
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor RearLeft;
    private DcMotor RearRight;
    private double x;
    private double y;
    private double z;
    private double speed = 1;
    private double expo = 1.75;


    @Override
    public void init() {
        FrontLeft = hardwareMap.dcMotor.get("m1");
        FrontRight = hardwareMap.dcMotor.get("m2");
        RearLeft = hardwareMap.dcMotor.get("m3");
        RearRight = hardwareMap.dcMotor.get("m4");
        reverseMotor(FrontRight);
        reverseMotor(RearRight);
    }

    @Override
    public void loop() {
        //x is forward/backward
        //y is side to side
        //z is angle
        if (gamepad1.left_stick_y > 0) {
            x = Math.pow(gamepad1.left_stick_y, expo);
        }
        else if (gamepad1.left_stick_y < 0){
            x = -Math.pow(Math.abs(gamepad1.left_stick_y), expo);
        }
        else {
            x = 0;
        }

        if (gamepad1.left_stick_x > 0) {
            y = Math.pow(gamepad1.left_stick_x, expo);
        }
        else if (gamepad1.left_stick_x < 0){
            y = -Math.pow(Math.abs(gamepad1.left_stick_x), expo);
        }
        else {
            y = 0;
        }

        if (gamepad1.right_stick_x > 0) {
            z = Math.pow(gamepad1.right_stick_x, expo);
        }
        else if (gamepad1.right_stick_x < 0){
            z = -Math.pow(Math.abs(gamepad1.right_stick_x), expo);
        }
        else {
            z = 0;
        }

        FrontLeft.setPower(speed*clip(-y+x-z));
        FrontRight.setPower(speed*clip(y+x+z));
        RearLeft.setPower(speed*clip(y+x-z));
        RearRight.setPower(speed*clip(-y+x+z));

    }

    public void reverseMotor(DcMotor motor) {
        motor.setDirection(DcMotor.Direction.REVERSE);
    }

    public double clip(double value) {
        return Range.clip(value, -1,1);
    }

}
