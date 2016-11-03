package org.firstinspires.ftc.teamcode;

import android.graphics.Path;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
/**
 * Created by Kyle on 10/22/2016.
 */

@TeleOp(name="MasterClass", group="Pushbot")
public class MasterClass extends OpMode {

    DcMotor BackRight;

    DcMotor BackLeft;

    DcMotor FrontRight;

    DcMotor FrontLeft;

    DcMotor FlyLeft;

    DcMotor FlyRight;

    @Override
    public void init() {

        BackLeft = hardwareMap.dcMotor.get("BackLeft");

        BackRight = hardwareMap.dcMotor.get("BackRight");

        FrontLeft = hardwareMap.dcMotor.get("FrontLeft");

        FrontRight = hardwareMap.dcMotor.get("FrontRight");

        FlyLeft = hardwareMap.dcMotor.get("FlyLeft");

        FlyRight = hardwareMap.dcMotor.get("FlyRight");

        FrontRight.setDirection(DcMotor.Direction.REVERSE);

        BackLeft.setDirection(DcMotor.Direction.REVERSE);

        FlyRight.setDirection(DcMotor.Direction.FORWARD);

        FlyLeft.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        float leftY = -gamepad1.left_stick_y;

        float rightY = -gamepad1.right_stick_y;

        BackRight.setPower(rightY);

        FrontRight.setPower(leftY);

        BackLeft.setPower(leftY);

        FrontLeft.setPower(rightY);

        //FlyRight.setPower(rightY);

        //FlyLeft.setPower(rightY);
    }
}
