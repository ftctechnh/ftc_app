package org.firstinspires.ftc.teamcode.Deprecated;

/**
 * Created by Aus on 10/24/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "drawerSlideClaw", group = "linear OpMode")
@Disabled
public class drawerSlideClaw extends OpMode {
    private Servo claw;
    private DcMotor motor;
    private double servolowend = 0.0;
    private double servohighend = 0.8;
    private double motorSpeed;
    private Servo arm;
    private double armDir;
    private double armlowend = 0.0;
    private double armhighend = 1.0;

    @Override
    public void init() {
        claw = hardwareMap.servo.get("s1");
        claw.setPosition(servohighend);
        motor = hardwareMap.dcMotor.get("m5");
        arm = hardwareMap.servo.get("s2");
        arm.setPosition(armlowend);
    }

    @Override
    public void loop() {
        motorSpeed = gamepad1.left_stick_y/2;
        armDir = Range.scale(gamepad1.right_stick_y, -1.0, 1.0, 0.0, 1.0);
        if (gamepad1.a) {
            //claw.setPosition(Range.clip(claw.getPosition() + 0.005, servolowend, servohighend));
            claw.setPosition(servolowend);
        }
        if (gamepad1.b) {
            //claw.setPosition(Range.clip(claw.getPosition() - 0.005, servolowend, servohighend));
            claw.setPosition(servohighend);
        }
        motor.setPower(motorSpeed);
        arm.setPosition(armDir);
        arm.setPosition(armDir);
        telemetry.addData("clawposition: ", claw.getPosition());
        telemetry.addData("servo pos", arm.getPosition());
    }

}
/*TODO
nothing, i think
*/
