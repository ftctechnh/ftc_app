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

@TeleOp(name = "MecanumDrive2sticks", group = "linear OpMode")
//@Disabled
public class Mecanum2Sticks extends OpMode {
    private DcMotor FrontLeft;
    private double flSpeed = 0;
    private DcMotor FrontRight;
    private double frSpeed = 0;
    private DcMotor RearLeft;
    private double rlSpeed = 0;
    private DcMotor RearRight;
    private double rrSpeed = 0;
    private double x;
    private double y;
    private double x2;
    private double y2;
    private double speed;


    @Override
    public void init() {
        FrontLeft = hardwareMap.dcMotor.get("m1");
        FrontRight = hardwareMap.dcMotor.get("m2");
        RearLeft = hardwareMap.dcMotor.get("m3");
        RearRight = hardwareMap.dcMotor.get("m4");
        reverseMotor(FrontRight);
        reverseMotor(RearRight);
        speed = 0.75;
    }

    @Override
    public void loop() {
        //x is forward/backward
        //y is side to side
        //z is angle
        x = gamepad1.left_stick_y;
        y = gamepad1.left_stick_x;
        x2 = gamepad1.right_stick_y;
        y2 = gamepad1.right_stick_x;
        //z = gamepad1.right_stick_x;
        if (gamepad1.right_bumper) {
            y = 0.75;
          //  z = 0.5;
        }
        else if (gamepad1.left_bumper) {
            y = -.75;
            //z = -.5;
        }

        flSpeed = -y+x;
        frSpeed = y2+x2;
        rlSpeed = y+x;
        rrSpeed = -y2+x2;
        if (gamepad1.right_bumper) {
            flSpeed = 0;
            frSpeed = 0;
            rlSpeed = -1;
            rrSpeed = 1;
        }
        else if (gamepad1.left_bumper) {
            flSpeed = 0;
            frSpeed = 0;
            rlSpeed = 1;
            rrSpeed = -1;
        }
        FrontLeft.setPower(speed*clip(flSpeed));
        FrontRight.setPower(speed*clip(frSpeed));
        RearLeft.setPower(speed*clip(rlSpeed));
        RearRight.setPower(speed*clip(rrSpeed));

    }

    public void reverseMotor(DcMotor motor) {
        motor.setDirection(DcMotor.Direction.REVERSE);
    }

    public double clip(double value) {
        return Range.clip(value, -1,1);
    }

}
