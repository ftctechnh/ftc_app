package com.qualcomm.ftcrobotcontroller.opmodes;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.ftcrobotcontroller.opmodes.BasicMoveFunctions;
import com.qualcomm.ftcrobotcontroller.opmodes.HTRGBExample;

/**
 * @author Darron and Caiti
 *  created 12/9/2015
 *  ftc robot conroler autonomous base class
 */


public class Encoders extends  LinearOpMode {
      final static int encoder_kpr = 1120;
    final static double gear_ratio1 = 1;// unkown
    final static double gear_ratio2= 1.14;//unknown
    final static int wheel_diameter = 4;
    final static double Circumfrance = Math.PI *wheel_diameter;
    double activegear;
    double dpk = (encoder_kpr* gear_ratio1) /Circumfrance;
    double distance;
    DcMotor leftmotor;
    DcMotor rightmotor;
    double encoder1;
    double encoder2;
    HTRGBExample color;
    int colortemp;
    ColorSensor sensorRGB;
    Servo servo;
    double move1;

    private void initiate () throws InterruptedException {
        leftmotor = hardwareMap.dcMotor.get("leftmotor");
        rightmotor = hardwareMap.dcMotor.get("rightmotor");
        activegear = gear_ratio1;
        rightmotor.setDirection(DcMotor.Direction.REVERSE);
        leftmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        sensorRGB = hardwareMap.colorSensor.get("color");
        servo = hardwareMap.servo.get("servo");
        initsensor();
        servo.setPosition(.3);
    }


    private void initsensor() throws InterruptedException {


        // write some device information (connection info, name and type)
        // to the log file.
        hardwareMap.logDevices();

        // get a reference to our ColorSensor object.
        sensorRGB = hardwareMap.colorSensor.get("color");

        // bEnabled represents the state of the LED.


        // turn the LED on in the beginning, just so user will know that the sensor is active.
        sensorRGB.enableLed(false);
    }


    private int getRed() throws InterruptedException {
        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F, 0F, 0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(R.id.RelativeLayout);

        // bPrevState and bCurrState represent the previous and current state of the button.
        boolean bPrevState = false;
        boolean bCurrState = false;

        // while the op mode is active, loop and read the RGB data.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.


        // convert the RGB values to HSV values.
        Color.RGBToHSV(sensorRGB.red(), sensorRGB.green(), sensorRGB.blue(), hsvValues);

        // send the info back to driver station using telemetry function.
        telemetry.addData("Clear", sensorRGB.alpha());
        telemetry.addData("Red  ", sensorRGB.red());
        telemetry.addData("Green", sensorRGB.green());
        telemetry.addData("Blue ", sensorRGB.blue());
        telemetry.addData("Hue", hsvValues[0]);

        // change the background color to match the color detected by the RGB sensor.
        // pass a reference to the hue, saturation, and value array as an argument
        // to the HSVToColor method.
        waitOneFullHardwareCycle();
        return sensorRGB.red();
    }

    private int getBlue() throws InterruptedException {
        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F, 0F, 0F};
        sensorRGB = hardwareMap.colorSensor.get("color");
        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(R.id.RelativeLayout);

        // bPrevState and bCurrState represent the previous and current state of the button.
        boolean bPrevState = false;
        boolean bCurrState = false;

        // while the op mode is active, loop and read the RGB data.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.


        // convert the RGB values to HSV values.
        Color.RGBToHSV(sensorRGB.red(), sensorRGB.green(), sensorRGB.blue(), hsvValues);

        // send the info back to driver station using telemetry function.

