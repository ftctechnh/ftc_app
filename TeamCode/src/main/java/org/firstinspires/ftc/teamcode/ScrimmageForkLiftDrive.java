package org.firstinspires.ftc.teamcode;

import android.text.method.Touch;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Kaden on 10/19/2017.
 */
@TeleOp(name = "ScrimmageForkliftDrive", group = "linear OpMode")
public class ScrimmageForkLiftDrive extends OpMode {
    //Forklift
    //Claw
    private Servo rightClaw;
    private Servo leftClaw;
    private double clawPosition = 0.0;
    private double clawHighEnd = 0.45;
    private double clawLowEnd = 0.1;
    //Drawer Slide
    private double DrawerSlideLowEnd;
    private double DrawerSlideHighEnd;
    private double DrawerSlideSpeed = 0;
    private double up = 0;
    private double down = 0;
    private TouchSensor TopButton;
    private TouchSensor BottomButton;
    private DcMotor DrawerSlide;
    //Relic Recovery
    private Servo relicClaw;
    private DcMotor motor;
    private double servolowend = 0.0;
    private double servohighend = 0.8;
    private double motorSpeed;
    private Servo arm;
    private double armDir;
    private double armlowend = 0.0;
    private double armhighend = 1.0;
    //Driving
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
    private double speed = 0.75;


    @Override
    public void init() {
        //Forklift
        // Right and Left claws are from back to front point of view
        rightClaw = hardwareMap.servo.get("s5");
        rightClaw.setDirection(Servo.Direction.REVERSE);
        rightClaw.setPosition(clawPosition);
        leftClaw = hardwareMap.servo.get("s6");
        leftClaw.setPosition(clawPosition);
        DrawerSlide = hardwareMap.dcMotor.get("m6");
        TopButton = hardwareMap.get(TouchSensor.class, "b0");
        BottomButton = hardwareMap.get(TouchSensor.class, "b1");
        //Relic Recovery
        relicClaw = hardwareMap.servo.get("s1");
        relicClaw.setPosition(servohighend);
        motor = hardwareMap.dcMotor.get("m5");
        arm = hardwareMap.servo.get("s2");
        arm.setPosition(armlowend);
        //Driving
        FrontLeft = hardwareMap.dcMotor.get("m1");
        FrontRight = hardwareMap.dcMotor.get("m2");
        RearLeft = hardwareMap.dcMotor.get("m3");
        RearRight = hardwareMap.dcMotor.get("m4");


        reverseMotor(FrontRight);
        reverseMotor(RearRight);
    }

    @Override
    public void loop() {
        //Drive
        //Find x and y for each side
        x = gamepad1.left_stick_y;
        y = gamepad1.left_stick_x;
        x2 = gamepad1.right_stick_y;
        y2 = gamepad1.right_stick_x;
        //Do math to figure out relative speeds of each motor
        flSpeed = -y + x;
        frSpeed = y2 + x2;
        rlSpeed = y + x;
        rrSpeed = -y2 + x2;
        //If bumpers are pressed, rotate around a glyph
        if (gamepad1.right_bumper) {
            flSpeed = 0;
            frSpeed = 0;
            rlSpeed = -1;
            rrSpeed = 1;
        } else if (gamepad1.left_bumper) {
            flSpeed = 0;
            frSpeed = 0;
            rlSpeed = 1;
            rrSpeed = -1;
        }
        //Set speeds of each motor
        FrontLeft.setPower(speed * clip(flSpeed));
        FrontRight.setPower(speed * clip(frSpeed));
        RearLeft.setPower(speed * clip(rlSpeed));
        RearRight.setPower(speed * clip(rrSpeed));


        //Claw close and open
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
        //Drawer Slide
        up = gamepad1.right_trigger;
        down = gamepad1.left_trigger;
        DrawerSlideSpeed = up - down;
        DrawerSlide.setPower(DrawerSlideSpeed);
        if (!((!TopButton.isPressed() && DrawerSlideSpeed > 0) || (!(BottomButton.isPressed()) && DrawerSlideSpeed < 0))) {
            DrawerSlide.setPower(DrawerSlideSpeed);
        } else {
            DrawerSlide.setPower(0.0);
        }
        telemetry.addData("Current claw Position", rightClaw.getPosition());

        //Relic recovery
        if (gamepad2.a) {
           // relicClaw.setPosition(Range.clip(relicClaw.getPosition() + 0.005, 0.0, 1.0));
            relicClaw.setPosition(servolowend);
        }
        if (gamepad2.b) {
            //relicClaw.setPosition(Range.clip(relicClaw.getPosition() - 0.005, 0.0, 1.0));
            relicClaw.setPosition(servohighend);
        }
        armDir = Range.clip(gamepad2.right_stick_y / 250 + armDir, 0.0, 1.0);
        motorSpeed = gamepad2.left_stick_y;
        motor.setPower(motorSpeed);
        arm.setPosition(armDir);
        arm.setPosition(armDir);
    }

    //sets the motors to be reversed so they all go the same way.
    public void reverseMotor(DcMotor motor) {

        motor.setDirection(DcMotor.Direction.REVERSE);
    }

    //makes sure motors don't go out of range
    public double clip(double value) {
        return Range.clip(value, -1, 1);
    }
}