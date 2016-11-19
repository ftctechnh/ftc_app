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

    /**
     * Created by FTC Team 4799-4800 on 11/14/2016.
     */
    @TeleOp(name = "Elevator4799", group = "")
    public class Elevator4799 extends OpMode {
        DcMotor motorBackLeft;
        DcMotor motorFrontLeft;
        DcMotor motorBackRight;
        DcMotor motorFrontRight;

        @Override
        public void init() {
            motorBackRight = hardwareMap.dcMotor.get("RightBack");
            motorFrontRight = hardwareMap.dcMotor.get("RightFront");
            motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
            motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
        }

        @Override
        public void loop() {
            motorBackLeft.setPower((gamepad1.left_stick_y-gamepad1.left_stick_x)/2);
            motorFrontLeft.setPower((gamepad1.left_stick_y+gamepad1.left_stick_x)/2);
            motorBackRight.setPower((gamepad1.left_stick_y+gamepad1.left_stick_x)/2);
            motorFrontRight.setPower((gamepad1.left_stick_y-gamepad1.left_stick_x)/2);
        }
    }

