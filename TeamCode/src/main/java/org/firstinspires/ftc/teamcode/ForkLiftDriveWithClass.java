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
public class ForkliftDriveWithClass extends OpMode {
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
    private double up = 0;
    private double down = 0;

    @Override
    public void init() {
        FrontLeft = hardwareMap.dcMotor.get("m1");
        FrontRight = hardwareMap.dcMotor.get("m2");
        RearLeft = hardwareMap.dcMotor.get("m3");
        RearRight = hardwareMap.dcMotor.get("m4");
        ForkLift = ForkLift(
            hardwareMap.Servo.get("s1"), 
            hardwareMap.Servo.get("s2"), 
            hardwareMap.DcMotor.get("m5"), 
            hardwareMap.get(DigitalChannel.class, "b1"),
            hardwareMap.get(DigitalChannel.class, "b2"))
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