        // change the background color to match the color detected by the RGB sensor.
        // pass a reference to the hue, saturation, and value array as an argument
        // to the HSVToColor method.
        waitOneFullHardwareCycle();
        return sensorRGB.blue();
    }

    /**
     * @Override run the main opmode
     * @throws InterruptedException
     */
    @Override
    public void runOpMode() throws InterruptedException {
        initiate();
        waitForStart();
        leftmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        waitOneFullHardwareCycle();
        rightmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        waitOneFullHardwareCycle();
        // movebackward(10, .5);
        waitOneFullHardwareCycle();
        // turnright(4, .5);
        waitOneFullHardwareCycle();
        // turnleft(4, .5);
        waitOneFullHardwareCycle();
        // moveforward(10, .5);
        if(getRed()< getBlue()) {
            servo.setPosition(.5);
            telemetry.addData("blue","blue");
        }
        else if (getRed()> getBlue()) {
            servo.setPosition(.8);
        }
        else {
            System.out.print("checks failed");
        }
    }


    /**
     * move forward for a number of counts
     * @param move the distance to move in inches
     * @param power the power to use
     * @throws InterruptedException
     */
    public void moveforward(double move, double power) throws InterruptedException {


        double distancetemp;
        distancetemp = move * dpk;
        distance = distancetemp;
        int is;
        is = 0;
        encoder1 = encoder1+(int) distancetemp;
        encoder2= encoder2+(int)distancetemp;
        leftmotor.setTargetPosition((int) (encoder1));
        waitOneFullHardwareCycle();
        rightmotor.setTargetPosition((int) encoder2);
        waitOneFullHardwareCycle();
        leftmotor.setPower(power);
        waitOneFullHardwareCycle();
        rightmotor.setPower(power);
        while (is <= 1000) { // object not locked by wait
            waitOneFullHardwareCycle();
            is++;
            telemetry.addData("is", is);
        }
        leftmotor.setPower(.0);
        rightmotor.setPower(0);
    }

    /**
     * turn left for a number of counts
     * @param move the distance to move in inches
     * @param power the power to use
     * @throws InterruptedException
     */
    public void turnleft(double move, double power) throws InterruptedException {

        double distancetemp;
        move = move ;
        distance = distancetemp;
        encoder1 = encoder1- distancetemp;
        encoder2= encoder2+distancetemp;
        int is;
        is = 0;

        leftmotor.setTargetPosition((int) encoder1);
        waitOneFullHardwareCycle();
        rightmotor.setTargetPosition((int) encoder2);
        waitOneFullHardwareCycle();
        leftmotor.setPower(power);
        waitOneFullHardwareCycle();
        rightmotor.setPower(power);
        waitOneFullHardwareCycle();
        while (is <=  distance) {
            waitOneFullHardwareCycle();
            is++;
        }
        leftmotor.setPower(0);
        rightmotor.setPower(0);
    }

    /**
     * turn right for a number of counts
     * @param move the distance to move in inches
     * @param power the power to use
     * @throws InterruptedException
     */
    public void turnright(double move, double power) throws InterruptedException {

        double distancetemp;
        distancetemp = move * dpk;
        int is;
        is = 0;
        distance = distancetemp;
        encoder1 = encoder1+ distancetemp;
        encoder2= encoder2- distancetemp;

        leftmotor.setTargetPosition((int) encoder1);
        waitOneFullHardwareCycle();
        rightmotor.setTargetPosition((int) encoder2);
        waitOneFullHardwareCycle();
        leftmotor.setPower(power);
        waitOneFullHardwareCycle();
        rightmotor.setPower(power);

        while (is <= 3* (int)distance) { // object not locked by wait
            waitOneFullHardwareCycle();
            is++;
            telemetry.addData("is", is);
            telemetry.addData("distance" , distance);
        }
        leftmotor.setPower(0);
        rightmotor.setPower(0);
    }
    /**
     * move backwards for a number of counts
     * @param move the distance to move in inches
     * @param power the power to use
     * @throws InterruptedException
     */
    public void movebackward(double move, double power) throws InterruptedException {

        double distancetemp;
        distancetemp = move * dpk;
        encoder1 = encoder1-(int) distancetemp;
        encoder2= encoder2-(int)distancetemp;
        int is;
        is = 0;
        distance = distancetemp;
        leftmotor.setTargetPosition((int) encoder1);
        waitOneFullHardwareCycle();
        rightmotor.setTargetPosition((int) encoder2);
        waitOneFullHardwareCycle();
        leftmotor.setPower(power);
        waitOneFullHardwareCycle();

        rightmotor.setPower(power);
        while (is <=  distance) { // object not locked by wait
            waitOneFullHardwareCycle();
            is++;
        }
        leftmotor.setPower(0);
        waitOneFullHardwareCycle();
        rightmotor.setPower(0);

    }

}

