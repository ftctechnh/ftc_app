package org.firstinspires.ftc.teamcode.SubAssembly.Sensors;

/**
 * Created by Nicholas on 8/29/2018.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.SubAssembly.Sample.SampleTemplate;
import org.firstinspires.ftc.teamcode.Utilities.EnumWrapper;
import org.firstinspires.ftc.teamcode.Utilities.ServoControl;

import java.util.EnumMap;

/* Sub Assembly Class
 */
public class SensorsControl {
    /* Declare private class object */
    private Telemetry telemetry = null;         /* local copy of telemetry object from opmode class */
    private HardwareMap hardwareMap = null;     /* local copy of HardwareMap object from opmode class */
    private String name = "Sensors";

    /* Configure sensors */
    private ColorSensor ColorSensor = null;
    private TouchSensor TouchSensor = null;

    /* Create sensor variables */
    public boolean IsRed = false;
    public boolean IsBlue = false;
    public boolean IsPressed = false;
    public double Voltage = 0.0;

    /* Declare public class object */
    public VoltageSensor Battery = null;


    /* Subassembly constructor */
    public SensorsControl(LinearOpMode opMode) {
        /* Set local copies from opmode class */
        telemetry = opMode.telemetry;
        hardwareMap = opMode.hardwareMap;

        telemetry.addLine(name + " initialize");

        /* Map hardware devices */
        Battery = hardwareMap.voltageSensor.get("Lower hub 3");
        ColorSensor = hardwareMap.colorSensor.get("ColorSensor");
        TouchSensor = hardwareMap.touchSensor.get("TouchSensor");

        Voltage = Battery.getVoltage();

        /* Checking color sensor values */
        if (ColorSensor.red() > 10) {
            IsRed = true;
        }
        else {
            IsRed = false;
        }
        //now for blue
        if (ColorSensor.blue() > 10) {
            IsBlue = true;
        }
        else {
            IsBlue = false;
        }

        /* Checking if button is pressed */
        if (TouchSensor.isPressed()) {
            IsPressed = true;
        }
        else {
            IsPressed = false;
        }

    }

    /* Subassembly methods */

    /* Displays color sensor red value readings */
    public void DisplayRed() {
        telemetry.addData("Red value = ", ColorSensor.red());
        telemetry.addData("Red = ", IsRed);
    }


    /* Displays color sensor blue value readings */
    public void DisplayBlue() {
        telemetry.addData("Blue value = ", ColorSensor.blue());
        telemetry.addData("Blue = ", IsBlue);
    }


    /* Displays touch sensor readings */
    public void DisplayPressed() {
        telemetry.addData("Touch = ", IsPressed);
    }


    /* Displays the battery level */
    public void DisplayBattery() {
        telemetry.addData("Battery level = ", "%.2f V", Voltage);

        /* Tells you if the battery is low */
        if (Voltage < 10.0) {
            telemetry.addLine("Low Battery..: PLZZZ REPLACE BATTERY NOW!!! >:|");
        }

    }

    /* Displays all sensor values */
    public void DisplaySensors() {
        /* Displays color sensor red value readings */
        telemetry.addData("Red value = ", ColorSensor.red());
        telemetry.addData("Red = ", IsRed);

        /* Displays color sensor blue value readings */
        telemetry.addData("Blue value = ", ColorSensor.blue());
        telemetry.addData("Blue = ", IsBlue);

        /* Displays touch sensor readings */
        telemetry.addData("Touch = ", IsPressed);

        /* Displays the battery level */
        telemetry.addData("Battery level = ", "%.2f V", Voltage);

        /* Tells you if the battery is low */
        if (Voltage < 10.0) {
            telemetry.addLine("Low Battery..: PLZZZ REPLACE BATTERY NOW!!! >:|");
        }
    }

}
