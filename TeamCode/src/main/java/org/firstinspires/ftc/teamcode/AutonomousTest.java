package org.firstinspires.ftc.teamcode;

import android.graphics.Path;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Kyle on 10/23/2016.
 */

public class AutonomousTest extends LinearOpMode {

    DcMotor BackRight;

    DcMotor BackLeft;

    DcMotor FrontRight;

    DcMotor FrontLeft;

    @Override
    public void runOpMode() throws InterruptedException {

        BackLeft = hardwareMap.dcMotor.get("BackLeft");

        BackRight = hardwareMap.dcMotor.get("BackRight");

        FrontLeft = hardwareMap.dcMotor.get("FrontLeft");

        FrontRight = hardwareMap.dcMotor.get("FrontRight");

        FrontRight.setDirection(DcMotor.Direction.REVERSE);

        BackLeft.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();

        BackLeft.setPower(-0.5);

        BackRight.setPower(-0.5);

        FrontLeft.setPower(-0.5);

        FrontRight.setPower(-0.5);


        sleep(1000);

        BackLeft.setPower(0.5);

        FrontRight.setPower(0.5);

        BackRight.setPower(-0.5);

        FrontLeft.setPower(-0.5);

        sleep(1100);

        BackLeft.setPower(-0.5);

        BackRight.setPower(-0.5);

        FrontLeft.setPower(-0.5);

        FrontRight.setPower(-0.5);

        sleep(1000);

        BackRight.setPower(0);

        BackLeft.setPower(0);

        FrontLeft.setPower(0);

        FrontRight.setPower(0);
    }
}
