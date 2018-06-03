package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;

/**
 * Created by shiva on 25/5/18.
 */

@TeleOp(name = "test", group = "prototype")

public class test extends LinearOpMode {

    private DcMotor motorFrontLeft;
    private DcMotor motorBackLeft;
    private DcMotor motorFrontRight;
    private DcMotor motorBackRight;

//    private Servo relicArm;

    private int mode;
    private double pos;

    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();

        while(opModeIsActive()) {

            telemetry.addLine("it works.");

//            //RESET ENCODERS
//            if (gamepad1.y) {
//                motorFrontLeft.setMode(STOP_AND_RESET_ENCODER);
//                motorFrontRight.setMode(STOP_AND_RESET_ENCODER);
//                motorBackLeft.setMode(STOP_AND_RESET_ENCODER);
//                motorBackRight.setMode(STOP_AND_RESET_ENCODER);
//
//                motorFrontLeft.setMode(RUN_USING_ENCODER);
//                motorFrontRight.setMode(RUN_USING_ENCODER);
//                motorBackLeft.setMode(RUN_USING_ENCODER);
//                motorBackRight.setMode(RUN_USING_ENCODER);
//            }
//                telemetry.addLine("MODE-1 <NATIONALS ALGORITHM>");
//
//                double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
//                telemetry.addData("r = ", r);
//                double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
//                telemetry.addData("robotAngle = ", robotAngle);
//                double rightX = gamepad1.right_stick_x;
//                //telemetry.addData("rightX = ", rightX);
//                final double v1 = r * Math.cos(robotAngle) + rightX;
//                telemetry.addData("front left power = ", v1);
//                final double v2 = r * Math.sin(robotAngle) - rightX;
//                telemetry.addData("front right power = ", v2);
//                final double v3 = r * Math.sin(robotAngle) + rightX;
//                telemetry.addData("back left power = ", v3);
//                final double v4 = r * Math.cos(robotAngle) - rightX;
//                telemetry.addData("back right power = ", v4);
//
//
//                telemetry.addData("front left degrees = ", motorFrontLeft.getCurrentPosition());
//                telemetry.addData("front right degrees = ",motorFrontRight.getCurrentPosition());
//                telemetry.addData("back left degrees = ", motorBackLeft.getCurrentPosition());
//                telemetry.addData("back right degrees = ", motorBackRight.getCurrentPosition());
//                telemetry.update();
//
//                motorFrontLeft.setPower(v1);
//                motorFrontRight.setPower(v2);
//                motorBackLeft.setPower(v3);
//                motorBackRight.setPower(v4);
        }
    }

}
