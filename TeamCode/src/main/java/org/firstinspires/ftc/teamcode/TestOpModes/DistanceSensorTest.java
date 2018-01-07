package org.firstinspires.ftc.teamcode.TestOpModes;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;

/**
 * Created by pston on 1/3/2018
 */

@TeleOp(name = "Range Sensor Test", group = "Sensor Test")
public class DistanceSensorTest extends OpMode {

    private Robot robot;

    ModernRoboticsI2cRangeSensor rangeSensor;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);

        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "range");
    }

    @Override
    public void loop() {
        telemetry.addData("raw ultrasonic", rangeSensor.rawUltrasonic());
        telemetry.addData("raw optical", rangeSensor.rawOptical());
        telemetry.addData("cm", "%.2f cm", Math.round(rangeSensor.getDistance(DistanceUnit.CM) * 100.0) / 100.0);
        telemetry.update();
    }
}
