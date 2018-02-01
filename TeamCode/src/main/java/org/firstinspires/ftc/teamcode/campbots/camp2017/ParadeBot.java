package org.firstinspires.ftc.teamcode.campbots.camp2017;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by ftc6347 on 6/27/17.
 */
@Disabled
@TeleOp(name = "Parade Robot RobotTeleOp", group = "demos")
public class ParadeBot extends OpMode {

    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;
    private DcMotor frontLeft;
    private int trackA = 0;
    private ElapsedTime timer = new ElapsedTime();
    @Override
    public void init() {
        // init the Wheels
        frontRight = hardwareMap.dcMotor.get("fr");
        backRight = hardwareMap.dcMotor.get("br");
        frontLeft = hardwareMap.dcMotor.get("fl");
        backLeft = hardwareMap.dcMotor.get("bl");


        // set wheel direction
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

// set deadzone
        gamepad1.setJoystickDeadzone(0.1f);
    }

    @Override
    public void loop() {


        if (gamepad1.a && timer.milliseconds() > 500) {
            timer.reset();
            if (trackA == 0) {
                trackA = trackA + 1;
            } else {
                trackA--;
            }
        }
        if (trackA == 0) {
            frontLeft.setPower(gamepad1.left_stick_y);
            frontRight.setPower(gamepad1.right_stick_y);
            backLeft.setPower(gamepad1.left_stick_y);
            backRight.setPower(gamepad1.right_stick_y);
        }
        if (trackA == 1) {
            double steer = gamepad1.right_stick_x;
            frontLeft.setPower((.8*(gamepad1.left_stick_y)) - steer);
            frontRight.setPower((.8*(gamepad1.left_stick_y)) + steer);
            backLeft.setPower((.8*(gamepad1.left_stick_y)) - steer);
            backRight.setPower((.8*(gamepad1.left_stick_y)) + steer);
        }


        if (gamepad1.left_trigger > 0.5) {
            frontLeft.setPower(gamepad1.left_trigger);
            backLeft.setPower(-gamepad1.left_trigger);
            frontRight.setPower(-gamepad1.left_trigger);
            backRight.setPower(gamepad1.left_trigger);
        }

        if (gamepad1.right_trigger > 0.5) {
            frontLeft.setPower(-gamepad1.right_trigger);
            backLeft.setPower(gamepad1.right_trigger);
            frontRight.setPower(gamepad1.right_trigger);
            backRight.setPower(-gamepad1.right_trigger);
        }
    }
}
