package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Stephen Ogden on 10/15/17.
 * FTC 6128 | 7935
 * FRC 1595
 */
@TeleOp(name = "color sensor demo", group = "Test")
@Disabled
public class colorSensorTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initializing...");
        telemetry.update();
        ColorSensor colorSensor = hardwareMap.colorSensor.get("color");
        telemetry.addData("Status", "Done.");
        telemetry.update();
        waitForStart();
        while(opModeIsActive()) {
            telemetry.addData("Jewel color", getColor(colorSensor));
            telemetry.addData("","");
            telemetry.addData("Red value", colorSensor.red());
            telemetry.addData("Blue value", colorSensor.blue());
            telemetry.update();
        }
    }

    private String getColor(ColorSensor color) {
        String colorValue;
        color.enableLed(true);
        if (color.red() > color.blue()) {
            colorValue = "Red";
        } else if (color.red() < color.blue()) {
            colorValue = "Blue";
        } else {
            colorValue = null;
        }
        return colorValue;
    }
}
