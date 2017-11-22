package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Kaden on 10/19/2017.
 */
@TeleOp(name = "DriveEverything", group = "linear OpMode")
public class DriveEverything extends OpMode {
    private ForkLift Forklift;
    private RelicClaw RelicClaw;
    private DriveMecanum drive;


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
        RelicClaw.init();
        //Driving
        drive = new DriveMecanum(
            hardwareMap.dcMotor.get("m1"), //FrontLeft
            hardwareMap.dcMotor.get("m2"), //FrontRight
            hardwareMap.dcMotor.get("m3"), //RearLeft
            hardwareMap.dcMotor.get("m4"), //RearRight
            1.0); //top speed as a decimal
    }

    @Override
    public void loop() {
        //Drive
        drive.driveLeftRight(gamepad1.left_stick_y, gamepad1.right_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        if (gamepad1.right_bumper) {
            drive.swingRight();
        }
        else if (gamepad1.left_bumper) {
            drive.swingLeft();
        }
        
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