package org.firstinspires.ftc.teamcode.Sensors;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Swagster_Wagster on 10/25/17.
 */
@Autonomous(name = "ColorSensor")
public class ColorSensor extends LinearOpMode {

    com.qualcomm.robotcore.hardware.ColorSensor colorSensor;    // Hardware Device Object

    @Override
    public void runOpMode() throws InterruptedException {

        float hsvValues[] = {0F,0F,0F};

        // bLedOn represents the state of the LED.
        boolean bLedOn = true;

        // get a reference to our ColorSensor object.
        colorSensor = hardwareMap.get(com.qualcomm.robotcore.hardware.ColorSensor.class, "colorSensor");

        // Set the LED in the beginning
        colorSensor.enableLed(true);

        // wait for the start button to be pressed.
        waitForStart();

        // while the op mode is active, loop and read the RGB data.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
        while (opModeIsActive()) {


            // convert the RGB values to HSV values.
            Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);


            int red = colorSensor.red();
            int blue = colorSensor.blue();
            int green = colorSensor.green();
            telemetry.addData("blue value is", blue);
            telemetry.addData("red value is ", red);
            telemetry.addData("red value is ", green);
            telemetry.update();

            if (blue >=4 && blue >red) {

                telemetry.addData("i am blue", colorSensor.blue());
                telemetry.addData("red value is ", colorSensor.red());
                telemetry.update();
            }

            if (red >=4  && red>blue ) {

                telemetry.addData("i am red", colorSensor.red());
                telemetry.addData("blue value is ", colorSensor.blue());
                telemetry.update();
            }
        }
}}
