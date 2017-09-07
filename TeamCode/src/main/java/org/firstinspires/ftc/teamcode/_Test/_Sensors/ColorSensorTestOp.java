package org.firstinspires.ftc.teamcode._Test._Sensors;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorHTColor;


/**
 * Created by phanau on 11/22/16.
 * Test color sensor hardware
 */
@Autonomous(name="Test: Color Sensor Test 1", group ="Test")
//@Disabled
public class ColorSensorTestOp extends OpMode {

    private ColorSensor mColorSensor;

    public ColorSensorTestOp() {
    }

    public void init() {
        // get hardware color sensor - should support either MR or NXT color sensor ...
        mColorSensor = (ColorSensor) hardwareMap.colorSensor.get("cs");
    }

    public void loop() {
        // test LED: illuminate it while button X is pressed
        mColorSensor.enableLed(gamepad1.x);

        // log data to DriverStation
        telemetry.addData("alpha: ", mColorSensor.alpha());
        telemetry.addData("red: ", mColorSensor.red());
        telemetry.addData("green: ", mColorSensor.green());
        telemetry.addData("blue: ", mColorSensor.blue());
    }

    public void stop() {}

}
