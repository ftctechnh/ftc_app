package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


/**
 * Created by shivaan on 20/1/18.
 */

@Disabled
@TeleOp(name = "classic driver mapping", group = "agroup")

public class classicDriverMapping extends LinearOpMode{
    private DcMotor motorFrontLeft;
    private DcMotor motorBackLeft;
    private DcMotor motorFrontRight;
    private DcMotor motorBackRight;

    private boolean deadzone;
    private boolean deadzone2;

    @Override
    public void runOpMode() throws InterruptedException
    {
        motorFrontLeft = hardwareMap.dcMotor.get("MC1M1");
        motorBackLeft = hardwareMap.dcMotor.get("MC1M2");
        motorFrontRight = hardwareMap.dcMotor.get("MC2M1");
        motorBackRight = hardwareMap.dcMotor.get("MC2M2");

        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while(opModeIsActive())
        {
            if (gamepad1.left_stick_x<0.05 && gamepad1.left_stick_y<0.05) {
                deadzone = true;
            }

            if (!(gamepad1.left_stick_x<0.05) || !(gamepad1.left_stick_y<0.05)) {
                deadzone = false;
            }

            if (gamepad1.right_stick_x<0.05 && gamepad1.right_stick_y<0.05) {
                deadzone2 = true;
            }

            if (!(gamepad1.right_stick_x<0.05) || !(gamepad1.right_stick_y<0.05)) {
                deadzone2 = false;
            }

            //STOP
            if (gamepad1.left_stick_x<0.05 && gamepad1.left_stick_y<0.05 && gamepad1.right_stick_x<0.05 && gamepad1.right_stick_y<0.05 ) {
                motorFrontRight.setPower(0);
                motorFrontLeft.setPower(0);
                motorBackLeft.setPower(0);
                motorBackRight.setPower(0);
            }

            //LEFT-STICK
            if (deadzone = false) {
                //FORWARD
                if (gamepad1.right_stick_x<0.38 && gamepad1.right_stick_x>-0.38 && gamepad1.right_stick_y>0.05) {
                    motorFrontRight.setPower(gamepad1.right_trigger);
                    motorFrontLeft.setPower(gamepad1.right_trigger);
                    motorBackLeft.setPower(gamepad1.right_trigger);
                    motorBackRight.setPower(gamepad1.right_trigger);
                }
                //BACKWARD
                if (gamepad1.right_stick_x<0.38 && gamepad1.right_stick_x>-0.38 && gamepad1.right_stick_y<-0.05) {
                    motorFrontRight.setPower(-gamepad1.right_trigger);
                    motorFrontLeft.setPower(-gamepad1.right_trigger);
                    motorBackLeft.setPower(-gamepad1.right_trigger);
                    motorBackRight.setPower(-gamepad1.right_trigger);
                }
                //AXIS-LEFT
                if (gamepad1.right_stick_y<0.38 && gamepad1.right_stick_y>-0.38 && gamepad1.right_stick_x<-0.05) {
                    motorFrontRight.setPower(gamepad1.right_trigger);
                    motorFrontLeft.setPower(-gamepad1.right_trigger);
                    motorBackLeft.setPower(-gamepad1.right_trigger);
                    motorBackRight.setPower(gamepad1.right_trigger);
                }
                //AXIS-RIGHT
                if (gamepad1.right_stick_y<0.38 && gamepad1.right_stick_y>-0.38 && gamepad1.right_stick_x>0.05) {
                    motorFrontRight.setPower(-gamepad1.right_trigger);
                    motorFrontLeft.setPower(gamepad1.right_trigger);
                    motorBackLeft.setPower(gamepad1.right_trigger);
                    motorBackRight.setPower(-gamepad1.right_trigger);
                }
                /*
                //FORWARD
                if (gamepad1.right_stick_x<0.38 && gamepad1.right_stick_x>-0.38 && gamepad1.right_stick_y>0.05) {
                    telemetry.clear();
                    telemetry.addData("FWD", deadzone);
                    telemetry.update();
                }
                //BACKWARD
                if (gamepad1.right_stick_x<0.38 && gamepad1.right_stick_x>-0.38 && gamepad1.right_stick_y<-0.05) {
                    telemetry.clear();
                    telemetry.addData("BCK", deadzone);
                    telemetry.update();
                }
                //AXIS-LEFT
                if (gamepad1.right_stick_y<0.38 && gamepad1.right_stick_y>-0.38 && gamepad1.right_stick_x<-0.05) {
                    telemetry.clear();
                    telemetry.addData("LFT", deadzone);
                    telemetry.update();
                }
                //AXIS-RIGHT
                if (gamepad1.right_stick_y<0.38 && gamepad1.right_stick_y>-0.38 && gamepad1.right_stick_x>0.05) {
                    telemetry.clear();
                    telemetry.addData("RHT", deadzone);
                    telemetry.update();
                }
                */
                /*
                //FORWARD
                if ((gamepad1.left_stick_x<(Math.sqrt(2)/2)) && (gamepad1.left_stick_x>-(Math.sqrt(2)/2)) && (gamepad1.left_stick_y>0.05)) {
                    telemetry.clear();
                    telemetry.addData("FWD", deadzone);
                    telemetry.update();
                }
                //BACKWARD
                if ((gamepad1.left_stick_x<(Math.sqrt(2)/2)) && (gamepad1.left_stick_x>-(Math.sqrt(2)/2)) && (gamepad1.left_stick_y<-0.05)) {
                    telemetry.clear();
                    telemetry.addData("BCK", deadzone);
                    telemetry.update();
                }
                //AXIS-LEFT
                if ((gamepad1.left_stick_y<(Math.sqrt(2)/2)) && (gamepad1.left_stick_y>-(Math.sqrt(2)/2)) && (gamepad1.left_stick_x<-0.05)) {
                    telemetry.clear();
                    telemetry.addData("LFT", deadzone);
                    telemetry.update();
                }
                //AXIS-RIGHT
                if ((gamepad1.left_stick_y<(Math.sqrt(2)/2)) && (gamepad1.left_stick_y>-(Math.sqrt(2)/2)) && (gamepad1.left_stick_x>0.05)) {
                    telemetry.clear();
                    telemetry.addData("RHT", deadzone);
                    telemetry.update();
                }
                */
            }

            //RIGHT-STICK
            if (deadzone2 = false) {
                //FORWARD
                if (gamepad1.right_stick_x<0.38 && gamepad1.right_stick_x>-0.38 && gamepad1.right_stick_y>0.05) {
                    motorFrontRight.setPower(gamepad1.right_trigger);
                    motorFrontLeft.setPower(gamepad1.right_trigger);
                    motorBackLeft.setPower(gamepad1.right_trigger);
                    motorBackRight.setPower(gamepad1.right_trigger);
                }
                //BACKWARD
                if (gamepad1.right_stick_x<0.38 && gamepad1.right_stick_x>-0.38 && gamepad1.right_stick_y<-0.05) {
                    motorFrontRight.setPower(-gamepad1.right_trigger);
                    motorFrontLeft.setPower(-gamepad1.right_trigger);
                    motorBackLeft.setPower(-gamepad1.right_trigger);
                    motorBackRight.setPower(-gamepad1.right_trigger);
                }
                //SWAY-LEFT
                if (gamepad1.right_stick_y<0.38 && gamepad1.right_stick_y>-0.38 && gamepad1.right_stick_x<-0.05) {
                    motorFrontRight.setPower(gamepad1.right_trigger);
                    motorFrontLeft.setPower(-gamepad1.right_trigger);
                    motorBackLeft.setPower(gamepad1.right_trigger);
                    motorBackRight.setPower(-gamepad1.right_trigger);
                }
                //SWAY-RIGHT
                if (gamepad1.right_stick_y<0.38 && gamepad1.right_stick_y>-0.38 && gamepad1.right_stick_x>0.05) {
                    motorFrontRight.setPower(-gamepad1.right_trigger);
                    motorFrontLeft.setPower(gamepad1.right_trigger);
                    motorBackLeft.setPower(-gamepad1.right_trigger);
                    motorBackRight.setPower(gamepad1.right_trigger);
                }
            }

            idle();
        }
    }
}
