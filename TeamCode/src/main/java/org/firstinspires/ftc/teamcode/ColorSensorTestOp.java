package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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
    ColorSensor leftWingColorSens;
    ColorSensor rightWingColorSens;
    //DeviceInterfaceModule cdim;
    boolean bPrevState;
    boolean bCurrState;
    boolean bLedOn;
    float hsvValues[] = {0F,0F,0F};
    float hsvValues_Ada[] = {0F,0F,0F};
    NewRobotFinal newRobot;

    // final float values[] = hsvValues;

    // we assume that the LED pin of the RGB sensor is connected to
    // digital port 5 (zero indexed).
    //static final int LED_CHANNEL = 5;

    public void init()
    {
        gamepad2.setJoystickDeadzone(.3f);//attachments
        gamepad1.setJoystickDeadzone(.3f);//driver
        newRobot = new NewRobotFinal(hardwareMap);
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
        leftWingColorSens = hardwareMap.colorSensor.get("leftWingColorSens");
        rightWingColorSens = hardwareMap.colorSensor.get("rightWingColorSens");
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
        Color.RGBToHSV((leftWingColorSens.red() * 255) / 800, (leftWingColorSens.green() * 255) / 800, (leftWingColorSens.blue() * 255) / 800, hsvValues);
        Color.RGBToHSV((rightWingColorSens.red() * 255) / 800, (rightWingColorSens.green() * 255) / 800, (rightWingColorSens.blue() * 255) / 800, hsvValues_Ada);

        // send the info back to driver station using telemetry function.

        telemetry.addData("right" , null);
        telemetry.addData("RWC = ", newRobot.getColor(rightWingColorSens));
        telemetry.addData("Hue ", hsvValues_Ada[0]);
        telemetry.addData("Saturation ", hsvValues_Ada[1]);
        telemetry.addData("Value ", hsvValues_Ada[2]);

        telemetry.addData("left" , null);
        telemetry.addData("LWC = ", newRobot.getColor(leftWingColorSens));
        telemetry.addData("Hue " ,hsvValues[0]);
        telemetry.addData("Saturation ", hsvValues[1]);
        telemetry.addData("Value ", hsvValues[2]);

        // telemetry.addData("Hue= ", hsvValues[0]);
        telemetry.update();
        if (gamepad1.y)
            newRobot.getWingMotor().setPower(.9f);//lift wing
        else if (gamepad1.b)
            newRobot.getWingMotor().setPower(-.9f);
        else
            newRobot.getWingMotor().setPower(0f);
    }
}
