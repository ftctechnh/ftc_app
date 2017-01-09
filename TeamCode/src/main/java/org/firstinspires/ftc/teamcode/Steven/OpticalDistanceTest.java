package org.firstinspires.ftc.teamcode.Steven;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by Steven on 1/5/2017.
 */
@TeleOp(name = "OpticalDistanceTest", group = "Pushbot")
//@Disabled
public class OpticalDistanceTest extends OpMode{
    OpticalDistanceSensor ODS;
    @Override
    public void init() {
        ODS = hardwareMap.opticalDistanceSensor.get("ODS");
    }

    @Override
    public void start(){

    }

    @Override
    public void loop(){

        telemetry.addData("LightDetected", ODS.getLightDetected());
        telemetry.addData("RawLightDetected",ODS.getRawLightDetected());
        telemetry.addData("RawLightDetectedMax",ODS.getRawLightDetectedMax());
        telemetry.update();
    }
}
