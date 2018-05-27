package org.firstinspires.ftc.teamcode.TestOpModes;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.DriveTrain;

/**
 * Created by pston on 1/7/2018
 */

public class SideStrafe extends OpMode {

    private Robot robot;

    ModernRoboticsI2cRangeSensor rangeSensor;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, false);

        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "range");
    }

    @Override
    public void loop() {

        robot.blockLift.lift(gamepad1.right_bumper, gamepad1.right_trigger, telemetry);

        robot.relicGrab.relicGrab(gamepad2.left_bumper, gamepad2.left_trigger, gamepad2.y, gamepad2.a, gamepad2.right_bumper, gamepad2.right_trigger, gamepad2.b, telemetry, gamepad2.x);
        robot.relicGrab.servoInfo(telemetry);
        robot.setServos();
        telemetry.addData("Raw Optical", rangeSensor.rawOptical());
        telemetry.addData("Raw Ultrasonic", rangeSensor.rawUltrasonic());
        telemetry.addData("Inch", "%.2f inch", rangeSensor.getDistance(DistanceUnit.INCH));
        telemetry.update();

        if (gamepad1.dpad_left) {
            robot.driveTrain.drive(DriveTrain.Direction.W, 0.3);
        } else if (gamepad1.dpad_right) {
            robot.driveTrain.drive(DriveTrain.Direction.E, 0.3);
        } else {
            robot.driveTrain.setMotorPower(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
        }

    }
}
