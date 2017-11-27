package org.firstinspires.ftc.teamcode.GMR.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;

/**
 * Created by pston on 11/18/2017
 */

@TeleOp(name = "Relic Recovery TeleOp V1", group = "test")
public class RR_TeleOp_V1 extends OpMode {

    private int currentLiftPosition;

    private Robot robot;
    private double topLeftPosition = 0.37; //Open: 0.37, Close: 0
    private double topRightPosition = 0.26; //Open: 0.26, Close: 0.65
    private double bottomLeftPosition = 0.55; //Open: 0.55, Close: 0.117
    private double bottomRightPosition = 0.2; //Open: 0.2, Close: 0.67

    private boolean letterButton = false;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
    }

    @Override
    public void loop() {

        if (gamepad1.a) {
            bottomRightPosition = 0.2;
            bottomLeftPosition = 0.55;
        } else if (gamepad1.x) {
            bottomRightPosition = 0.67;
            bottomLeftPosition = 0.117;
        }

        if (gamepad1.y) {
            topLeftPosition = 0.37;
            topRightPosition = 0.26;
        } else if (gamepad1.b) {
            topLeftPosition = 0;
            topRightPosition = 0.65;
        }

        if (gamepad1.right_bumper && robot.blockLift.liftMotor.getCurrentPosition() < 0) {
            robot.blockLift.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.blockLift.liftMotor.setPower(1);
            currentLiftPosition = robot.blockLift.liftMotor.getCurrentPosition();
        } else if (gamepad1.right_trigger > 0) {
            robot.blockLift.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.blockLift.liftMotor.setPower(-1);
            currentLiftPosition = robot.blockLift.liftMotor.getCurrentPosition();
        } else {
            robot.blockLift.liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.blockLift.liftMotor.setTargetPosition(currentLiftPosition);
        }

        robot.driveTrain.setMotorPower(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

        telemetry.addData("Current Lift Motor Power: ", robot.blockLift.liftMotor.getPower());
        telemetry.addData("Current Lift Goal: ", currentLiftPosition);

        robot.blockLift.topLeftGrab.setPosition(topLeftPosition);
        robot.blockLift.topRightGrab.setPosition(topRightPosition);
        robot.blockLift.bottomLeftGrab.setPosition(bottomLeftPosition);
        robot.blockLift.bottomRightGrab.setPosition(bottomRightPosition);

        robot.blockLift.currentServoPositions(telemetry);
    }

}
