package com.qualcomm.ftcrobotcontroller.FreshClasses;

import android.graphics.Color;

import com.qualcomm.ftcrobotcontroller.opmodes.TheFreshMenAuton;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.PWMOutput;

/**
 * Created by Naisan on 4/16/2016.
 */
public class FreshMethods {

    public static void approach_ir_signal(FreshMotors motors, FreshSensors sensors, FreshServos servos, double end_signal_strength) { //this method will be called only if an ir signal is detected
        double angle = sensors.irSensor.getAngle();
        double signal_strength = sensors.irSensor.getStrength();
        while (angle < -3) { //if the ir signal is to the left, slide to the left
            motors.M_backLeft.setPower(1);
            motors.M_backRight.setPower(1);
            motors.M_frontLeft.setPower(-1);
            motors.M_frontRight.setPower(-1);
            angle = sensors.irSensor.getAngle();
        }
        while (angle > 3) { //if the ir signal is to the right, slide to the right
            motors.M_backLeft.setPower(-1);
            motors.M_backRight.setPower(-1);
            motors.M_frontLeft.setPower(1);
            motors.M_frontRight.setPower(1);
            angle = sensors.irSensor.getAngle();
        }
        while (signal_strength < end_signal_strength) { //if the ir signal is too far away to dump, get closer
            motors.M_backLeft.setPower(-1);
            motors.M_backRight.setPower(1);
            motors.M_frontLeft.setPower(-1);
            motors.M_frontRight.setPower(1);
            signal_strength = sensors.irSensor.getStrength();
        }
        while (signal_strength > end_signal_strength) { //if the ir signal is too far away to dump, get farther away
            motors.M_backLeft.setPower(1);
            motors.M_backRight.setPower(-1);
            motors.M_frontLeft.setPower(1);
            motors.M_frontRight.setPower(-1);
            signal_strength = sensors.irSensor.getStrength();
        }
        motors.M_backLeft.setPower(0); //if all the conditions are met, do nothing
        motors.M_backRight.setPower(0);
        motors.M_frontLeft.setPower(0);
        motors.M_frontRight.setPower(0);
    }
    public static void follow_colored_line(FreshMotors motors, FreshSensors sensors, FreshServos servos, float lineHsvValues[], long time_to_run_ms) {
        float leftHsvValues[] = {0, 0, 0};
        float rightHsvValues[] = {0, 0, 0};
        Color.RGBToHSV(sensors.colorSensorLeft.red() * 8, sensors.colorSensorLeft.green() * 8, sensors.colorSensorLeft.blue() * 8, leftHsvValues);
        Color.RGBToHSV(sensors.colorSensorRight.red() * 8, sensors.colorSensorRight.green() * 8, sensors.colorSensorRight.blue() * 8, rightHsvValues);
       long start_time = System.currentTimeMillis();
       long end_time = start_time + time_to_run_ms;
        while (System.currentTimeMillis() < end_time) {
            if ((rightHsvValues[0] >= (lineHsvValues[0] - 5) && rightHsvValues[0] <= (lineHsvValues[0] + 5)) &&
                    (rightHsvValues[1] >= (lineHsvValues[1] - 5) && rightHsvValues[1] <= (lineHsvValues[1] + 5)) &&
                    (rightHsvValues[2] >= (lineHsvValues[2] - 5) && rightHsvValues[2] <= (lineHsvValues[0] + 5))) { //if the right color sensor sees the line, turn to the right
                motors.M_backLeft.setPower(-1);
                motors.M_backRight.setPower(0.75);
                motors.M_frontLeft.setPower(-0.75);
                motors.M_frontRight.setPower(1);
                Color.RGBToHSV(sensors.colorSensorLeft.red() * 8, sensors.colorSensorLeft.green() * 8, sensors.colorSensorLeft.blue() * 8, leftHsvValues);
                Color.RGBToHSV(sensors.colorSensorRight.red() * 8, sensors.colorSensorRight.green() * 8, sensors.colorSensorRight.blue() * 8, rightHsvValues);
            } else if ((leftHsvValues[0] >= (lineHsvValues[0] - 5) && leftHsvValues[0] <= (lineHsvValues[0] + 5)) &&
                    (leftHsvValues[1] >= (lineHsvValues[1] - 5) && leftHsvValues[1] <= (lineHsvValues[1] + 5)) &&
                    (leftHsvValues[2] >= (lineHsvValues[2] - 5) && leftHsvValues[2] <= (lineHsvValues[0] + 5))) { //if the left color sensor sees the line, turn to the left
                motors.M_backLeft.setPower(-0.75);
                motors.M_backRight.setPower(1);
                motors.M_frontLeft.setPower(-1);
                motors.M_frontRight.setPower(0.75);
                Color.RGBToHSV(sensors.colorSensorLeft.red() * 8, sensors.colorSensorLeft.green() * 8, sensors.colorSensorLeft.blue() * 8, leftHsvValues);
                Color.RGBToHSV(sensors.colorSensorRight.red() * 8, sensors.colorSensorRight.green() * 8, sensors.colorSensorRight.blue() * 8, rightHsvValues);
            } else {
                motors.M_backLeft.setPower(-1);
                motors.M_backRight.setPower(1);
                motors.M_frontLeft.setPower(-1);
                motors.M_frontRight.setPower(1);
                Color.RGBToHSV(sensors.colorSensorLeft.red() * 8, sensors.colorSensorLeft.green() * 8, sensors.colorSensorLeft.blue() * 8, leftHsvValues);
                Color.RGBToHSV(sensors.colorSensorRight.red() * 8, sensors.colorSensorRight.green() * 8, sensors.colorSensorRight.blue() * 8, rightHsvValues);
            }
        }
    }
    public static void forward_until_colored_line(FreshMotors motors, FreshSensors sensors, FreshServos servos, float targetHsvValues[]) {
        float leftHsvValues[] = {0,0,0};
        float rightHsvValues[] = {0,0,0};
        Color.RGBToHSV(sensors.colorSensorLeft.red() * 8, sensors.colorSensorLeft.green() * 8, sensors.colorSensorLeft.blue() * 8, leftHsvValues);
        Color.RGBToHSV(sensors.colorSensorRight.red() * 8, sensors.colorSensorRight.green() * 8, sensors.colorSensorRight.blue() * 8, rightHsvValues);
            while ((leftHsvValues[0] <= (targetHsvValues[0] - 5) || leftHsvValues[0] >= (targetHsvValues[0] + 5))&&
                    (leftHsvValues[1] <= (targetHsvValues[1] - 5) || leftHsvValues[1] >= (targetHsvValues[1] + 5)) &&
                    (leftHsvValues[2] <= (targetHsvValues[2] - 5) || leftHsvValues[2] >= (targetHsvValues[0] + 5))) {

                if ((rightHsvValues[0] >= (targetHsvValues[0] - 5) && rightHsvValues[0] <= (targetHsvValues[0] + 5))&&
                        (rightHsvValues[1] >= (targetHsvValues[1] - 5) && rightHsvValues[1] <= (targetHsvValues[1] + 5)) &&
                        (rightHsvValues[2] >= (targetHsvValues[2] - 5) && rightHsvValues[2] <= (targetHsvValues[0] + 5))) {
                    motors.M_backLeft.setPower(-1);
                    motors.M_frontLeft.setPower(-1);
                }
                else {
                        motors.M_backLeft.setPower(-1);
                        motors.M_backRight.setPower(1);
                        motors.M_frontLeft.setPower(-1);
                        motors.M_frontRight.setPower(1);
                    }
                Color.RGBToHSV(sensors.colorSensorLeft.red() * 8, sensors.colorSensorLeft.green() * 8, sensors.colorSensorLeft.blue() * 8, leftHsvValues);
                Color.RGBToHSV(sensors.colorSensorRight.red() * 8, sensors.colorSensorRight.green() * 8, sensors.colorSensorRight.blue() * 8, rightHsvValues);
            }
        while ((rightHsvValues[0] <= (targetHsvValues[0] - 5) || rightHsvValues[0] >= (targetHsvValues[0] + 5))&&
                (rightHsvValues[1] <= (targetHsvValues[1] - 5) || rightHsvValues[1] >= (targetHsvValues[1] + 5)) &&
                (rightHsvValues[2] <= (targetHsvValues[2] - 5) || rightHsvValues[2] >= (targetHsvValues[0] + 5))) {

            if ((leftHsvValues[0] >= (targetHsvValues[0] - 5) && leftHsvValues[0] <= (targetHsvValues[0] + 5))&&
                    (leftHsvValues[1] >= (targetHsvValues[1] - 5) && leftHsvValues[1] <= (targetHsvValues[1] + 5)) &&
                    (leftHsvValues[2] >= (targetHsvValues[2] - 5) && leftHsvValues[2] <= (targetHsvValues[0] + 5))) {
                motors.M_backRight.setPower(-1);
                motors.M_frontRight.setPower(-1);
            }
                else {
                    motors.M_backLeft.setPower(-1);
                    motors.M_backRight.setPower(1);
                    motors.M_frontLeft.setPower(-1);
                    motors.M_frontRight.setPower(1);
                }
            Color.RGBToHSV(sensors.colorSensorLeft.red() * 8, sensors.colorSensorLeft.green() * 8, sensors.colorSensorLeft.blue() * 8, leftHsvValues);
            Color.RGBToHSV(sensors.colorSensorRight.red() * 8, sensors.colorSensorRight.green() * 8, sensors.colorSensorRight.blue() * 8, rightHsvValues);
        }
    }

