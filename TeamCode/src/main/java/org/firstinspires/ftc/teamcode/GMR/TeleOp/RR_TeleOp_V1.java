package org.firstinspires.ftc.teamcode.GMR.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;

/**
 * Created by pston on 11/18/2017
 */

@TeleOp(name = "Servo Test", group = "test")
public class RR_TeleOp_V1 extends OpMode {

    private Robot robot;
    private double topLeftPosition = 0.5;
    private double topRightPosition = 0.5;
    private double bottomLeftPosition = 0.55;
    private double bottomRightPosition = 0.2;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
    }

    @Override
    public void loop() {

        if (gamepad1.a) {
            topLeftPosition += 0.0008;
        } else if (gamepad1.dpad_down) {
            topLeftPosition -= 0.0008;
        }

        if (gamepad1.b) {
            topRightPosition += 0.0008;
        } else if (gamepad1.dpad_right) {
            topRightPosition -= 0.0008;
        }

        if (gamepad1.y) {
            bottomLeftPosition = 0.55;
            bottomRightPosition = 0.2;
        } else if (gamepad1.x) {
            bottomLeftPosition = 0.117;
            bottomRightPosition = 0.67;
        }

        robot.blockLift.topLeftGrab.setPosition(topLeftPosition);
        robot.blockLift.topRightGrab.setPosition(topRightPosition);
        robot.blockLift.bottomLeftGrab.setPosition(bottomLeftPosition);
        robot.blockLift.bottomRightGrab.setPosition(bottomRightPosition);

        robot.blockLift.currentServoPositions(telemetry);
    }

}
