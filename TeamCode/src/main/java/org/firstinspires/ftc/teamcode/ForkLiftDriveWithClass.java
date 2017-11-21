package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.TouchSensor;
/*
 * Created by Kaden on 10/19/2017.
*/
@TeleOp(name = "ForkliftDriveWithClass", group = "linear OpMode")
public class ForkLiftDriveWithClass extends OpMode {
    private ForkLift Forklift;
    private DcMotor FrontLeft;
    private double flSpeed = 0;
    private DcMotor FrontRight;
    private double frSpeed = 0;
    private DcMotor RearLeft;
    private double rlSpeed = 0;
    private DcMotor RearRight;
    private double rrSpeed = 0;
    private double x;
    private double x2;
    private double y;
    private double y2;
    private double speed;

    @Override
    public void init() {
        FrontLeft = hardwareMap.dcMotor.get("m1");
        FrontRight = hardwareMap.dcMotor.get("m2");
        RearLeft = hardwareMap.dcMotor.get("m3");
        RearRight = hardwareMap.dcMotor.get("m4");
        Forklift = new ForkLift(
            hardwareMap.servo.get("s5"), //rightClaw
            hardwareMap.servo.get("s6"), //leftClaw
            hardwareMap.dcMotor.get("m6"), //updown
            hardwareMap.get(TouchSensor.class, "b0"), //top button
            hardwareMap.get(TouchSensor.class, "b1")); //bottom button
        reverseMotor(FrontRight);
        reverseMotor(RearRight);
        speed = 0.75;
        Forklift.initClaw();

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
            Forklift.closeClaw();

        }
        if (gamepad1.b) {
            Forklift.openClaw();
        }
        Forklift.moveUpDown(gamepad1.right_trigger - gamepad1.left_trigger);
    }

    public void reverseMotor(DcMotor motor) {
        motor.setDirection(DcMotor.Direction.REVERSE);
    }
    public double clip(double value) {
        return Range.clip(value, -1,1);
    }
}
