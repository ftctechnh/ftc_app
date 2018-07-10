package org.firstinspires.ftc.teamcode.ftc2016to2017season.Steven;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.TimeUnit;

/**
 * Created by inspirationteam on 12/2/2016.
 */
@Autonomous(name = "pushBeacon", group = "Pushbot")
@Disabled
public class PushBeaconRed extends OpMode {
    Servo beaconPress;
    boolean bLedOn;


    ColorSensor colorSensor;

    @Override
    public void init(){
        beaconPress = hardwareMap.servo.get("beaconPress");


        colorSensor = hardwareMap.colorSensor.get("sensor_color");
        bLedOn = false;
        colorSensor.enableLed(bLedOn);
        beaconPress.setPosition(0.85);
    }

    @Override
    public void init_loop(){


    }

    @Override
    public void start() {//main autonomous mode goes in here

        //wait 20 seconds

        //found these values by trial and error






        if(ReadColorSensor() == 'r'){
            //push button if on blue team, don't push if on red team
            //leftPlate.setPosition(0.8);//this is the servo with the modern robotics logo on the opposite side of the plate
            beaconPress.setPosition(0);//this is the servo with the modern robotics logo on the same side of the plate
        }
        /*else if (ReadColorSensor() == 'b'){
            //push button if on red team, don't push if on blue team
        }*/

    }
    public void loop(){

        // this is only a test to see what the positions are
         /*telemetry.addData("Left Servo Position", leftPlate.getPosition());
            telemetry.addData("Right Servo Position", rightPlate.getPosition());
            telemetry.update();
        if (position != 1) {
            position += 0.1;
            leftPlate.setPosition(position);
        }*/

    }

    public void stop(){

    }

    public void sleep(int seconds) throws InterruptedException{
        TimeUnit.SECONDS.sleep(seconds);
    }

    public char ReadColorSensor(){

        char redOrBlue = 'b';

    //* Variables used to store value of the color sensor
    //* hsvValues is an array that will hold the hue, saturation, and value information
        float hsvValues[] = {0F,0F,0F};



    //convert the RGB values to HSV values
        Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);

    // send the info back to driver station using telemetry function.
        telemetry.addData("LED", bLedOn ? "On" : "Off");
        telemetry.addData("Clear", colorSensor.alpha());
        telemetry.addData("Red  ", colorSensor.red());
        telemetry.addData("Green", colorSensor.green());
        telemetry.addData("Blue ", colorSensor.blue());
        telemetry.addData("Hue", hsvValues[0]);


        if (colorSensor.red()>colorSensor.blue()) {//need to find out the treashold for
            redOrBlue = 'r';
        }
        telemetry.addData("Red or Blue", redOrBlue);
        telemetry.update();
        return redOrBlue;
    }

}
