package org.firstinspires.ftc.teamcode.Qualifier;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class JewelArm {
    public Servo jewelArmServo;
    public Servo jewelFlickerServo;
   // public ColorSensor sensorColor;
//    public DistanceSensor sensorDistance;
    public double BRRatio = 0;
    public double jewelarmup = 0.22;

    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F, 0F, 0F};

    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;

    // sometimes it helps to multiply the raw RGB values with a scale factor
    // to amplify/attentuate the measured values.
    final double SCALE_FACTOR = 255;

    boolean teamIsRED;
    boolean redjewelisfront;

    public void init(HardwareMap hardwareMap) {
        jewelArmServo = hardwareMap.servo.get("jewel_arm");
        jewelFlickerServo = hardwareMap.servo.get("jewel_flicker");
      //  sensorColor = hardwareMap.get(ColorSensor.class, "sensor_color");
        // get a reference to the distance sensor that shares the same name.
 //       sensorDistance = hardwareMap.get(DistanceSensor.class, "sensor_color");


        jewelArmUp();


    }

    public void jewelArmUp() {
        jewelArmServo.setPosition(0.22);
        jewelflickerForward();
    }

    public void jewelArmDown() {
        jewelArmServo.setPosition(0.80);
        jewelflickerCenter();
    }

    public void jewelflickerBack() { jewelFlickerServo.setPosition(0.0); //was 0.0
    }

    public void jewelflickerCenter() { jewelFlickerServo.setPosition(0.5);

    }

    public void jewelflickerForward() {
        jewelFlickerServo.setPosition(1.0);
    } //was 1.0

    public void solveJewelPuzzle(boolean teamIsRED) {

          // sensor is facing the forward ball.

//        // determine if Red to the left, the sensor reads in the left direction.
//        if (sensorColor.red() > sensorColor.blue()) {   //then RED is front
//            redjewelisfront = true;
//        } else {
//            redjewelisfront = false;
//        }

        if (teamIsRED && redjewelisfront) {            //Red Team, red is in front
            jewelflickerBack();
            //while( jewelFlickerServo.getPosition() > 0.05) { }
        } else if (teamIsRED && !redjewelisfront) {    //Red Team, red is in back
            jewelflickerForward();
            //while( jewelFlickerServo.getPosition() < 0.95) { }
        } else if (!teamIsRED && redjewelisfront) {     //Blue Team, red is in front
            jewelflickerForward();
            //while( jewelFlickerServo.getPosition() < 0.95) { }
        } else if (!teamIsRED && !redjewelisfront) {    //Blue Team, red is in front
            jewelflickerBack();
           //while( jewelFlickerServo.getPosition() > 0.05) { }
        }


    }

    public void solveJewelPuzzleCamera(boolean teamIsRED,boolean blueJewelIsLeft) {

        // sensor is facing the forward ball.

        if (teamIsRED && blueJewelIsLeft) {            //Red Team, blue is in front
            jewelflickerForward();
        } else if (teamIsRED && !blueJewelIsLeft) {    //Red Team, blue is in back
            jewelflickerBack();
        } else if (!teamIsRED && blueJewelIsLeft) {     //Blue Team, blue is in front
            jewelflickerBack();
        } else if (!teamIsRED && !blueJewelIsLeft) {    //Blue Team, blue is in back
            jewelflickerForward();
        }
    }

    public void solveJewelPuzzleCameraRP(boolean teamIsRED,boolean blueJewelIsLeft) {

        // sensor is facing the forward ball.  This should so the WRONG jewel

        if (teamIsRED && blueJewelIsLeft) {            //Red Team, blue is in front
            jewelflickerBack();
        } else if (teamIsRED && !blueJewelIsLeft) {    //Red Team, blue is in back
            jewelflickerForward();
        } else if (!teamIsRED && blueJewelIsLeft) {     //Blue Team, blue is in front
            jewelflickerForward();
        } else if (!teamIsRED && !blueJewelIsLeft) {    //Blue Team, blue is in back
            jewelflickerBack();
        }
    }




    /** -----------------------------------------------------------------------------------
    |  Grab a frame from Vuforia, check out where the jewel should be, see if it's red or Blue.
    \------------------------------------------------------------------------------------- */
    public boolean blueIsOnLeft(Bitmap rgbImage) {
        // play images are 1280x720 w x h  origin is lower right looking at vuforia image,  Lower Left from camera pov
        // x is vertical, y is horizontal (for portrait orientation)
        // Ball was about inscribed in a 300x300 box



/**
                 <---------  X  --------------->

     (0,0)     +----------------------------------+   (1280,0)         ^
               |                            Ball x|                    |
               |                            xxxxxx|                    |
               |                            xxxxxx|                    |   Y
               |                            xxxxxx|                    |
               |                                  |                    |
               |                                  |                    |
     (0,720)   +----------------------------------+   (1280,720)
*/



        // go for a square that is inside the ball (ball is about 350x 350,  sample 250x250;

        int XStart = 1280-200;
        int XEnd =1279;
        int yStart =0;
        int yEnd = 300;
// moved the phone up, the ball down (X)
//        int XStart = 1280-200;
//        int XEnd =1280-0;
//        int yStart =50;
//        int yEnd = 300-50;

        int RedValue = 0;
        int BlueValue = 0;

        for (int x = XStart; x < XEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {
                int pixel = rgbImage.getPixel(x, y);
                RedValue += (pixel >> 16) & 0xff;
                BlueValue += pixel & 0xff;
            }
        }
        BRRatio = (double) BlueValue / RedValue;

        if (BlueValue > RedValue)

            return true;
        else
            return false;

    }



}

