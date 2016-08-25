/**
 *
 * Created by Maddie and Bria!, FTC Team 4962, The Rockettes
 * version 1.0 Aug 22, 2016
 *
 */

package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;

/**
 * Example program to read data from a generic 3-pin Potentiometer.
 * The sensor must be plugged into an analog port on the Device Interface Module and
 * configured on the phone as "potentiometer".
 *
 * The relationship between rotation and voltage is linear, so we will return a number betweeen
 * 0 and 100 to tell the percentage of how far the knob has rotated.
 *
 *
 * See https://www.adafruit.com/product/562
 *
 */
@TeleOp(name = "Sensor: Generic Potentiometer", group = "Sensor")
//@Disabled
public class SensorGenericPotentiometer extends LinearOpMode {


	/*
	 * Main loop
	 */
	@Override
    public void runOpMode() throws InterruptedException {

	/*
	 * Initialize the hardware
	 */

        AnalogInput DIM;

        DIM = hardwareMap.analogInput.get("potentiometer");

        // wait for the start button to be pressed.
        waitForStart();

        while (opModeIsActive()) {
            // Reading voltage
            double voltreading = (float) DIM.getVoltage();
            //convert voltage to distance (cm)

            double percentTurned = voltreading/5 * 100;

            telemetry.addData("time", "elapsed time: " + Double.toString(this.time));
            telemetry.addData("raw val", "voltage:  " + Double.toString(voltreading));
            // this is our calculated value
            telemetry.addData("PercentRot", "percent: " + Double.toString(percentTurned));
            telemetry.update();
        }
	}


}
