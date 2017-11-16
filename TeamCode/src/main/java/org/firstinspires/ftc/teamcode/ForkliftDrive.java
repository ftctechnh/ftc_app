package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Kaden on 10/19/2017.
 */
@TeleOp(name = "ForkliftDrive", group = "linear OpMode")
public class ForkliftDrive extends OpMode {
    private Servo rightClaw;
    private Servo leftClaw;
    private double clawPosition = 0.0;
    private double clawHighEnd = 0.45;
    private double clawLowEnd = 0.1;
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
    private double z;
    private double speed;
    private DcMotor DrawerSlide;
    private double DrawerSlideLowEnd;
    private double DrawerSlideHighEnd;
    private double DrawerSlideSpeed = 0;
    private double up = 0;
    private double down = 0;

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
        speed = 0.75;

    }

    @Override
    public void loop() {
        x = gamepad1.left_stick_y;
        y = gamepad1.left_stick_x;
        z = gamepad1.right_stick_x;
        if (gamepad1.right_bumper) {
            y = 0.75;
            z = 0.5;
        }
        else if (gamepad1.left_bumper) {
            y = -.75;
            z = -.5;
        }

        flSpeed = -y+x-z;
        frSpeed = y+x+z;
        rlSpeed = y+x-z;
        rrSpeed = -y+x+z;
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

    public void reverseMotor(DcMotor motor) {
        motor.setDirection(DcMotor.Direction.REVERSE);
    }
    public double clip(double value) {
        return Range.clip(value, -1,1);
    }
}
