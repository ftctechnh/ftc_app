package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;

/**
 * Created by Peter on 11/6/2016.
 */

@TeleOp(name = "ColorSensTest", group = "Test")
public class AdafruitColorTest extends OpMode
{
  //  private OmniDriveBot robot = new OmniDriveBot();
    //ColorSensor sensorRGB;
    ColorSensor beaconColorSensor;
    DeviceInterfaceModule cdim;
    boolean bPrevState;
    boolean bCurrState;
    boolean bLedOn;
    float hsvValues[] = {0F,0F,0F};
    final float values[] = hsvValues;

    // we assume that the LED pin of the RGB sensor is connected to
    // digital port 5 (zero indexed).
    static final int LED_CHANNEL = 5;

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
        bLedOn = true;

        // get a reference to our DeviceInterfaceModule object.
        cdim = hardwareMap.deviceInterfaceModule.get("dim");

        // set the digital channel to output mode.
        // remember, the Adafruit sensor is actually two devices.
        // It's an I2C sensor and it's also an LED that can be turned on or off.
        cdim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);

        // get a reference to our ColorSensor object.
        beaconColorSensor = hardwareMap.colorSensor.get("beaconColorSensor");
        // turn the LED on in the beginning, just so user will know that the sensor is active.
        cdim.setDigitalChannelState(LED_CHANNEL, bLedOn);

    }

    public void loop()
    {
        // check the status of the x button on gamepad.
        bCurrState = gamepad1.x;
        // check for button-press state transitions.
        if ((bCurrState == true) && (bCurrState != bPrevState))
        {
            // button is transitioning to a pressed state. Toggle the LED.
            bLedOn = !bLedOn;
            cdim.setDigitalChannelState(LED_CHANNEL, bLedOn);
        }

        // update previous state variable.
        bPrevState = bCurrState;

        // convert the RGB values to HSV values.
        Color.RGBToHSV((beaconColorSensor.red() * 255) / 800, (beaconColorSensor.green() * 255) / 800, (beaconColorSensor.blue() * 255) / 800, hsvValues);
        // send the info back to driver station using telemetry function.

        telemetry.addData("beaconColorSensor" , null);
        telemetry.addData("LED", bLedOn ? "On" : "Off");
        telemetry.addData("Clear", beaconColorSensor.alpha());
        telemetry.addData("Red  ", beaconColorSensor.red());
        telemetry.addData("Green", beaconColorSensor.green());
        telemetry.addData("Blue ", beaconColorSensor.blue());
        telemetry.addData("Hue", hsvValues[0]);
        telemetry.update();
    }
}

