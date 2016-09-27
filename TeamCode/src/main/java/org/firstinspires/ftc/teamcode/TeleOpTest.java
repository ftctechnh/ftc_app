package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by jacost63 on 9/27/2016.
 */
@TeleOp(name="TeleOp Test", group="Linear Opmode")

public class TeleOpTest extends OpMode{

    DcMotor motorLeft = null;
    DcMotor motorRight = null;
    protected float leftY;
    protected float rightY;


    @Override
    public void init() {
        this.motorLeft = this.hardwareMap.dcMotor.get("motorLeft");
        this.motorRight = this.hardwareMap.dcMotor.get("motorRight");
        DcMotor.RunMode mode = DcMotor.RunMode.RUN_USING_ENCODER;
        this.motorLeft.setMode(mode);
        this.motorRight.setMode(mode);

        this.motorLeft.setDirection(DcMotor.Direction.FORWARD);
        this.motorRight.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        leftY = -gamepad1.left_stick_y;
        rightY = -gamepad1.right_stick_y;

        motorLeft.setPower(leftY);
        motorRight.setPower(rightY);
    }
}
