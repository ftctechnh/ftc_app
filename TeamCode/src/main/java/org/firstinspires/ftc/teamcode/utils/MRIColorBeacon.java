/*
DO NOT EDIT THIS PROGRAM

Modern Robotics Color Beacon Driver
Created 12/7/2016 by Colton Mehlhoff of Modern Robotics using FTC SDK 2.35
Reuse permitted with credit where credit is due

This class provides functions to use the Color Beacon http://modernroboticsinc.com/color-beacon
Support is available by emailing support@modernroboticsinc.com
*/

package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

public class MRIColorBeacon {

    private byte[] colorBcache;

    private I2cDevice colorB;
    private I2cDeviceSynch colorBreader;

    HardwareMap hwMap = null;


    public MRIColorBeacon() {

    }

    public void init(HardwareMap ahwMap, String cfgName) {
        init(ahwMap, cfgName, 0x4c); //Default I2C address for color beacon is 0x4c
    }

    public void init(HardwareMap ahwMap, String cfgName, int i2cAddr8) {

        hwMap = ahwMap;

        colorB = hwMap.i2cDevice.get(cfgName);

        colorBreader = new I2cDeviceSynchImpl(colorB, I2cAddr.create8bit(i2cAddr8), false);

        colorBreader.engage();
    }

    public void off() {
        colorBreader.write8(4, 0);
    }

    public void red() {
        colorBreader.write8(4, 1);
    }

    public void green() {
        colorBreader.write8(4, 2);
    }

    public void yellow() {
        colorBreader.write8(4, 3);
    }

    public void blue() {
        colorBreader.write8(4, 4);
    }

    public void purple() {
        colorBreader.write8(4, 5);
    }

    public void teal() {
        colorBreader.write8(4, 6);
    }

    public void white() {
        colorBreader.write8(4, 7);
    }

    public void rgb(int red, int green, int blue) {
        colorBreader.write8(4, 8); //Custom Color Mode
        colorBreader.write8(5, red);
        colorBreader.write8(6, green);
        colorBreader.write8(7, blue);
    }

    public int getColorNumber(){
        colorBcache = colorBreader.read(0x04, 4);

        return colorBcache[0];
    }

    public String getColor() {
        colorBcache = colorBreader.read(0x04, 4);

        String returnString = "UNKNOWN";

        switch (colorBcache[0]) {
            case 0:
                returnString = "OFF";
                break;
            case 1:
                returnString = "RED";
                break;
            case 2:
                returnString = "GREEN";
                break;
            case 3:
                returnString = "YELLOW";
                break;
            case 4:
                returnString = "BLUE";
                break;
            case 5:
                returnString = "PURPLE";
                break;
            case 6:
                returnString = "TEAL";
                break;
            case 7:
                returnString = "WHITE";
                break;
            case 8:
                returnString = "rgb " + ((int) colorBcache[1] & 0xFF) + " " + ((int) colorBcache[2] & 0xFF) + " " + ((int) colorBcache[3] & 0xFF);
                break;
            default:
                returnString = "UNKNOWN";
                break;
        }

        return returnString;
    }

    public void colorNumber(int number) {
        number = number % 7;

        switch (number) {
            case 0:
                off();
                break;
            case 1:
                red();
                break;
            case 2:
                green();
                break;
            case 3:
                yellow();
                break;
            case 4:
                blue();
                break;
            case 5:
                purple();
                break;
            case 6:
                teal();
                break;
            case 7:
                white();
                break;
            default:
                off();
                break;
        }
    }

}

