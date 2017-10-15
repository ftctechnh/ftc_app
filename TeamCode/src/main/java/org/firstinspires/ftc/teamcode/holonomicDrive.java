package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by jodasue on 9/24/17.
 */

@TeleOp(name="holonomicDrive", group="Testing")
class holonomicDrive extends LinearOpMode {
    int motorConfig = 0;
    DcMotor frontMotor;
    DcMotor backMotor;
    DcMotor leftMotor;
    DcMotor rightMotor;

    /*
        0
      1   2
        3

        01 23
        03 12
        02 13

        10 23
        13 02
        12 03

        30 12
        32 01
        31 02

        20 13
        21 03
        23 01
     */

    void adjustMotorConfiguration() {
        if(gamepad1.a) {
            motorConfig = ++motorConfig % 3;
            switch(motorConfig)
            {
                case 0:
                    frontMotor = hardwareMap.dcMotor.get("front");
                    backMotor  = hardwareMap.dcMotor.get("back");
                    leftMotor  = hardwareMap.dcMotor.get("left");
                    rightMotor = hardwareMap.dcMotor.get("right");
                    break;
                case 1:
                    frontMotor = hardwareMap.dcMotor.get("front");
                    backMotor  = hardwareMap.dcMotor.get("left");
                    leftMotor  = hardwareMap.dcMotor.get("back");
                    rightMotor = hardwareMap.dcMotor.get("right");
                    break;
                case 2:
                    frontMotor = hardwareMap.dcMotor.get("front");
                    backMotor  = hardwareMap.dcMotor.get("right");
                    leftMotor  = hardwareMap.dcMotor.get("back");
                    rightMotor = hardwareMap.dcMotor.get("left");
                    break;
                default:
                    break;
            }
        }
    }

    void adjustMotorDirection() {
        DcMotor motor = null;
        String name = null;
        if (gamepad1.dpad_up == true) {
            motor = frontMotor;
            name = "front";
        } else if (gamepad1.dpad_right == true) {
            motor = rightMotor;
            name = "right";
        } else if (gamepad1.dpad_left == true) {
            motor = leftMotor;
            name = "left";
        } else if (gamepad1.dpad_down == true) {
            motor = backMotor;
            name = "down";
        }
        if (motor != null) {
            ElapsedTime timer = new ElapsedTime();
            timer.startTime();
            while (timer.seconds() < 10) {
                motor.setPower(1);
                if (gamepad1.x == true) {
                    if (motor.getDirection() == DcMotorSimple.Direction.FORWARD) {
                        motor.setDirection(DcMotorSimple.Direction.FORWARD);
                    } else {
                        motor.setDirection(DcMotorSimple.Direction.REVERSE);
                    }
                }
                telemetry.addData(name, motor.getDirection());
                telemetry.update();
            }
            motor.setPower(0);
            idle();
        }
    }

    @Override
    public void runOpMode() {
        frontMotor = hardwareMap.dcMotor.get("front");
        frontMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backMotor  = hardwareMap.dcMotor.get("back");
        backMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotor  = hardwareMap.dcMotor.get("left");
        leftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor = hardwareMap.dcMotor.get("right");
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        double x = 0;
        double y = 0;
        double rotate = 0;

        waitForStart();
        while (opModeIsActive()) {

//            adjustMotorConfiguration();
//            adjustMotorDirection();

            rotate = gamepad1.right_stick_y;
            if(rotate != 0) {
                frontMotor.setPower(-1*rotate);
                backMotor.setPower(rotate);
                leftMotor.setPower(-1*rotate);
                rightMotor.setPower(rotate);
            }
            else {
                x = gamepad1.left_stick_x;
                y = gamepad1.left_stick_y;
                frontMotor.setPower(x/2);
                backMotor.setPower(x/2);
                leftMotor.setPower(y/2);
                rightMotor.setPower(y/2);
            }

            telemetry.addData("x: ", x);
            telemetry.addData("y: ", y);
            telemetry.addData("rotate:", rotate);
            telemetry.update();
            idle();
        }
    }
}
