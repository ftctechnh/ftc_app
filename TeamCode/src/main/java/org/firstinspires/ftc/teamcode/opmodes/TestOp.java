package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.robot.Claw;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.*;
import static org.firstinspires.ftc.teamcode.robot.Claw.ClawPosition.*;
import static org.firstinspires.ftc.teamcode.robot.Claw.CLAW_INCREMENT;


/**
 * Created by frazierb on 10/18/2017.
 */
@TeleOp(name="Test", group="Test")
public class TestOp extends OpMode {
    private DcMotor leftDrive, rightDrive,joint;
    private Claw claw;

    @Override
    public void init() {
        try {
            leftDrive = hardwareMap.dcMotor.get("left");
            rightDrive = hardwareMap.dcMotor.get("right");
            joint = hardwareMap.dcMotor.get("joint");

            Servo left = hardwareMap.servo.get("leftClaw");
            Servo right = hardwareMap.servo.get("rightClaw");

            left.setDirection(Servo.Direction.REVERSE);

            claw = new Claw(left,right);

            rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
            joint.setZeroPowerBehavior(BRAKE);


        } catch (Exception e) {
            telemetry.addData("",e.getLocalizedMessage());
        }
    }

    @Override
    public void loop() {
        leftDrive.setPower(gamepad1.left_stick_y);
        rightDrive.setPower(gamepad1.right_stick_y);

        joint.setPower(gamepad2.left_stick_y/4);

        if (gamepad2.x) claw.setPosition(OPEN);
        if (gamepad2.b) claw.setPosition(CLOSED);
        if (gamepad2.y) claw.setPosition(CENTER);

        if (gamepad2.dpad_right) {
            claw.setPosition(claw.getPosition() + CLAW_INCREMENT);
        }

        if (gamepad2.dpad_left) {
            claw.setPosition(claw.getPosition() - CLAW_INCREMENT);
        }
    }
}
