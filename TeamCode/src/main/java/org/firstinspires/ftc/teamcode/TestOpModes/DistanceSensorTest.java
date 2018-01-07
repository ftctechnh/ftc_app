package org.firstinspires.ftc.teamcode.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;

/**
 * Created by pston on 1/3/2018
 */

@TeleOp(name = "Range Sensor Test", group = "Sensor Test")
public class DistanceSensorTest extends OpMode {

    private Robot robot;

    private byte[] range1Cache; //The read will return an array of bytes. They are stored in this variable

    private I2cAddr RANGE1ADDRESS = new I2cAddr(0x14); //Default I2C address for MR Range (7-bit)
    private static final int RANGE1_REG_START = 0x04; //Register to start reading
    private static final int RANGE1_READ_LENGTH = 2; //Number of byte to read

    private I2cDevice RANGE1;
    private I2cDeviceSynch RANGE1Reader;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);

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
}
