package org.firstinspires.ftc.teamcode._Test._Sensors;

import android.app.Activity;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.LightSensor;


/**
 * Created by phanau on 11/25/16.
 * Test light sensor hardware
 */
@Autonomous(name="Test: Light Sensor Test 1", group ="Test")
//@Disabled
public class LightSensorTestOp extends OpMode {

    private LightSensor mLightSensor;
    private View mRelativeLayout;

    public LightSensorTestOp() {
    }

    public void init() {
        // get hardware color sensor - should support either MR or NXT color sensor ...
        mLightSensor = (LightSensor) hardwareMap.lightSensor.get("ls");
    }

    public void loop() {
        // test LED: illuminate it while button X is pressed
        mLightSensor.enableLed(gamepad1.x);

        // log data to DriverStation
        telemetry.addData("light: ", mLightSensor.getLightDetected());
        telemetry.addData("raw: ", mLightSensor.getRawLightDetected());
        telemetry.addData("max: ", mLightSensor.getRawLightDetectedMax());
    }

    public void stop() {}

}
