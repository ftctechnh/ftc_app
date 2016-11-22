package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by ftc6347 on 9/26/16.
 */
@TeleOp(name = "Sensor test", group = "tests")
public class LegacySensorTest extends OpMode {

    private LegacyModule legacyModule;

    private LightSensor lightSensor;
    private UltrasonicSensor ultrasonicSensor;

    @Override
    public void init() {
        legacyModule = hardwareMap.legacyModule.get("lm");

        lightSensor = hardwareMap.lightSensor.get("ls");
        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("us");

        lightSensor.enableLed(true);
    }

    @Override
    public void loop() {
        double reflectedLight = lightSensor.getLightDetected();

        telemetry.addLine("Light sensor")
                .addData("Value: ", reflectedLight);

        double ultraSonicValue = ultrasonicSensor.getUltrasonicLevel();

        telemetry.addLine("Ultrasonic sensor")
                .addData("Value: ", ultraSonicValue);
    }
}
