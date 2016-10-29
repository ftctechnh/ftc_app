/*
    Main robot teleop program
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "TeleOpMain", group = "Main")
//@Disabled
public class TeleOpMain extends OpMode {

    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;
    DcMotor backRightMotor;

    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");

        // hardware maps
        frontLeftMotor = hardwareMap.dcMotor.get("front_left");
        frontRightMotor = hardwareMap.dcMotor.get("front_right");
        backLeftMotor = hardwareMap.dcMotor.get("back_left");
        backRightMotor = hardwareMap.dcMotor.get("back_right");

        // change directions if necessary
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    @Override
    public void start() {}

    @Override
    public void loop() {

        /*

                         1,1,1,1
                            |
                 0,0,1,1    |    1,1,0,0
                            |
            -1,-1,1,1 ------------- 1,1,-1,-1
                            |
               -1,-1,0,0    |    0,0,-1,-1
                            |
                       -1,-1,-1,-1

        float x = gamepad1.left_stick_x;
        float y = -1 * gamepad1.left_stick_y;

        float ySign = (y >= 0) ? 1 : -1;

        float frontPower = (ySign * x > 0) ? ySign : (ySign * Math.abs(x) * -2 + ySign);
        float backPower = (ySign * x < 0) ? ySign : (ySign * Math.abs(x) * -2 + ySign);

        */

        // run drivetrain motors
        // dpad steering
        if(gamepad1.dpad_up && gamepad1.dpad_left) {
            setFrontPower(0.0f);
            setBackPower(-1.0f);
        }
        else if(gamepad1.dpad_up && gamepad1.dpad_right) {
            setFrontPower(-1.0f);
            setBackPower(0.0f);
        }
        else if(gamepad1.dpad_down && gamepad1.dpad_left) {
            setFrontPower(1.0f);
            setBackPower(0.0f);
        }
        else if(gamepad1.dpad_down && gamepad1.dpad_right) {
            setFrontPower(0.0f);
            setBackPower(1.0f);
        }
        else if(gamepad1.dpad_up) {
            setFrontPower(-1.0f);
            setBackPower(-1.0f);
        }
        else if(gamepad1.dpad_left) {
            setFrontPower(1.0f);
            setBackPower(-1.0f);
        }
        else if(gamepad1.dpad_right) {
            setFrontPower(-1.0f);
            setBackPower(1.0f);
        }
        else if(gamepad1.dpad_down) {
            setFrontPower(1.0f);
            setBackPower(1.0f);
        }
        else {
            // joystick tank steering
            frontLeftMotor.setPower(gamepad1.left_stick_y);
            frontRightMotor.setPower(gamepad1.right_stick_y);
            backLeftMotor.setPower(gamepad1.left_stick_y);
            backRightMotor.setPower(gamepad1.right_stick_y);
        }
    }

    protected void setFrontPower(float power) {
        frontLeftMotor.setPower(power);
        frontRightMotor.setPower(power);
    }

    protected void setBackPower(float power) {
        backLeftMotor.setPower(power);
        backRightMotor.setPower(power);
    }
}
