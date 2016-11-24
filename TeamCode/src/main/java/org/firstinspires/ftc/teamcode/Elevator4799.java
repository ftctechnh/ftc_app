package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by FTC Team 4799-4800 on 11/17/2016.
 */

    import com.qualcomm.robotcore.eventloop.opmode.OpMode;
    import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

/**
     * Created by FTC Team 4799-4800 on 11/14/2016.
     */
    @TeleOp(name = "Elevator4799", group = "")
    public class Elevator4799 extends OpMode {
        DcMotor motorBackLeft;
        DcMotor motorFrontLeft;
        DcMotor motorBackRight;
        DcMotor motorFrontRight;
        DcMotor motorElevator;
        Servo rack;


        @Override
        public void init() {
            motorBackRight = hardwareMap.dcMotor.get("RightBack");
            motorFrontRight = hardwareMap.dcMotor.get("RightFront");
            motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
            motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
            motorElevator = hardwareMap.dcMotor.get("Elevator");
            rack = hardwareMap.servo.get("Rack");
        }

        @Override
        public void loop() {
            motorElevator.setPower(gamepad2.right_stick_y);

            motorBackLeft.setPower(gamepad1.right_stick_y);
            motorFrontLeft.setPower(gamepad1.right_stick_y);
            motorBackRight.setPower(-gamepad1.left_stick_y);
            motorFrontRight.setPower(-gamepad1.left_stick_y);

            if (gamepad1.left_bumper)
                rack.setPosition(1);
            else
            if (gamepad1.right_bumper)
                rack.setPosition(-1);
            else
                rack.setPosition(.5);
        }

    }

