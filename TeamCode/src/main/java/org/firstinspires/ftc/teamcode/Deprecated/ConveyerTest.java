package org.firstinspires.ftc.teamcode;

/**
 * Created by Liam on 9/14/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp(name = "ConveyerTest", group = "linear OpMode")
@Disabled
public class ConveyerTest extends OpMode {
    DcMotor FLM;
    DcMotor FRM;
    DcMotor BLM;
    DcMotor BRM;
    DcMotor CVB;
    DcMotor MCB;

    @Override
    public void init(){
        FLM = hardwareMap.dcMotor.get("m1");
        FRM = hardwareMap.dcMotor.get("m2");
        BLM = hardwareMap.dcMotor.get("m3");
        BRM = hardwareMap.dcMotor.get("m4");
        CVB = hardwareMap.dcMotor.get("m5");
        MCB = hardwareMap.dcMotor.get("m6");

        BLM.setDirection(DcMotor.Direction.REVERSE);
        FLM.setDirection(DcMotor.Direction.REVERSE);
    }
    @Override
    public void loop(){
        double left = gamepad1.left_stick_y;
        double right = gamepad1.right_stick_y;
        FLM.setPower(left);
        BLM.setPower(left);
        FRM.setPower(right);
        BRM.setPower(right);
        CVB.setPower(gamepad2.left_stick_y);
        MCB.setPower((gamepad2.right_stick_y) / 10);
    }
}