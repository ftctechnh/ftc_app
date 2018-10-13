package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by jodasue on 10/28/17.
 */
@TeleOp(name = "fauxbotDrive", group = "Testing")
public class fauxbotDrive extends LinearOpMode {

    DcMotor left;
    DcMotor right;

    public void runOpMode() {

        left = hardwareMap.dcMotor.get("left");
        left.setDirection(DcMotorSimple.Direction.FORWARD);
        right = hardwareMap.dcMotor.get("right");
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        double temp = 0;

        waitForStart();
        while (opModeIsActive()) {


            left.setPower(gamepad1.left_stick_y);
            right.setPower(gamepad1.right_stick_y);

            telemetry.update();
            idle();
        }
    }
}
