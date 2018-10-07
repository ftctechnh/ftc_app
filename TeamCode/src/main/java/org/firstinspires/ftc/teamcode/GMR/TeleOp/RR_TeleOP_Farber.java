package org.firstinspires.ftc.teamcode.GMR.TeleOp;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;

/**
 * Created by pston on 12/14/2017
 */

@TeleOp(name = "Relic Recovery TeleOp (Farber)", group = "test")
public class RR_TeleOP_Farber extends OpMode {

    private Robot robot;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, false);
        robot.setRelicTilt();
        robot.relicClamp.setPosition(1);
    }

    @Override
    public void loop() {
        robot.blockLift.grab(gamepad1.right_bumper, gamepad1.right_trigger);
        robot.blockLift.slideHeight(gamepad1.left_bumper, gamepad1.left_trigger, gamepad1.a, gamepad1.b, gamepad1.y, telemetry);
        if (gamepad2.left_stick_x == 0 && gamepad2.left_stick_y == 0 && gamepad2.right_stick_x == 0) {
            robot.driveTrain.setMotorPower(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        } else {
            robot.driveTrain.setMotorPowerSlow(gamepad2.left_stick_x, -gamepad2.left_stick_y, gamepad2.right_stick_x);
        }

        robot.relicGrab.relicGrab(gamepad2.left_bumper, gamepad2.left_trigger, gamepad2.y, gamepad2.a, gamepad2.right_bumper, gamepad2.right_trigger, gamepad2.b, telemetry, gamepad2.x);
        robot.relicGrab.servoInfo(telemetry);
        robot.setServos();
    }
}
