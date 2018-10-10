package org.firstinspires.ftc.teamcode.Sensors;

/**
 * Created by Nicholas on 8/29/2018.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/* Sub Assembly Class
 */
public class SensorsTemplate {
    /* Declare private class object */
    private Telemetry telemetry = null;         /* local copy of telemetry object from opmode class */
    private HardwareMap hardwareMap = null;     /* local copy of HardwareMap object from opmode class */
    private String name = "Sensors";

    /* Configure sensors */
    private ColorSensor ColorSensor = null;
    private TouchSensor TouchSensor = null;

    /* Create sensor variables */
    private boolean ColorSensorRed = false;
    private boolean ColorSensorBlue = false;
    private boolean TouchSensorPressed = false;

    /* Declare public class object */
    public VoltageSensor Battery = null;


    /* Subassembly constructor */
    public SensorsTemplate(LinearOpMode opMode) {
        /* Set local copies from opmode class */
        telemetry = opMode.telemetry;
        hardwareMap = opMode.hardwareMap;

        telemetry.addLine(name + " initialize");

        /* Map hardware devices */
        Battery = hardwareMap.voltageSensor.get("Lower hub 3");
        ColorSensor = hardwareMap.colorSensor.get("ColorSensor");
        TouchSensor = hardwareMap.touchSensor.get("TouchSensor");

        /* Checking color sensor values */
        if (ColorSensor.red() > 10) {
            ColorSensorRed = true;
        }
        else {
            ColorSensorRed = false;
        }
        //now for blue
        if (ColorSensor.blue() > 10) {
            ColorSensorBlue = true;
        }
        else {
            ColorSensorBlue = false;
        }

        /* Checking if button is pressed */
        if (TouchSensor.isPressed()) {
            TouchSensorPressed = true;
        }
        else {
            ColorSensorRed = false;
        }

    }

    /* Subassembly methods */
    /* Checks if color sensor sees red */
    public boolean IsRed() {
        telemetry.addLine(name + " test");
        telemetry.addData("Red value = ", ColorSensor.red());
        telemetry.addData("Red = ", ColorSensorRed);

        /* Checking if color sensor reads red */
        if (ColorSensorRed) {
            return true;
        }
        else {
            return false;
        }

    }


    /* Displays color sensor blue values */
    public boolean IsBlue() {
        telemetry.addLine(name + " test");
        telemetry.addData("Blue value = ", ColorSensor.blue());
        telemetry.addData("ColorSensorBlue = ", ColorSensorBlue);
    }


    /* Checks if touch sensor is pressed */
    public boolean IsPressed() {
        telemetry.addLine(name + " test");
        telemetry.addData("Touch = ", TouchSensorPressed);

        /* Checking touch sensor */
        if (TouchSensorPressed) {
            return true;
        }
        else {
            return false;
        }

    }


    /* Displays the battery level */
    public void BatteryLevel() {
        telemetry.addLine(name + " test");
        telemetry.addData("Battery level = ", "%.2f V", Battery.getVoltage());
    }

}
