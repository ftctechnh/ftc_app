package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Kaden on 10/19/2017.
 */
@TeleOp(name = "ForkliftDrivewith2remotes", group = "linear OpMode")
public class ForkliftDrive2controllers extends OpMode {
    private Servo rightClaw;
    private Servo leftClaw;
    private double clawPosition = 0.0;
    private double clawHighEnd = 0.45;
    private double clawLowEnd = 0.1;
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

    @Override
    public void init() {
        FrontLeft = hardwareMap.dcMotor.get("m1");
        FrontRight = hardwareMap.dcMotor.get("m2");
        RearLeft = hardwareMap.dcMotor.get("m3");
        RearRight = hardwareMap.dcMotor.get("m4");
        DrawerSlide = hardwareMap.dcMotor.get("m5");
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
        double right = gamepad1.right_stick_y;
        double left = gamepad1.left_stick_y;

        if (gamepad1.right_trigger > 0.1) {
            right = right * 0.4;
            left = left * 0.4;
        }

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
        up = gamepad2.right_trigger;
        down = gamepad2.left_trigger;
        DrawerSlideSpeed = up - down;
        DrawerSlide.setPower(DrawerSlideSpeed);
        FrontRight.setPower(right);
        FrontLeft.setPower(left);
        RearRight.setPower(right);
        RearLeft.setPower(left);
        telemetry.addData("Current Claw Position", rightClaw.getPosition());
    }
    public void reverseMotor(DcMotor motor) {

        motor.setDirection(DcMotor.Direction.REVERSE);
    }
}
