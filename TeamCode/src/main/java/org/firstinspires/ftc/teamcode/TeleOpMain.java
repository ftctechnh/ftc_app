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

    BotHardware robot = new BotHardware();

    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");

        // hardware maps
        robot.init(hardwareMap);
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
            robot.frontLeftMotor.setPower(gamepad1.left_stick_y);
            robot.frontRightMotor.setPower(gamepad1.right_stick_y);
            robot.backLeftMotor.setPower(gamepad1.left_stick_y);
            robot.backRightMotor.setPower(gamepad1.right_stick_y);
        }
    }

    protected void setFrontPower(float power) {
        robot.frontLeftMotor.setPower(power);
        robot.frontRightMotor.setPower(power);
    }

    protected void setBackPower(float power) {
        robot.backLeftMotor.setPower(power);
        robot.backRightMotor.setPower(power);
    }
}
