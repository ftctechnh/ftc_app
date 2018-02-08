package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

/**
 * Created by Derek on 11/14/2017.
 */

@TeleOp(group = "Test",name = "MainTest")
public class testMainOp extends OpMode{
    private DcMotor A,B,C,D,arm;
    private Servo claw;
    private STATE clawState = STATE.OPEN;

    private enum STATE {
        OPEN(1),CENTER(0.75),CLOSED(0.4);

        public double value;

        STATE(double value) {
            this.value = value;
        }
    }

    @Override
    public void init() {
        try {
            B = hardwareMap.dcMotor.get("frontLeft");
            C = hardwareMap.dcMotor.get("frontRight");
            A = hardwareMap.dcMotor.get("backLeft");
            D = hardwareMap.dcMotor.get("backRight");
            arm = hardwareMap.dcMotor.get("arm");
            claw = hardwareMap.servo.get("claw");

            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            C.setDirection(REVERSE);
            D.setDirection(REVERSE);




        } catch (Exception e) {
            telemetry.addData("",e.getLocalizedMessage());
        }


    }

    @Override
    public void loop() {
        double side = - gamepad1.left_stick_x/2;
        double angle = gamepad1.right_stick_x / 4;
        double foreward = gamepad1.left_stick_y/2 ;

        A.setPower((foreward - side) + angle);
        B.setPower((foreward + side) + angle);
        C.setPower((foreward - side) - angle);
        D.setPower((foreward + side) - angle);

        arm.setPower(gamepad2.right_stick_y / 2);

        if (gamepad2.dpad_left) {
            clawState = STATE.OPEN;
        }

        if (gamepad2.dpad_up) {
            clawState = STATE.CENTER;
        }

        if (gamepad2.dpad_right) {
            clawState = STATE.CLOSED;
        }
        claw.setPosition(clawState.value);
    }
}
