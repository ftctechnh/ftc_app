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

        // run drivetrain motors
        /*if(gamepad1.dpad_left) {
            frontLeftMotor.setPower(-1.0);
            frontRightMotor.setPower(-1.0);
            backLeftMotor.setPower(1.0);
            backRightMotor.setPower(1.0);
        }
        else if(gamepad1.dpad_right) {
            frontLeftMotor.setPower(1.0);
            frontRightMotor.setPower(1.0);
            backLeftMotor.setPower(-1.0);
            backRightMotor.setPower(-1.0);
        }
        else if(gamepad1.x) {
            frontLeftMotor.setPower(1.0);
            frontRightMotor.setPower(1.0);
            backLeftMotor.setPower(0.0);
            backRightMotor.setPower(0.0);
        }
        else if(gamepad1.b) {
            frontLeftMotor.setPower(0.0);
            frontRightMotor.setPower(0.0);
            backLeftMotor.setPower(1.0);
            backRightMotor.setPower(1.0);
        }*/
        if(gamepad1.a) {
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

             */

            float x = gamepad1.left_stick_x;
            float y = -1 * gamepad1.left_stick_y;

            float ySign = (y >= 0) ? 1 : -1;

            float frontPower = (ySign * x > 0) ? ySign : (ySign * Math.abs(x) * -2 + ySign);
            float backPower = (ySign * x < 0) ? ySign : (ySign * Math.abs(x) * -2 + ySign);

            // left and right edge cases don't work
            //float frontPower = (ySign * x > 0) ? y : (y * Math.abs(x) * -2 + y);
            //float backPower = (ySign * x < 0) ? y : (y * Math.abs(x) * -2 + y);

            if(x == 0 && y == 0){
                frontPower = 0;
                backPower = 0;
            }

            telemetry.addData("front", frontPower);
            telemetry.addData("back", backPower);

            frontLeftMotor.setPower(frontPower);
            frontRightMotor.setPower(frontPower);
            backLeftMotor.setPower(backPower);
            backRightMotor.setPower(backPower);

        }
        else {
            frontLeftMotor.setPower(gamepad1.left_stick_y);
            frontRightMotor.setPower(gamepad1.right_stick_y);
            backLeftMotor.setPower(gamepad1.left_stick_y);
            backRightMotor.setPower(gamepad1.right_stick_y);
        }
    }
}
