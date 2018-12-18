package org.firstinspires.ftc.teamcode.testStuff.oldteststuff;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@TeleOp(name = "Sensor: Touch Sensor 1a", group = "Sensor")

public class TestbotTouchSensor extends LinearOpMode {

    DigitalChannel touchSensor;  // Hardware Device Object

    @Override
    public void runOpMode() {

        touchSensor = hardwareMap.digitalChannel.get("touch_sensor");

        touchSensor.setMode(DigitalChannel.Mode.INPUT);

        waitForStart();


        while (opModeIsActive()) {
            telemetry.addData("Touch Sensor", touchSensor.getState());
            telemetry.update();
        }
    }
}
