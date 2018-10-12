package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by Nora on 1/20/2018.
 */
/*@Autonomous(name = "range sensor", group = "Sensor")
public class RangeTest{
    ModernRoboticsI2cRangeSensor rangeSensor;

}*/

@Autonomous(name = "RangeTest", group = "Sensor")
@Disabled
   // comment out or remove  this line to enable this opmode
public class RangeTest extends LinearOpMode {


    ModernRoboticsI2cRangeSensor rangeSensor;
    DigitalChannel digitalTouch;
    @Override public void runOpMode() {
// get a reference to our digitalTouch object.

        digitalTouch = hardwareMap.get(DigitalChannel.class, "sensor_digital");

        // set the digital channel to input.
        digitalTouch.setMode(DigitalChannel.Mode.INPUT);
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "sensor_range");

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("ultrasonic", rangeSensor.rawUltrasonic());
            telemetry.addData("optical", rangeSensor.rawOptical());
            telemetry.addData("cm optical", "%.2f cm", rangeSensor.cmOptical());
            telemetry.addData("cm", "%.2f cm", rangeSensor.getDistance(DistanceUnit.CM));
            telemetry.update();
        }
        // send the info back to driver station using telemetry function.
        // if the digital channel returns true it's HIGH and the button is unpressed.
        if (digitalTouch.getState() == true) {
            telemetry.addData("Digital Touch", "Is Not Pressed");
        } else {
            telemetry.addData("Digital Touch", "Is Pressed");
        }

        telemetry.update();
    }

}

