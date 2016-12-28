package org.firstinspires.ftc.teamcode.Steven;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaCodecInfo;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by Steven on 12/27/2016.
 */
@Autonomous(name = "#11183: TeleOp Competition", group = "Robot")
public class DistanceSensorTest extends OpMode{


/*
    ---------------------------------------------------------------------------------------------

   Define the sensors we use in the robot here
*/



    ModernRoboticsI2cRangeSensor rangeSensor;//combines ultrasonic and optical measuring elements


    /*---------------------------------------------------------------------------------------------
            Get references to the hardware installed on the robot and name them here
    */
    @Override
    public void init() {
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");
    }

    /*
    ---------------------------------------------------------------------------------------------

          Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    */
    @Override
    public void init_loop() {

    }

    /*
     ---------------------------------------------------------------------------------------------

          Code to run ONCE when the driver hits PLAY

    */
    @Override
    public void start(){


        rangeSensor.enableLed(true);
        telemetry.addData("cmOptical", rangeSensor.cmOptical());//use this for a range of 1-7cm
        telemetry.update();
        sleep(5000);
        rangeSensor.enableLed(false);

        telemetry.addData("cmUltrasonic", rangeSensor.cmUltrasonic());//use this for a range of 5-255cm
        telemetry.update();
        sleep(5000);

        telemetry.addData("distance",rangeSensor.getDistance(DistanceUnit.CM));
        telemetry.update();

    }

    /*
    Code to run REPEATEDLY after the driver hit PLAY
    Main code loop goes here
     */

    @Override
    public void loop() {

        //shoot();

    }

    @Override
    public void stop(){

    }


/*
---------------------------------------------------------------------------------------------


    Functions go here
 */


    public void sleep(long pauseInMS) {
        long time_sleepStart = System.currentTimeMillis();
        long endTime = time_sleepStart + pauseInMS;

        while(endTime - System.currentTimeMillis() > 0) {

        }
    }
}