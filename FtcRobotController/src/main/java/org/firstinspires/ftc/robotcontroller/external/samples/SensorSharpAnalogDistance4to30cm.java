/**
 *
 * Created by Maddie, FTC Team 4962, The Rockettes
 * version 1.0 Aug 20, 2016
 *
 */

package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;

/**
 * Example program to read data from the Sharp GP2Y0A41SK0F Analog Distance Sensor 4-30cm.
 * The sensor must be plugged into an analog port on the Device Interface Module and
 * configured on the phone as "distance".
 *
 * The relationship between distance and voltage is non-linear, so use a lookup table to find the
 * distance to the nearest cm, and then use linear interpolation to get the decimals.
 *
 * The datasheet can be found online at:
 *
 * http://www.sharp-world.com/products/device/lineup/data/pdf/datasheet/gp2y0a41sk_e.pdf
 *
 * and it is available for purchase at several retailers.
 *
 */
@TeleOp(name = "Sensor: Sharp Distance", group = "Sensor")
//@Disabled
public class SensorSharpAnalogDistance4to30cm extends LinearOpMode {


	/*
	 * Main loop
	 */
	@Override
    public void runOpMode() throws InterruptedException {
        //lookup table to find distance compared to voltage based on our measurements. We may need to
        // recalculate these using a more exact measurement tool, but they are within 1 cm.
        //
        // We measured these by taking physical measurements with the sensor.

        double[] distanceCm = {3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30};
        // These need to be re-calibrated.  Will update in next release.
        double[] voltage = {3.051757813, 2.685546875, 2.255859375, 1.93359375, 1.684570313,
                1.489257813, 1.357421875, 1.240234375, 1.137695313, 1.044921875,
                0.9814453125, 0.9228515625, 0.8740234375, 0.8349609375, 0.7958984375,
                0.76171875, 0.7177734375, 0.6982421875, 0.6591796875, 0.6396484375,
                0.6201171875, 0.576171875, 0.556640625, 0.5322265625, 0.5126953125,
                0.5126953125, 0.4931640625, 0.4736328125};



	/*
	 * Initialize the hardware
	 */

        AnalogInput DIM;

        DIM = hardwareMap.analogInput.get("distance");


        // wait for the start button to be pressed.
        waitForStart();

        while (opModeIsActive()) {
            // Reading voltage
            double voltreading = (float) DIM.getVoltage();
            //convert voltage to distance (cm)

            double distance = -999;

            //check to see if reading is out of range
            if (voltreading > 3.05 || voltreading < 0.48) {
                distance = -999;//missing value
            } else {
                // loop through our lookup table to get the two voltage points on either side
                // of the voltage we read.
                int arrayLength = distanceCm.length;
                for (int i = 0; i < (arrayLength - 1); i++) {
                    // when we find the voltage, use linear interpolation to calculate the exact
                    // value
                    if (voltreading <= voltage[i] && voltreading >= voltage[i + 1]) {
                        distance = distanceCm[i] +
                                ((voltreading - voltage[i]) * (distanceCm[i + 1] - distanceCm[i])) /
                                        (voltage[i + 1] - voltage[i]);

                    }
                }
            }



            telemetry.addData("time", "elapsed time: " + Double.toString(this.time));
            telemetry.addData("raw val", "reflection:  " + Double.toString(voltreading));
            // this is our calculated value
            telemetry.addData("Distance", "cm: " + Double.toString(distance));
            telemetry.update();
        }
	}


}
