package org.firstinspires.ftc.teamcode.NEW_TEST;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by shiva on 25/5/18.
 */

@TeleOp(name = "Driving test", group = "prototype")

public class DRIVING extends LinearOpMode {

    private DcMotor motorFrontLeft;
    private DcMotor motorBackLeft;
    private DcMotor motorFrontRight;
    private DcMotor motorBackRight;

//    private Servo relicArm;

    private int mode;
    private double pos;

    @Override
    public void runOpMode() throws InterruptedException {

        motorFrontLeft = hardwareMap.dcMotor.get("MC1M1");
        motorBackLeft = hardwareMap.dcMotor.get("MC1M2");
        motorFrontRight = hardwareMap.dcMotor.get("MC2M1");
        motorBackRight = hardwareMap.dcMotor.get("MC2M2");

//        relicArm = hardwareMap.servo.get("RA");

        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        mode = 1;

//        relicArm.setPosition(0.4);

        waitForStart();

        while(opModeIsActive()) {

            //SET MODE
            if (gamepad1.a) {
                mode = 1;
            }
            if (gamepad1.b) {
                mode = 2;
            }
            if (gamepad1.x) {
                mode = 3;
            }

            //MODE-1 <NATIONALS ALGORITHM>
            if (mode == 1) {
                telemetry.addLine("MODE-1 <NATIONALS ALGORITHM>");

                double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
                telemetry.addData("r = ", r);
                double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
                telemetry.addData("robotAngle = ", robotAngle);
                double rightX = gamepad1.right_stick_x;
                //telemetry.addData("rightX = ", rightX);
                final double v1 = r * Math.cos(robotAngle) + rightX;
                telemetry.addData("front left power = ", v1);
                final double v2 = r * Math.sin(robotAngle) - rightX;
                telemetry.addData("front right power = ", v2);
                final double v3 = r * Math.sin(robotAngle) + rightX;
                telemetry.addData("back left power = ", v3);
                final double v4 = r * Math.cos(robotAngle) - rightX;
                telemetry.addData("back right power = ", v4);


                telemetry.addData("front left degrees = ", motorFrontLeft.getCurrentPosition());
                telemetry.addData("front right degrees = ",motorFrontRight.getCurrentPosition());
                telemetry.addData("back left degrees = ", motorBackLeft.getCurrentPosition());
                telemetry.addData("back right degrees = ", motorBackRight.getCurrentPosition());
                telemetry.update();

                motorFrontLeft.setPower(v1);
                motorFrontRight.setPower(v2);
                motorBackLeft.setPower(v3);
                motorBackRight.setPower(v4);

                if (gamepad1.y){

                    motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                    motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }
            }

            //MODE-2 <NEW ALGORITHM>
            if (mode == 2) {
                telemetry.addLine("MODE-2 <NEW ALGORITHM>");
                telemetry.addLine("This mode is incomplete.");
                telemetry.update();
            }

            //MODE-3 <D-PAD / CLASSIC>
            if (mode == 3) {
                telemetry.addLine("MODE-3 <CLASSIC / D-PAD>");
                telemetry.update();

                //AUTO BRAKE
                if (!gamepad1.dpad_up && !gamepad1.dpad_down && !gamepad1.dpad_left && !gamepad1.dpad_right && !gamepad1.x && !gamepad1.b) {
                    motorFrontLeft.setPower(0);
                    motorFrontRight.setPower(0);
                    motorBackLeft.setPower(0);
                    motorBackRight.setPower(0);

                    telemetry.addLine("AUTO-BREAK");
                    telemetry.update();
                }

                //FORWARD
                if (gamepad1.dpad_up && !gamepad1.dpad_down && !gamepad1.dpad_left && !gamepad1.dpad_right && !gamepad1.x && !gamepad1.b) {
                    motorFrontLeft.setPower(gamepad1.right_trigger);
                    motorFrontRight.setPower(gamepad1.right_trigger);
                    motorBackRight.setPower(gamepad1.right_trigger);
                    motorBackLeft.setPower(gamepad1.right_trigger);

                    telemetry.addLine("FORWARD");
                    telemetry.update();
                }

                //BACKWARD
                if (!gamepad1.dpad_up && gamepad1.dpad_down && !gamepad1.dpad_left && !gamepad1.dpad_right && !gamepad1.x && !gamepad1.b) {
                    motorFrontLeft.setPower(-gamepad1.right_trigger);
                    motorFrontRight.setPower(-gamepad1.right_trigger);
                    motorBackRight.setPower(-gamepad1.right_trigger);
                    motorBackLeft.setPower(-gamepad1.right_trigger);

                    telemetry.addLine("BACKWARD");
                    telemetry.update();
                }

                //AXIS-LEFT
                if (!gamepad1.dpad_up && !gamepad1.dpad_down && gamepad1.dpad_left && !gamepad1.dpad_right && !gamepad1.x && !gamepad1.b) {
                    motorFrontLeft.setPower(-gamepad1.right_trigger);
                    motorFrontRight.setPower(gamepad1.right_trigger);
                    motorBackRight.setPower(gamepad1.right_trigger);
                    motorBackLeft.setPower(-gamepad1.right_trigger);

                    telemetry.addLine("AXIS-LEFT");
                    telemetry.update();
                }

                //AXIS-RIGHT
                if (!gamepad1.dpad_up && !gamepad1.dpad_down && !gamepad1.dpad_left && gamepad1.dpad_right && !gamepad1.x && !gamepad1.b) {
                    motorFrontLeft.setPower(gamepad1.right_trigger);
                    motorFrontRight.setPower(-gamepad1.right_trigger);
                    motorBackRight.setPower(-gamepad1.right_trigger);
                    motorBackLeft.setPower(gamepad1.right_trigger);

                    telemetry.addLine("AXIS-RIGHT");
                    telemetry.update();
                }

                //SWAY-LEFT
                if (!gamepad1.dpad_up && !gamepad1.dpad_down && !gamepad1.dpad_left && !gamepad1.dpad_right && gamepad1.x && !gamepad1.b) {
                    motorFrontLeft.setPower(-gamepad1.right_trigger);
                    motorFrontRight.setPower(gamepad1.right_trigger);
                    motorBackRight.setPower(-gamepad1.right_trigger);
                    motorBackLeft.setPower(gamepad1.right_trigger);

                    telemetry.addLine("SWAY-LEFT");
                    telemetry.update();
                }

                //SWAY-RIGHT
                if (!gamepad1.dpad_up && !gamepad1.dpad_down && !gamepad1.dpad_left && !gamepad1.dpad_right && !gamepad1.x && gamepad1.b) {
                    motorFrontLeft.setPower(gamepad1.right_trigger);
                    motorFrontRight.setPower(-gamepad1.right_trigger);
                    motorBackRight.setPower(gamepad1.right_trigger);
                    motorBackLeft.setPower(-gamepad1.right_trigger);

                    telemetry.addLine("SWAY-RIGHT");
                    telemetry.update();
                }

                //DIAGONAL [NW]
                if (gamepad1.dpad_up && !gamepad1.dpad_down && gamepad1.dpad_left && !gamepad1.dpad_right && !gamepad1.x && !gamepad1.b) {
                    motorFrontLeft.setPower(0);
                    motorFrontRight.setPower(gamepad1.right_trigger);
                    motorBackRight.setPower(0);
                    motorBackLeft.setPower(gamepad1.right_trigger);

                    telemetry.addLine("DIAGONAL [NW]");
                    telemetry.update();
                }

                //DIAGONAL [NE]
                if (gamepad1.dpad_up && !gamepad1.dpad_down && !gamepad1.dpad_left && gamepad1.dpad_right && !gamepad1.x && !gamepad1.b) {
                    motorFrontLeft.setPower(gamepad1.right_trigger);
                    motorFrontRight.setPower(0);
                    motorBackRight.setPower(gamepad1.right_trigger);
                    motorBackLeft.setPower(0);

                    telemetry.addLine("DIAGONAL [NE]");
                    telemetry.update();
                }

                //DIAGONAL [SW]
                if (!gamepad1.dpad_up && gamepad1.dpad_down && gamepad1.dpad_left && !gamepad1.dpad_right && !gamepad1.x && !gamepad1.b) {
                    motorFrontLeft.setPower(-gamepad1.right_trigger);
                    motorFrontRight.setPower(0);
                    motorBackRight.setPower(-gamepad1.right_trigger);
                    motorBackLeft.setPower(0);

                    telemetry.addLine("DIAGONAL [SW]");
                    telemetry.update();
                }

                //DIAGONAL [SE]
                if (!gamepad1.dpad_up && !gamepad1.dpad_down && !gamepad1.dpad_left && !gamepad1.dpad_right && !gamepad1.x && gamepad1.b) {
                    motorFrontLeft.setPower(0);
                    motorFrontRight.setPower(-gamepad1.right_trigger);
                    motorBackRight.setPower(0);
                    motorBackLeft.setPower(-gamepad1.right_trigger);

                    telemetry.addLine("DIAGONAL [SE]");
                    telemetry.update();
                }

            }
        }
    }

}
