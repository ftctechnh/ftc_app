package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Aus on 11/14/2017.
 */
@TeleOp(name = "forkliftMechanum", group = "linear OpMode")
public class forkliftMechanum extends OpMode {
    private Servo rightClaw;
    private Servo leftClaw;
    private double clawPosition = 0.0;
    private double clawHighEnd = 0.45;
    private double clawLowEnd = 0.2;
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor RearLeft;
    private DcMotor RearRight;
    private DcMotor DrawerSlide;
    private double DrawerSlideLowEnd;
    private double DrawerSlideHighEnd;
    private double DrawerSlideSpeed = 0;
    private double up = 0;
    private double down = 0;
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
        DrawerSlide = hardwareMap.dcMotor.get("m5");
        // Right and Left claws are from back to front point of view
        rightClaw = hardwareMap.servo.get("s1");
        rightClaw.setDirection(Servo.Direction.REVERSE);
        rightClaw.setPosition(clawPosition);
        leftClaw = hardwareMap.servo.get("s2");
        leftClaw.setPosition(clawPosition);
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


        if (gamepad1.a) {
            clawPosition = clawHighEnd;
            //clawPosition = clawPosition + 0.001;

        }
        if (gamepad1.b) {
            clawPosition = clawLowEnd;
            //clawPosition = clawPosition - 0.001;
        }
        rightClaw.setPosition(clawPosition);
        rightClaw.setPosition(clawPosition);
        leftClaw.setPosition(clawPosition);
        leftClaw.setPosition(clawPosition);
        up = gamepad1.right_trigger;
        down = gamepad1.left_trigger;
        DrawerSlideSpeed = up - down;
        DrawerSlide.setPower(DrawerSlideSpeed);
        telemetry.addData("Current rightClaw Position", rightClaw.getPosition());
    }


    public double clip(double value) {
        return Range.clip(value, -1,1);
    }



    public void reverseMotor(DcMotor motor) {

        motor.setDirection(DcMotor.Direction.REVERSE);
    }
}