    public static void distance_with_encoders(FreshMotors motors, FreshSensors sensors, FreshServos servos, double distance) { //this probably doesn't work with Omniwheels
        final int  ENCODER_CPR = 1120;
        final double WHEEL_CIRCUMFERENCE = 9.42;
        double rotations_to_destination = distance / WHEEL_CIRCUMFERENCE;
        double counts_to_destination = rotations_to_destination * ENCODER_CPR;
        motors.M_backLeft.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motors.M_backRight.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motors.M_frontLeft.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motors.M_frontRight.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motors.M_backLeft.setTargetPosition((int) counts_to_destination);
        motors.M_backRight.setTargetPosition((int) counts_to_destination);
        motors.M_frontLeft.setTargetPosition((int) counts_to_destination);
        motors.M_frontRight.setTargetPosition((int) counts_to_destination);
        motors.M_backLeft.setPower(50);
        motors.M_backRight.setPower(50);
        motors.M_frontLeft.setPower(50);
        motors.M_frontRight.setPower(50);
    }
    public int colorRed (FreshSensors sensors){
        return sensors.colorSensorRight.red();
    }
    public int colorBlue (FreshSensors sensors){
        return sensors.colorSensorRight.blue();
    }
    public int colorGrey (FreshSensors sensors){
        return sensors.colorSensorRight.argb();
    }
    public void simpleColorStopRed (FreshSensors sensors, FreshMotors motors,float Power,int red){
        boolean reached = false;
        boolean left = false;
        boolean right = false;
        motors.M_backLeft.setPower(-Power);
        motors.M_backRight.setPower(Power);
        motors.M_frontLeft.setPower(-Power);
        motors.M_frontRight.setPower(Power);
        while (!reached){
            if (sensors.colorSensorRight.red()>= red){
                right = true;
                motors.M_backRight.setPower(0);
                motors.M_frontRight.setPower(0);
            }
            if (sensors.colorSensorLeft.red()>= red){
                left = true;
                motors.M_backLeft.setPower(0);
                motors.M_frontLeft.setPower(0);
            }
            if (right){
                if(left){
                    reached = true;
                }
            }

        }
    }
    public void simpleColorStopBlue (FreshSensors sensors, FreshMotors motors,float Power,int blue){
        boolean reached = false;
        boolean left = false;
        boolean right = false;
        motors.M_backLeft.setPower(-Power);
        motors.M_backRight.setPower(Power);
        motors.M_frontLeft.setPower(-Power);
        motors.M_frontRight.setPower(Power);
        while (!reached){
            if (sensors.colorSensorRight.blue()>= blue){
                right = true;
                motors.M_backRight.setPower(0);
                motors.M_frontRight.setPower(0);
            }
            if (sensors.colorSensorLeft.blue()>= blue){
                left = true;
                motors.M_backLeft.setPower(0);
                motors.M_frontLeft.setPower(0);
            }
            if (right){
                if(left){
                    reached = true;
                }
            }

        }
    }
    public void PID_ColorFollow (FreshSensors sensors, FreshMotors motors,int red,int blue){


    }
    //Run Commands
    public void moveForward(FreshMotors motors,float power){
        motors.M_backLeft.setPower(power);
        motors.M_backRight.setPower(power);
        motors.M_frontRight.setPower(power);
        motors.M_frontLeft.setPower(power);
    }
    public void moveBack(FreshMotors motors,float power){
        motors.M_backLeft.setPower(power);
        motors.M_backRight.setPower(power);
        motors.M_frontRight.setPower(power);
        motors.M_frontLeft.setPower(power);
    }
    public void moveLeft(FreshMotors motors,float power){
        motors.M_backLeft.setPower(power);
        motors.M_backRight.setPower(power);
        motors.M_frontLeft.setPower(power);
        motors.M_frontRight.setPower(power);

    }
    public void moveRight(FreshMotors motors,float power){

    }
}