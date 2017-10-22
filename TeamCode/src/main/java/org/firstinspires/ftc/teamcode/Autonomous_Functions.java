package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.robotcore.external.Telemetry;

// Created by MRINAAL RAMACHANDRAN on 10/8/17
//
//
// Last edit: 10/21/17 BY MRINAAL RAMACHANDRAN

// Last edit: 10/21/17 BY SAMIH QURESHI

public class Autonomous_Functions {

    // MOTOR NAMES

    protected DcMotor F_L = null;
    protected DcMotor F_R = null;
    protected DcMotor R_L = null;
    protected DcMotor R_R = null;


    protected Servo dropper = null;

    protected NormalizedColorSensor colorSensor;
    float[] hsvValues = new float[3];
    final float values[] = hsvValues;

    // LOCAL OPMODE MEMBERS
    HardwareMap hwMap = null;
    Telemetry telemetry = null;

    // HARDWARE INIT
    public void init(HardwareMap ahwMap) {

        hwMap = ahwMap;

        F_L = hwMap.get(DcMotor.class, "F_L");
        F_R = hwMap.get(DcMotor.class, "F_R");
        R_L = hwMap.get(DcMotor.class, "R_L");
        R_R = hwMap.get(DcMotor.class, "R_R");

        F_L.setPower(0);
        F_R.setPower(0);
        R_L.setPower(0);
        R_R.setPower(0);

        F_L.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        F_R.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        R_L.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        R_R.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        dropper = hwMap.get(Servo.class, "dropper");


        // initializing color sensor

        colorSensor = hwMap.get(NormalizedColorSensor.class, "sensor_color");

    }

