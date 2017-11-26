package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

/**
 * Created by Jeremy on 9/20/2017.
 */
@TeleOp(name = "ColorSensorTest", group = "Test")
public class ColorSensorTestOp extends OpMode
{
    //  private OmniDriveBot robot = new OmniDriveBot();
    //ColorSensor sensorRGB;
    ColorSensor revColorSens;
    ColorSensor adaFruitSens;
    //DeviceInterfaceModule cdim;
    boolean bPrevState;
    boolean bCurrState;
    boolean bLedOn;
    float hsvValues[] = {0F,0F,0F};
    float hsvValues_Ada[] = {0F,0F,0F};


    // final float values[] = hsvValues;

    // we assume that the LED pin of the RGB sensor is connected to
    // digital port 5 (zero indexed).
    //static final int LED_CHANNEL = 5;

    public void init()
    {
//        robot.init(hardwareMap);

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.RelativeLayout);

        // bPrevState and bCurrState represent the previous and current state of the button.
        bPrevState = false;
        bCurrState = false;

        // bLedOn represents the state of the LED.
        bLedOn = false;

        // get a reference to our DeviceInterfaceModule object.
        //cdim = hardwareMap.deviceInterfaceModule.get("dim");

        // set the digital channel to output mode.
        // remember, the Adafruit sensor is actually two devices.
        // It's an I2C sensor and it's also an LED that can be turned on or off.
        //  cdim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);

        // get a reference to our ColorSensor object.
        revColorSens = hardwareMap.colorSensor.get("revSensor");
        adaFruitSens = hardwareMap.colorSensor.get("adaFruitSensor");
        // turn the LED on in the beginning, just so user will know that the sensor is active.
        //    cdim.setDigitalChannelState(LED_CHANNEL, bLedOn);
    }

    public void loop()
    {
        // check the status of the x button on gamepad.
        //bCurrState = gamepad1.x;
        // check for button-press state transitions.

        // update previous state variable.
        // bPrevState = bCurrState;

        // convert the RGB values to HSV values.
        Color.RGBToHSV((revColorSens.red() * 255) / 800, (revColorSens.green() * 255) / 800, (revColorSens.blue() * 255) / 800, hsvValues);
        Color.RGBToHSV((adaFruitSens.red() * 255) / 800, (adaFruitSens.green() * 255) / 800, (adaFruitSens.blue() * 255) / 800, hsvValues_Ada);

        // send the info back to driver station using telemetry function.

        telemetry.addData("Ada" , null);
        telemetry.addData("Hue ", hsvValues_Ada[0]);
        telemetry.addData("Saturation ", hsvValues_Ada[1]);
        telemetry.addData("Value ", hsvValues_Ada[2]);

        telemetry.addData("REV" , null);
        telemetry.addData("Hue " ,hsvValues[0]);
        telemetry.addData("Saturation ", hsvValues[1]);
        telemetry.addData("Value ", hsvValues[2]);

        // telemetry.addData("Hue= ", hsvValues[0]);
        telemetry.update();
    }
}
