package org.firstinspires.ftc.teamcode;

import android.text.method.Touch;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Kaden on 10/19/2017.
 */
@TeleOp(name = "ScrimmageForkliftDrive", group = "linear OpMode")
public class ScrimmageForkLiftDrive extends OpMode {
    ForkLift Forklift;
    RelicClaw RelicClaw;
    //Relic Recovery
    private Servo relicClaw;
    private DcMotor motor;
    private double servolowend = 0.25;
    private double servohighend = 0.75;
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
        Forklift = new ForkLift(
            hardwareMap.servo.get("s5"), //rightClaw
            hardwareMap.servo.get("s6"), //leftClaw
            hardwareMap.dcMotor.get("m6"), //updown
            hardwareMap.get(TouchSensor.class, "b0"), //top button
            hardwareMap.get(TouchSensor.class, "b1")); //bottom button
        Forklift.initClaw();
        //Relic Recovery
        RelicClaw = new RelicClaw(
            hardwareMap.servo.get("s1"), //claw
            hardwareMap.servo.get("s2"), //arm
            hardwareMap.dcMotor.get("m5")); //motor
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

        //Forklift
        if (gamepad1.a) {
            Forklift.closeClaw();
        }
        if (gamepad1.b) {
            Forklift.openClaw();
        }
        Forklift.moveUpDown(gamepad1.right_trigger - gamepad1.left_trigger);
        
        //Relic recovery
        if (gamepad2.a) {
            RelicClaw.closeClaw();
        }
        if (gamepad2.b) {
            RelicClaw.openClaw();
        }
        RelicClaw.setArmPosition(Range.clip(gamepad2.right_stick_y / 250 + RelicClaw.getArmPosition(), 0.0, 1.0));
        RelicClaw.moveMotor(gamepad2.left_stick_y);

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