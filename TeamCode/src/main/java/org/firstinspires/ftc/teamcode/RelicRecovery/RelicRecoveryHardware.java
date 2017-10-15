package org.firstinspires.ftc.teamcode.RelicRecovery;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by jeppe on 25-09-2017.
 */
@TeleOp (name = "test",group = "TeleOp")
public class RelicRecoveryHardware extends OpMode {

    DcMotor rightMotor;
    DcMotor leftMotor;

    // power
    double rightPower;
    double leftPower;


    @Override
    public void init() {

        // Hardware Mapping
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        leftMotor = hardwareMap.dcMotor.get("leftMotor");

        // Other initialization stuff
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void loop() {
        rightPower = -gamepad1.right_stick_y;
        leftPower = -gamepad1.left_stick_y;
    }
}
