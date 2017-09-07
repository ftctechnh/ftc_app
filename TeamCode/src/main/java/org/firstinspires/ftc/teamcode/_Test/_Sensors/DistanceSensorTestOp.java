package org.firstinspires.ftc.teamcode._Test._Sensors;

import android.view.View;

import com.qualcomm.hardware.hitechnic.HiTechnicNxtUltrasonicSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


/**
 * Created by phanau on 11/25/16.
 * Test NXT ultrasonic sensor hardware
 * this device must be attached to port 4 or 5 of the CORE LEGACY DEVICE module
 */
@Autonomous(name="Test: Distance Sensor Test 1", group ="Test")
//@Disabled
public class DistanceSensorTestOp extends OpMode {

    private UltrasonicSensor mDistanceSensor;

    public DistanceSensorTestOp() {
    }

    public void init() {
        // get ultrasonic distance sensor
        mDistanceSensor = hardwareMap.ultrasonicSensor.get("ds");
    }

    public void loop() {
        // log data to DriverStation
        telemetry.addData("distance: ", mDistanceSensor.getUltrasonicLevel());
    }

    public void stop() {}

}
