package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Aus on 10/24/2017.
 */
@TeleOp(name = "drawerSlideClaw", group = "linear OpMode")
public class drawerSlideClaw extends OpMode {
    private Servo claw;
    private DcMotor motor;
    private double servolowend = 0.0;
    private double servohighend = 0.43;
    private double motorSpeed;
    private Servo arm;
    private double servoDir;

    @Override
    public void init() {
        claw = hardwareMap.servo.get("s1");
        claw.setPosition(servohighend);
        motor = hardwareMap.dcMotor.get("m5");
        arm = hardwareMap.servo.get("s2");
    }

    @Override
    public void loop() {
        motorSpeed = gamepad1.left_stick_y;
        servoDir = Range.scale(gamepad1.right_stick_y, -1.0, 1.0, 0.0, 1.0);
        if (gamepad1.a) {
            //    claw.setPosition(Range.clip(claw.getPosition() + 0.001, servolowend, servohighend));
            claw.setPosition(servolowend);
        }
        if (gamepad1.b) {
            //    claw.setPosition(Range.clip(claw.getPosition() - 0.001, servolowend, servohighend));
            claw.setPosition(servohighend);
        }
        motor.setPower(motorSpeed);
        motor.setPower(servoDir);

        telemetry.addData("clawposition: ", claw.getPosition());
        telemetry.addData("servo dir", servo.getPosition());
    }

}
