package org.firstinspires.ftc.team11248.Hardware;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by Tony_Air on 12/6/16.
 */

public class MRColorSensorV3 {

    private I2cDeviceSynch colorSynch;

    private final int COLOR_THRESHOLD = 10;


        /**
         * Creates an MRColorSensorV3 object (connects to a Modern Robotics Color Sensor as an I2C device)
         * colorSynch holds and updates all values of the sensor through its I2C registry
         * @param color_sensor - a color sensor declared from the hardwareMap as an I2C device
         * @param COLOR_SENSOR_ADDR - the I2C address of the color sensor
         */

        /*
            Address	Function
            0x00 Sensor Firmware Revision
            0x01 Manufacturer Code
            0x02 Sensor ID Code
            0x03 Command
            0x04 Color Number
            0x05 Red Value
            0x06 Green Value
            0x07 Blue Value
            0x08 White Value
            0x09 Color Index Number
            0x0A Red Index
            0x0B Green Index
            0x0C Blue Index
            0x0D Undefined
            0x0E/0x0F Red Reading (lsb/msb)
            0x10/0x11 Green Reading (lsb/msb)
            0x12/0x13 Blue Reading (lsb/msb)
            0x14/0x15 White Reading (lsb/msb)
            0x16/0x17 Normalized Red Reading (lsb/msb)
            0x18/0x19 Normalized Green Reading (lsb/msb)
            0x1A/0x1B Normalized Blue Reading (lsb/msb)
            0x1C/0x1D Normalized White Reading (lsb/msb)
         */

    public MRColorSensorV3(I2cDevice color_sensor, byte COLOR_SENSOR_ADDR){

        this.colorSynch = new I2cDeviceSynchImpl(color_sensor, I2cAddr.create8bit(COLOR_SENSOR_ADDR), false);
        this.colorSynch.engage();

    }

    /**
     * Writes data to command register 0x03
     * 0x00 Active Mode (LED ON)
     * 0x01 Passive Mode (LED OFF)
     * 0x35 50Hz Operating Frequency
     * 0x36 60Hz Operating Frequency
     * 0x42 Black Level Calibration
     * 0x43 White Balance Calibration
     * @param bVal - value being written to command register
     */
    public void writeData(int bVal){
        colorSynch.write8(3, bVal);
    }

        /*
         * Activates/ deactivates colorSensor
         * (Active mode senses color of static non light emmiting object)
         * (Passive mode senses color of light emmiting object)
         * aka. turns colorSensor led on/off
         */

    public void enableLed(boolean on){ //Senses color
        writeData(on?0:1);
    }



        /**
         * @return and int corresponding to the color being seen by the colorSensor
         * http://modernroboticsinc.com/color-sensor
         */
    public int getColorNumber(){
        byte[] val = colorSynch.read(0x04, 1);
        return (val[0] & 0XFF);
    }

    public double red(){
        byte[] color = colorSynch.read(0x16, 1);
        return (color[0] & 0XFF);
    }

    public double green(){
        byte[] color = colorSynch.read(0x18, 1);
        return (color[0] & 0XFF);
    }

    public double blue(){
        byte[] color = colorSynch.read(0x1A, 1);
        return (color[0] & 0XFF);
    }

    public boolean isBlue(){
        return (blue() > (red() + COLOR_THRESHOLD));
    }

    public boolean isRed(){
        return ((blue() + COLOR_THRESHOLD) < red());
    }
}
