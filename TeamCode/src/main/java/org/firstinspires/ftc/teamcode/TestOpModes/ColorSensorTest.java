package org.firstinspires.ftc.teamcode.TestOpModes;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;

/**
 * Created by FTC 4316 on 3/17/2018
 */

public class ColorSensorTest extends OpMode {

    private ColorSensor colorSensor;

    @Override
    public void init() {

        colorSensor = hardwareMap.get(ColorSensor.class, "color");
    }

    @Override
    public void loop() {
        telemetry.addData("Red:", colorSensor.red());
        telemetry.addData("Blue:", colorSensor.blue());
        telemetry.addData("Green:", colorSensor.green());
        telemetry.update();
    }
}