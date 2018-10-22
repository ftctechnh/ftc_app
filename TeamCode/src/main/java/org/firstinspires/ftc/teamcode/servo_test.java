package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by sharma on 10/14/18.
 */
@TeleOp
public class servo_test extends LinearOpMode {
    //boom-ting
    CRServo spinnerR;
    CRServo spinnerL;
    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;
    DcMotor winch;
    //hello boom yo ting goes skrrra
    public void runOpMode() throws InterruptedException {
        spinnerL = hardwareMap.crservo.get("spinnerL");
        spinnerR = hardwareMap.crservo.get("spinnerR");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        winch = hardwareMap.dcMotor.get("winch");


        telemetry.addData("System: ", "waiting for initalization");
        telemetry.update();
        waitForStart();
        winch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        while (opModeIsActive()) {
            telemetry.addData("System: ", "initialized");
            telemetry.update();

            spinnerL.setPower((gamepad1.left_trigger - gamepad2.right_trigger));
            spinnerR.setPower(-(gamepad1.left_trigger - gamepad2.right_trigger));

            motorFrontRight.setPower(gamepad1.left_stick_x);
            motorFrontLeft.setPower(gamepad1.right_stick_y);
            motorBackRight.setPower(gamepad1.left_stick_y);
            motorBackLeft.setPower(gamepad1.right_stick_y);
        }
    }
}
