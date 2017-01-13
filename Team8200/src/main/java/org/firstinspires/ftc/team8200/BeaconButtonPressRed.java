package org.firstinspires.ftc.team8200;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;



/**
 * Created by student on 1/12/17.
 */

@Autonomous(name="8200: Red Button Press", group="K9bot")

public class BeaconButtonPressRed extends LinearOpMode {
    LightSensor lightSensor;
    ColorSensor sensorRGB;
    DeviceInterfaceModule cdim;


    HardwareK9bot robot = new HardwareK9bot(); // Hardware Device Object
    private ElapsedTime runtime = new ElapsedTime();

    static final double WHITE_THRESHOLD = 0.4;
    static final double MOTOR_POWER = .1;
    static final int LED_CHANNEL = 5;



    @Override

    public void runOpMode() {

        robot.init(hardwareMap);
        cdim = hardwareMap.deviceInterfaceModule.get("dim");
        lightSensor = robot.lightSensor;
        //colorSensor = robot.colorSensor;


//        // bLedOn represents the state of the LED.
        bLedOn = true;

//        // get a reference to our Light Sensor object.
        lightSensor = hardwareMap.lightSensor.get("light");

//        // Set the LED state in the beginning.
        lightSensor.enableLed(bLedOn);
        cdim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);

        // get a reference to our ColorSensor object.
        sensorRGB = hardwareMap.colorSensor.get("sensor_color");

        // turn the LED on in the beginning, just so user will know that the sensor is active.
        cdim.setDigitalChannelState(LED_CHANNEL, bLedOn);

        // wait for the start button to be pressed.

        // loop and read the RGB data.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
        while (opModeIsActive())  {

            // check the status of the x button on gamepad.
            bCurrState = gamepad1.x;

            // check for button-press state transitions.
            if ((bCurrState == true) && (bCurrState != bPrevState))  {

                // button is transitioning to a pressed state. Toggle the LED.
                // bLedOn = !bLedOn;
                cdim.setDigitalChannelState(LED_CHANNEL, bLedOn);
            }

            // update previous state variable.
            bPrevState = bCurrState;

            // convert the RGB values to HSV values.
            Color.RGBToHSV((sensorRGB.red() * 255) / 800, (sensorRGB.green() * 255) / 800, (sensorRGB.blue() * 255) / 800, hsvValues);

            // send the info back to driver station using telemetry function.
            telemetry.addData("LED", bLedOn ? "On" : "Off");
            telemetry.addData("Clear", sensorRGB.alpha());
            telemetry.addData("Red  ", sensorRGB.red());
            telemetry.addData("Green", sensorRGB.green());
            telemetry.addData("Blue ", sensorRGB.blue());
            telemetry.addData("Hue", hsvValues[0]);

            // change the background color to match the color detected by the RGB sensor.
            // pass a reference to the hue, saturation, and value array as an argument
            // to the HSVToColor method.
            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
                }
            });

//        // wait for the start button to be pressed.
        waitForStart();

        MoveToBeacon();
    }

    public void MoveToBeacon() {
        // Move to white line
        while (lightSensor.getLightDetected() < WHITE_THRESHOLD) {
            robot.leftMotor.setPower(MOTOR_POWER);
            robot.rightMotor.setPower(MOTOR_POWER);
            updateTelemetry();
        }

        //step 2
        robot.rightMotor.setPower(0);
        robot.leftMotor.setPower(0);

        telemetry.addData("Say", "Motors just stopped.");
        telemetry.update();


        while (opModeIsActive()) { //while the touch sensor is not touching the wall (or proximity sensor is not touching wall)
            // step 3 turning for ___ seconds
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 0.01)) {
                robot.leftMotor.setPower(0);
                robot.rightMotor.setPower(MOTOR_POWER);
                telemetry.addData("Raw", lightSensor.getRawLightDetected());
                telemetry.addData("Say", "Motors turning.");
                telemetry.update();
            }

            //step 4 follow the line
            while (lightSensor.getLightDetected() >= WHITE_THRESHOLD) { //follows white light is above threshold AND touch sensor is not touching
                robot.leftMotor.setPower(MOTOR_POWER);
                robot.rightMotor.setPower(MOTOR_POWER);
                telemetry.addData("Say", "Motors following line.");
                telemetry.update();
            }

        }
    }

    public void updateTelemetry() {

        telemetry.addData("Raw", lightSensor.getRawLightDetected());
        telemetry.addData("Normal", lightSensor.getLightDetected());
        telemetry.update();

    }


    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F, 0F, 0F};

    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;

    // get a reference to the RelativeLayout so we can change the background
    // color of the Robot Controller app to match the hue detected by the RGB sensor.
    final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.RelativeLayout);

    // bPrevState and bCurrState represent the previous and current state of the button.
    boolean bPrevState = false;
    boolean bCurrState = false;

    // bLedOn represents the state of the LED.
    boolean bLedOn = true;


    ///////Errors start from here


    // get a reference to our DeviceInterfaceModule object.



    // set the digital channel to output mode.
    // remember, the Adafruit sensor is actually two devices.
    // It's an I2C sensor and it's also an LED that can be turned on or off.

<<<<<<< HEAD

=======
>>>>>>> origin/master
        }
    }
}

