package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by djfigs1 on 10/7/16.
 */

@TeleOp(name="Sensor Test", group="Manual")
public class RobotSensorTelemetry extends RobotTelemetry {

    @Override
    public void loop() {
        //Color Sensors
        telemetry.addData("Left Color Sensor", String.format("R: %s, G: %s, B: %s", leftColorSensor.red(), leftColorSensor.green(), leftColorSensor.blue()));
        telemetry.addData("Right Color Sensor", String.format("R: %s, G: %s, B: %s", rightColorSensor.red(), rightColorSensor.green(), rightColorSensor.blue()));
        telemetry.addData("Beacon Color Sensor", String.format("R: %s, G: %s, B: %s", beaconColorSensor.red(), beaconColorSensor.green(), beaconColorSensor.blue()));

        telemetry.addData("Gyro Sensor", String.format("HEAD: %s, X: %s, Y: %s, Z: %s", gyroSensor.getHeading(), gyroSensor.rawX(), gyroSensor.rawY(), gyroSensor.rawZ()));
        telemetry.addData("Compass Sensor", String.format("Direction: %s", compassSensor.getDirection()));
        telemetry.addData("Range Sensor", String.format("US: %s, OPT: %s", rangeSensor.cmUltrasonic(), rangeSensor.cmOptical()));
    }

}
