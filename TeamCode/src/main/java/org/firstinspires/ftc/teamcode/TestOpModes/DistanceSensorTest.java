package org.firstinspires.ftc.teamcode.TestOpModes;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;

/**
 * Created by pston on 1/3/2018
 */

public class DistanceSensorTest extends OpMode {

    private Robot robot;

    private ModernRoboticsI2cRangeSensor rangeSensor;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, false);

        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "range");

    }

    @Override
    public void loop() {
        telemetry.addData("Color Alpha", rangeSensor.rawUltrasonic());
        telemetry.addData("Color ARGB", rangeSensor.rawOptical());
        telemetry.addData("Blue", rangeSensor.getDistance(DistanceUnit.CM));
        telemetry.update();
        robot.driveTrain.setMotorPower(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
    }
}
