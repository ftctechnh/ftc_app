package org.firstinspires.ftc.teamcode.Autonomous;


import android.graphics.Color;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

// Created by MRINAAL RAMACHANDRAN on 10/8/17
//
//
// Last edit: 10/27/17 BY MRINAAL RAMACHANDRAN

public class Autonomous_Functions {

    // MOTOR NAMES

    protected DcMotor F_L = null;
    protected DcMotor F_R = null;
    protected DcMotor R_L = null;
    protected DcMotor R_R = null;

    public DcMotor clamp = null;

    protected Servo dropper = null;

    ColorSensor colorSensor = null;

    String jewelColor = null;

    float hsvValues[] = {0F,0F,0F};

    IntegratingGyroscope gyro;
    ModernRoboticsI2cGyro modernRoboticsI2cGyro;

    ElapsedTime timer = new ElapsedTime();

    // LOCAL OPMODE MEMBERS
    HardwareMap hwMap = null;

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

        clamp = hwMap.get(DcMotor.class, "clamp");

        colorSensor = hwMap.get(ColorSensor.class, "colorSensor");

        modernRoboticsI2cGyro = hwMap.get(ModernRoboticsI2cGyro.class, "gyro");
        gyro = (IntegratingGyroscope)modernRoboticsI2cGyro;

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

    public void moveMotor(double power, String direction) {

        if (direction == Constants.forward) {

            F_L.setDirection(DcMotor.Direction.REVERSE);
            F_R.setDirection(DcMotor.Direction.FORWARD);
            R_L.setDirection(DcMotor.Direction.REVERSE);
            R_R.setDirection(DcMotor.Direction.FORWARD);

            F_L.setPower(power);
            F_R.setPower(power);
            R_L.setPower(power);
            R_R.setPower(power);
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
        }

        stopMotor();
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

            F_L.setPower(power);
            F_R.setPower(power);
            R_L.setPower(power);
            R_R.setPower(power);


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

            F_L.setPower(power);
            F_R.setPower(power);
            R_L.setPower(power);
            R_R.setPower(power);

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

            F_L.setPower(power);
            F_R.setPower(power);
            R_L.setPower(power);
            R_R.setPower(power);

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

            F_L.setPower(power);
            F_R.setPower(power);
            R_L.setPower(power);
            R_R.setPower(power);

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

            F_L.setPower(power);
            F_R.setPower(power);
            R_L.setPower(power);
            R_R.setPower(power);

            while (F_L.isBusy() ) {

            }
        }
        stopMotor();
    }

    // MOVES MOTOR WITH GYRO
    public void turnMotorUsingGyro (double power, float degrees, String direction) {

        modernRoboticsI2cGyro.calibrate();

        modernRoboticsI2cGyro.resetZAxisIntegrator();

        float zAngle = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;

        if (direction == Constants.spinRight) {

            while (zAngle <= degrees) {

                F_L.setDirection(DcMotor.Direction.FORWARD);
                F_R.setDirection(DcMotor.Direction.FORWARD);
                R_L.setDirection(DcMotor.Direction.FORWARD);
                R_R.setDirection(DcMotor.Direction.FORWARD);

                F_L.setPower(power);
                F_R.setPower(power);
                R_L.setPower(power);
                R_R.setPower(power);

                zAngle = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;

            }  if (zAngle >= degrees) {

                F_L.setPower(0);
                F_R.setPower(0);
                R_L.setPower(0);
                R_R.setPower(0);
            }
        }

        if (direction == Constants.spinLeft) {

            while (zAngle <= degrees) {

                F_L.setDirection(DcMotor.Direction.REVERSE);
                F_R.setDirection(DcMotor.Direction.REVERSE);
                R_L.setDirection(DcMotor.Direction.REVERSE);
                R_R.setDirection(DcMotor.Direction.REVERSE);

                F_L.setPower(power);
                F_R.setPower(power);
                R_L.setPower(power);
                R_R.setPower(power);

                zAngle = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;

            } if (zAngle >= degrees) {

                F_L.setPower(0);
                F_R.setPower(0);
                R_L.setPower(0);
                R_R.setPower(0);
            }
        }
    }

    // SENSES THE JEWEL USING THE COLOR SENSOR
    public String senseJewelColor () {

        String color = "NOTHING";


        Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);

        int red = colorSensor.red();
        int blue = colorSensor.blue();
        int green = colorSensor.green();

        // sense color blue
        if (blue >=2 && blue > red) {

            color = "BLUE";
        }

        // sense color red
        if (red >=2  && red>blue ) {

            color = "RED";
        }

        // Return the color
        return color;
    }

    // KNOCKS OFF THE JEWEL WITH THE "DROPPER" SERVO ARM. USES senseJewelColor
    public void knockOffJewel () {

        dropper.setPosition(0);

        mysleep(500);

        colorSensor.enableLed(true);

        jewelColor = senseJewelColor();

        while(jewelColor=="NOTHING")
        {
            jewelColor = senseJewelColor();

        }
        //sense color

        // if color red turn that way
        if (jewelColor == "RED") {

            moveMotorWithEncoder(.05, 100, Constants.spinRight);
            stopMotor(1000);
            dropper.setPosition(1);
            stopMotor(1000);
            moveMotorWithEncoder(.03, 100, Constants.spinLeft);
            stopMotor(1000);
        }

        // if color blue turn other way
        else if (jewelColor == "BLUE") {

            moveMotorWithEncoder(.05, 100, Constants.spinLeft);
            stopMotor(1000);
            dropper.setPosition(1);
            stopMotor(1000);
            moveMotorWithEncoder(.03, 100, Constants.spinRight);
            stopMotor(1000);
        }

    }


}









