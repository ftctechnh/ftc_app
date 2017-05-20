/*
Modern Robotics ODS Wall Follow Example
Updated 11/4/2016 by Colton Mehlhoff of Modern Robotics using FTC SDK 2.35
Reuse permitted with credit where credit is due

Configuration:
Optical Distance sensor named "ods"
Left drive train motor named "ml"  (two letters)
Right drive train motor named "mr"
Both motors need encoders

For more information, go to http://modernroboticsedu.com/course/view.php?id=5
Support is available by emailing support@modernroboticsinc.com.
*/

package org.firstinspires.ftc.teamcode.sensors;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

@TeleOp(name = "LightReading", group = "MRI")
//@Disabled
public class ODS_LightReading extends LinearOpMode {

    //Instance of OpticalDistanceSensor
    OpticalDistanceSensor ODS;

    //Motors

    //Raw value is between 0 and 1
    static double odsReadingRaw;

    // odsReadingRaw to the power of (-0.5)
    static double odsReadingLinear;

    @Override
    public void runOpMode() throws InterruptedException {

        //identify the port of the ODS and motors in the configuration file
        ODS = hardwareMap.opticalDistanceSensor.get("ods");

        //This program was designed around a robot that uses two gears on each side of the drive train.
        //If your robot uses direct drive between the motor and wheels or there are an odd number of gears, the opposite motor will need to be reversed.

        waitForStart();

        while (opModeIsActive()) {
            odsReadingRaw = ODS.getRawLightDetected() ;
            odsReadingLinear = Math.pow(odsReadingRaw, -0.5);

            telemetry.addData("0 ODS Raw", odsReadingRaw);
            telemetry.addData("1 ODS linear", odsReadingLinear);
            telemetry.update();
        }
    }
}//end class