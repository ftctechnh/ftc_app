package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name="wowee a test class", group="TeleOp")
public class okay extends OpMode {
    private CRServo blockServoL1, blockServoL2, blockServoR1, blockServoR2;
    @Override
    public void init() {
        telemetry.addData("Status", "Uninitialized...");
        blockServoL1 = hardwareMap.get(CRServo.class, "blockServoL1");
        blockServoL2 = hardwareMap.get(CRServo.class, "blockServoL2");
        blockServoR1 = hardwareMap.get(CRServo.class, "blockServoR1");
        blockServoR2 = hardwareMap.get(CRServo.class, "blockServoR2");
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {
        if (gamepad1.a){
            spinTheBois(1);
        } else if (gamepad1.b) {
            spinTheBois(-1);
        } else {
            spinTheBois(0);
        }
    }

    private void spinTheBois(double quickness){
        // I'm not sure if they'll all need to spin the same direction.
        blockServoL1.setPower(quickness);
        blockServoL2.setPower(quickness);
        blockServoR1.setPower(-quickness);
        blockServoR2.setPower(-quickness);
    }
}