    // SLEEPING THREAD FUNCTION
    protected void mysleep(long time) {

        try {

            Thread.sleep(time);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    // STOP MOTOR FOR (x) TIME FUNCTION
    public void stopMotor(long time) {

        F_L.setPower(0);
        F_R.setPower(0);
        R_L.setPower(0);
        R_R.setPower(0);
        mysleep(time);

    }

    // STOP MOTOR
    public void stopMotor() {

        stopMotor(0);

    }

    // MOVES THE MOTOR FOR TIME WITH INPUTS POWER, TIME, AND DIRECTION
    public void moveMotorWithTime(double power, long time, String direction) {

        if (direction == Constants.forward) {

            F_L.setDirection(DcMotor.Direction.REVERSE);
            F_R.setDirection(DcMotor.Direction.FORWARD);
            R_L.setDirection(DcMotor.Direction.REVERSE);
            R_R.setDirection(DcMotor.Direction.FORWARD);

            F_L.setPower(power);
            F_R.setPower(power);
            R_L.setPower(power);
            R_R.setPower(power);
            mysleep(time);
        }

        if (direction == Constants.backward) {

            F_R.setDirection(DcMotor.Direction.REVERSE);
            F_L.setDirection(DcMotor.Direction.FORWARD);
            R_L.setDirection(DcMotor.Direction.FORWARD);
            R_R.setDirection(DcMotor.Direction.REVERSE);

            F_L.setPower(power);
            F_R.setPower(power);
            R_L.setPower(power);
            R_R.setPower(power);
            mysleep(time);
        }

        if (direction == Constants.left) {

            R_L.setDirection(DcMotor.Direction.REVERSE);
            F_L.setDirection(DcMotor.Direction.FORWARD);
            F_R.setDirection(DcMotor.Direction.FORWARD);
            R_R.setDirection(DcMotor.Direction.REVERSE);

            F_L.setPower(power);
            F_R.setPower(power);
            R_L.setPower(power);
            R_R.setPower(power);
            mysleep(time);
        }

        if (direction == Constants.right) {

            F_L.setDirection(DcMotor.Direction.REVERSE);
            F_R.setDirection(DcMotor.Direction.REVERSE);
            R_L.setDirection(DcMotor.Direction.FORWARD);
            R_R.setDirection(DcMotor.Direction.FORWARD);

            F_L.setPower(-power);
            F_R.setPower(-power);
            R_L.setPower(power);
            R_R.setPower(power);
            mysleep(time);
        }

        stopMotor();
    }

    // MOVES THE MOTOR WITH ENCODERS WITH INPUTS POWER, TIME, AND DISTANCE
    public void moveMotorWithEncoder(double power, int distance, String direction) {

        F_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if (direction == Constants.forward) {

            F_L.setDirection(DcMotor.Direction.REVERSE);
            F_R.setDirection(DcMotor.Direction.FORWARD);
            R_L.setDirection(DcMotor.Direction.REVERSE);
            R_R.setDirection(DcMotor.Direction.FORWARD);

            F_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            F_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            F_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            F_L.setTargetPosition(distance);
            F_R.setTargetPosition(distance);
            R_L.setTargetPosition(distance);
            R_R.setTargetPosition(distance);

            F_L.setPower(power * 10);
            F_R.setPower(power * 10);
            R_L.setPower(power * 10);
            R_R.setPower(power * 10);

            while (F_L.getCurrentPosition() < distance/2) {

                try {

                    Thread.sleep(1);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }

            F_L.setPower(power * 5);
            F_R.setPower(power * 5);
            R_L.setPower(power * 5);
            R_R.setPower(power * 5);


            while (F_L.isBusy() ) {

            }
        }

        if (direction == Constants.backward) {

            F_R.setDirection(DcMotor.Direction.REVERSE);
            F_L.setDirection(DcMotor.Direction.FORWARD);
            R_L.setDirection(DcMotor.Direction.FORWARD);
            R_R.setDirection(DcMotor.Direction.REVERSE);

            F_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            F_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            F_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            F_L.setTargetPosition(distance);
            F_R.setTargetPosition(distance);
            R_L.setTargetPosition(distance);
            R_R.setTargetPosition(distance);

            F_L.setPower(power*10);
            F_R.setPower(power*10);
            R_L.setPower(power*10);
            R_R.setPower(power*10);

            while (F_L.isBusy() ) {

            }
        }

        if (direction == Constants.left) {

            R_L.setDirection(DcMotor.Direction.REVERSE);
            F_L.setDirection(DcMotor.Direction.FORWARD);
            F_R.setDirection(DcMotor.Direction.FORWARD);
            R_R.setDirection(DcMotor.Direction.REVERSE);

            F_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            F_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            F_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            F_L.setTargetPosition(distance);
            F_R.setTargetPosition(distance);
            R_L.setTargetPosition(distance);
            R_R.setTargetPosition(distance);

            F_L.setPower(power*10);
            F_R.setPower(power*10);
            R_L.setPower(power*10);
            R_R.setPower(power*10);

            while (F_L.isBusy() ) {

            }
        }

        if (direction == Constants.right) {

            F_L.setDirection(DcMotor.Direction.REVERSE);
            F_R.setDirection(DcMotor.Direction.REVERSE);
            R_L.setDirection(DcMotor.Direction.FORWARD);
            R_R.setDirection(DcMotor.Direction.FORWARD);

            F_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            F_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            F_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            F_L.setTargetPosition(distance);
            F_R.setTargetPosition(distance);
            R_L.setTargetPosition(distance);
            R_R.setTargetPosition(distance);

            F_L.setPower(power*10);
            F_R.setPower(power*10);
            R_L.setPower(power*10);
            R_R.setPower(power*10);

            while (F_L.isBusy() ) {

            }
        }

        if (direction == Constants.spinRight) {

            F_L.setDirection(DcMotor.Direction.FORWARD);
            F_R.setDirection(DcMotor.Direction.FORWARD);
            R_L.setDirection(DcMotor.Direction.FORWARD);
            R_R.setDirection(DcMotor.Direction.FORWARD);

            F_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            F_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            F_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            F_L.setTargetPosition(distance);
            F_R.setTargetPosition(distance);
            R_L.setTargetPosition(distance);
            R_R.setTargetPosition(distance);

            F_L.setPower(power*10);
            F_R.setPower(power*10);
            R_L.setPower(power*10);
            R_R.setPower(power*10);

            while (F_L.isBusy() ) {

            }
        }

        if (direction == Constants.spinLeft) {

            F_L.setDirection(DcMotor.Direction.REVERSE);
            F_R.setDirection(DcMotor.Direction.REVERSE);
            R_L.setDirection(DcMotor.Direction.REVERSE);
            R_R.setDirection(DcMotor.Direction.REVERSE);

            F_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            F_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_L.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            R_R.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            F_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            R_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            F_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            F_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            R_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            F_L.setTargetPosition(distance);
            F_R.setTargetPosition(distance);
            R_L.setTargetPosition(distance);
            R_R.setTargetPosition(distance);

            F_L.setPower(power*10);
            F_R.setPower(power*10);
            R_L.setPower(power*10);
            R_R.setPower(power*10);

            while (F_L.isBusy() ) {

            }
        }


        stopMotor();
    }
    public void senseColor() {
        // turn on light on sensor
        if (colorSensor instanceof SwitchableLight) {
            SwitchableLight light = (SwitchableLight)colorSensor;
            light.enableLight(true);
        }
        // read color values
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        // normalize color
        Color.colorToHSV(colors.toColor(), hsvValues);
        //telemetry.addLine()
        //        .addData("H", "%.3f", hsvValues[0])
        //        .addData("S", "%.3f", hsvValues[1])
        //        .addData("V", "%.3f", hsvValues[2]);
        //telemetry.addLine()
        //        .addData("a", "%.3f", colors.alpha)
        //        .addData("r", "%.3f", colors.red)
        //        .addData("g", "%.3f", colors.green)
        //        .addData("b", "%.3f", colors.blue);
        /** We also display a conversion of the colors to an equivalent Android color integer.
         * @see Color */
        int color = colors.toColor();
        //telemetry.addLine("raw Android color: ")
        //        .addData("a", "%02x", Color.alpha(color))
        //        .addData("r", "%02x", Color.red(color))
        //        .addData("g", "%02x", Color.green(color))
        //        .addData("b", "%02x", Color.blue(color));

        // Balance the colors. The values returned by getColors() are normalized relative to the
        // maximum possible values that the sensor can measure. For example, a sensor might in a
        // particular configuration be able to internally measure color intensity in a range of
        // [0, 10240]. In such a case, the values returned by getColors() will be divided by 10240
        // so as to return a value it the range [0,1]. However, and this is the point, even so, the
        // values we see here may not get close to 1.0 in, e.g., low light conditions where the
        // sensor measurements don't approach their maximum limit. In such situations, the *relative*
        // intensities of the colors are likely what is most interesting. Here, for example, we boost
        // the signal on the colors while maintaining their relative balance so as to give more
        // vibrant visual feedback on the robot controller visual display.
        float max = Math.max(Math.max(Math.max(colors.red, colors.green), colors.blue), colors.alpha);
        colors.red   /= max;
        colors.green /= max;
        colors.blue  /= max;
        color = colors.toColor();

        /*telemetry.addLine("normalized color:  ")
                .addData("a", "%02x", Color.alpha(color))
                .addData("r", "%02x", Color.red(color))
                .addData("g", "%02x", Color.green(color))
                .addData("b", "%02x", Color.blue(color));
        telemetry.update();*/

        // convert the RGB values to HSV values.
        Color.RGBToHSV(Color.red(color), Color.green(color), Color.blue(color), hsvValues);
        // set background to color

        final View relativeLayout = null;
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
            }
        });
        // turn off light on sensor
        if (colorSensor instanceof SwitchableLight) {
            SwitchableLight light = (SwitchableLight)colorSensor;
            light.enableLight(false);
        }
    }
}








