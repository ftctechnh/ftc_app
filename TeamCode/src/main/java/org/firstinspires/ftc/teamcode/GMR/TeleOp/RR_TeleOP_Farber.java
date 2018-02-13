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

    ModernRoboticsI2cRangeSensor rangeSensor;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, false);

        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "range");
    }

    @Override
    public void loop() {

        robot.blockLift.grab(gamepad1.right_bumper, gamepad1.right_trigger);
        robot.blockLift.lift(gamepad1.left_bumper, gamepad1.left_trigger, telemetry);
        //robot.blockLift.slideHeight(gamepad1.a, gamepad1.b, gamepad1.y, telemetry);
        robot.driveTrain.setMotorPower(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

        robot.relicGrab.relicGrab(gamepad2.left_bumper, gamepad2.left_trigger, gamepad2.dpad_up, gamepad2.dpad_down, gamepad2.y, gamepad2.a, gamepad2.right_bumper, gamepad2.right_trigger, gamepad2.b);
        robot.relicGrab.servoInfo(telemetry);
        robot.setServos();
    }

}
