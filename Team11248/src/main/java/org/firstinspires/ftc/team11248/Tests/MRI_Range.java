/*
Modern Robotics Range Sensor Example
Created 9/8/2016 by Colton Mehlhoff of Modern Robotics using FTC SDK 2.x Beta
Reuse permitted with credit where credit is due

Configuration:
I2cDevice on an Interface Module named "range" at the default address of 0x28 (0x14 7-bit)

This program can be run without a battery and Power Destitution Module.

For more information, visit modernroboticsedu.com.
Support is available by emailing support@modernroboticsinc.com.
*/

package org.firstinspires.ftc.team11248.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

@TeleOp(name = "Range", group = "MRI")
@Disabled
public class MRI_Range extends OpMode {

    byte[] range1Cache; //The read will return an array of bytes. They are stored in this variable

    I2cAddr RANGE1ADDRESS = new I2cAddr(0x14); //Default I2C address for MR Range (7-bit)
    public static final int RANGE1_REG_START = 0x04; //Register to start reading
    public static final int RANGE1_READ_LENGTH = 2; //Number of byte to read

    public I2cDevice RANGE1;
    public I2cDeviceSynch RANGE1Reader;

    @Override
    public void init() {
        RANGE1 = hardwareMap.i2cDevice.get("range");
        RANGE1Reader = new I2cDeviceSynchImpl(RANGE1, RANGE1ADDRESS, false);
        RANGE1Reader.engage();
    }

    @Override
    public void loop() {
        range1Cache = RANGE1Reader.read(RANGE1_REG_START, RANGE1_READ_LENGTH);

        telemetry.addData("Ultra Sonic", range1Cache[0] & 0xFF);
        telemetry.addData("ODS", range1Cache[1] & 0xFF);
    }

    @Override
    public void stop() {

    }

}