package org.firstinspires.ftc.teamcode.ftc2016to2017season.CharlieTestCode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Steven on 11/27/2016.
 */
@Autonomous(name = "#11183: Color Sensor Test", group = "Robot")
@Disabled
public class ColorSensorTestOp extends OpMode{


        /*
            ---------------------------------------------------------------------------------------------

           Define the actuators we use in the robot here
        */

/*
    ---------------------------------------------------------------------------------------------

   Define the sensors we use in the robot here
*/


        ColorSensor colorSensor;    // Hardware Device Object



/*
 ----------------------------------------------------------------------------------------------
Declare global variables here
*/
    private boolean bLedOn;

        /*---------------------------------------------------------------------------------------------
                Get references to the hardware installed on the robot and name them here
        */
        @Override
        public void init() {

        /* get a reference to our ColorSensor object */
            colorSensor = hardwareMap.colorSensor.get("bColorSensorLeft");

            /* bLedOn represents the state of the LED.*/
             bLedOn = true;
            /* Set the LED in the beginning*/
            colorSensor.enableLed(bLedOn);
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
            //colorSensor.enableLed(false);
        }

    /*
    Code to run REPEATEDLY after the driver hit PLAY
    Main code loop goes here
     */

        @Override
        public void loop() {

             ReadColorSensor();
            colorSensor.enableLed(false);


        }
/*
---------------------------------------------------------------------------------------------

    Functions go here
 */




        /*---------------------------------------------------------------------------------------------
        */
    /* Read the color sensor and return "b" for Blue or "r" for Red */
        public char ReadColorSensor(){

            char redOrBlue = 'b';

    /* Variables used to store value of the color sensor*/
    /* hsvValues is an array that will hold the hue, saturation, and value information */
            float hsvValues[] = {0F,0F,0F};


            

    /* convert the RGB values to HSV values*/
            Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);

    /* send the info back to driver station using telemetry function.*/
            telemetry.addData("LED", bLedOn ? "On" : "Off");
            telemetry.addData("Clear", colorSensor.alpha());
            telemetry.addData("Red  ", colorSensor.red());
            telemetry.addData("Green", colorSensor.green());
            telemetry.addData("Blue ", colorSensor.blue());
            telemetry.addData("Hue", hsvValues[0]);


            if (colorSensor.red()>colorSensor.blue() && colorSensor.red() >= 1) {/*need to find out the treashold for */
                redOrBlue = 'r';
            }
            telemetry.addData("Red or Blue", redOrBlue);
            telemetry.update();
            return redOrBlue;
        }


        /*
        ---------------------------------------------------------------------------------------------

             Code to run ONCE after the driver hits STOP
        */
        @Override
        public void stop() {
            bLedOn = false;
            colorSensor.enableLed(bLedOn);
        }

/*
---------------------------------------------------------------------------------------------
 */

    }


