//auhto: Jose
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;



@Autonomous

public class TouchSensorOpMode extends LinearOpMode {

    private TouchSensor ts;

    private boolean pressed;
    private double val;

    @Override
    public void runOpMode() {

        ts = hardwareMap.get(TouchSensor.class,"Touch_Sensor");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            pressed = ts.isPressed();
            val = ts.getValue();

            String s = "Pressed" + pressed + "\nValue" + val;
            telemetry.addData("Status:",s);
            telemetry.update();

        }
    }
}

