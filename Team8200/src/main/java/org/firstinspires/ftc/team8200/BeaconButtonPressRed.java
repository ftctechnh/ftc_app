package org.firstinspires.ftc.team8200;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
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


    HardwareK9bot robot = new HardwareK9bot(); // Hardware Device Object
    static final double WHITE_THRESHOLD = 0.4;
    static final double MOTOR_POWER = 0.1;
    static final int LED_CHANNEL = 5;
    DeviceInterfaceModule dim;
    AnalogInput distanceSensor;
    double voltage, maxVoltage, voltsPerInch, voltageInInches;
    private ElapsedTime runtime = new ElapsedTime();
    String kev = "klk";


    @Override

    public void runOpMode()
    {
        waitForStart();
        MoveToBeacon();
        distanceSensorCode();
    }

   public void distanceSensorCode() {
        dim = hardwareMap.get(DeviceInterfaceModule.class, "dim");
        distanceSensor = hardwareMap.get(AnalogInput.class, "distance");

        waitForStart();
            while (opModeIsActive()) {
                voltage = distanceSensor.getVoltage();
                maxVoltage = distanceSensor.getMaxVoltage();
                voltsPerInch = 5.0 / 512.0;
                voltageInInches = voltage / voltsPerInch;
                while (voltageInInches <= 13)
                {
                        runtime.reset();
                         if (runtime.seconds() > 3.0)
                         {
                             telemetry.addData("distanceTest. will print klk", kev);
                             telemetry.update();
                         }
                 }
            telemetry.addData("Voltage", voltageInInches);
            telemetry.addData("Max Voltage", maxVoltage);
            telemetry.update();
        }
    }

    public void colorSensorCode()
    {
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
        robot.init(hardwareMap);
        robot.cdim = hardwareMap.deviceInterfaceModule.get("cdim");
        //colorSensor = robot.colorSensor;

        bLedOn = true;

/       // Set the LED state in the beginning.
        robot.lightSensor.enableLed(bLedOn);
        robot.cdim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);

        // get a reference to our ColorSensor object.

        // turn the LED on in the beginning, just so user will know that the sensor is active.
        robot.cdim.setDigitalChannelState(LED_CHANNEL, bLedOn);

        // wait for the start button to be pressed.

        // loop and read the RGB data.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
        while (opModeIsActive()) {

            // convert the RGB values to HSV values.
            Color.RGBToHSV((robot.colorSensor.red() * 255) / 800, (robot.colorSensor.green() * 255) / 800, (robot.colorSensor.blue() * 255) / 800, hsvValues);

            // send the info back to driver station using telemetry function.
            telemetry.addData("LED", bLedOn ? "On" : "Off");
            telemetry.addData("Clear", robot.colorSensor.alpha());
            telemetry.addData("Red  ", robot.colorSensor.red());
            telemetry.addData("Green", robot.colorSensor.green());
            telemetry.addData("Blue ", robot.colorSensor.blue());
            telemetry.addData("Hue", hsvValues[0]);

            // change the background color to match the color detected by the RGB sensor.
            // pass a reference to the hue, saturation, and value array as an argument
            // to the HSVToColor method.
            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
                }
            });
        }
    }

    public void MoveToBeacon() {
        // Move to white line
        while (robot.lightSensor.getLightDetected() < WHITE_THRESHOLD)
        {
            robot.leftMotor.setPower(MOTOR_POWER);
            robot.rightMotor.setPower(MOTOR_POWER);
            updateTelemetry();
        }

        //step 2
        robot.rightMotor.setPower(0);
        robot.leftMotor.setPower(0);

        telemetry.addData("Say", "Motors just stopped.");
        telemetry.update();


        while (opModeIsActive())
        { //while the touch sensor is not touching the wall (or proximity sensor is not touching wall)
            // step 3 turning for ___ seconds
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 0.01)) {
                robot.leftMotor.setPower(0);
                robot.rightMotor.setPower(MOTOR_POWER);
                telemetry.addData("Raw", robot.lightSensor.getRawLightDetected());
                telemetry.addData("Say", "Motors turning.");
                telemetry.update();
            }

            //step 4 follow the line
            while (robot.lightSensor.getLightDetected() >= WHITE_THRESHOLD)
            { //follows white light is above threshold AND touch sensor is not touching
                robot.leftMotor.setPower(MOTOR_POWER);
                robot.rightMotor.setPower(MOTOR_POWER);
                telemetry.addData("Say", "Motors following line.");
                telemetry.update();
            }

        }
    }

    public void updateTelemetry() {
        telemetry.addData("Raw", robot.lightSensor.getRawLightDetected());
        telemetry.addData("Normal", robot.lightSensor.getLightDetected());
        telemetry.update();
    }
}



