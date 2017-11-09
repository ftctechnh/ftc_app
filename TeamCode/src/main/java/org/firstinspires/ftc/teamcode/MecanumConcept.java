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

@TeleOp(name = "MecanumDriveConcept", group = "linear OpMode")
//@Disabled
public class MecanumConcept extends OpMode {
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor RearLeft;
    private DcMotor RearRight;
    private float x;
    private float y;
    private float z;
    private double speed;


    @Override
    public void init() {
        FrontLeft = hardwareMap.dcMotor.get("m1");
        FrontRight = hardwareMap.dcMotor.get("m2");
        RearLeft = hardwareMap.dcMotor.get("m3");
        RearRight = hardwareMap.dcMotor.get("m4");
        reverseMotor(FrontLeft);
        reverseMotor(RearLeft);
        speed = 0.5;
    }

    @Override
    public void loop() {
        //x is forward/backward
        //y is side to side
        //z is angle
        x = gamepad1.left_stick_x;
        y = gamepad1.left_stick_y;
        z = gamepad1.right_stick_x;

        FrontLeft.setPower(speed*clip(y-x+z));
        FrontRight.setPower(speed*clip(y+x-z));
        RearLeft.setPower(speed*clip(y+x+z));
        RearRight.setPower(speed*clip(y-x-z));

    }

    public void reverseMotor(DcMotor motor) {
        motor.setDirection(DcMotor.Direction.REVERSE);
    }

    public float clip(float value) {
        return Range.clip(value, -1,1);
    }

}
