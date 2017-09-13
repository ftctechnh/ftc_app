package org.firstinspires.ftc.rick;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by Chris D on 10/5/2016.
 *
 * Partially based on:
 * https://github.com/OliviliK/FTC_Library/blob/master/TCS34725_ColorSensor.java
 */
public class MultiplexColorSensor {

    // MUX Registers
    static final int ENABLE = 0x80;
    static final int ATIME = 0x81;
    static final int CONTROL = 0x8F;
    static final int ID = 0x92;
    static final int STATUS = 0x93;
    static final int CDATAL = 0x94;

    // Default I2C address for multiplexer. The address can be changed to any
    // value from 0x70 to 0x77, so this line would need to be changed if a
    // non-default address is to be used.
    static final I2cAddr MUX_ADDRESS = new I2cAddr(0x70);
    private I2cDevice mux;
    private I2cDeviceSynch muxReader;

    // Only one color sensor is needed in code as the multiplexer switches
    // between the physical sensors
    private byte[] adaCache;
    // I2C address for color sensor
    static final I2cAddr ADA_ADDRESS = new I2cAddr(0x29);
    private I2cDevice ada;
    private I2cDeviceSynch adaReader;

    private int[] sensorPorts;

    public static int GAIN_1X =  0x00;
    public static int GAIN_4X =  0x01;
    public static int GAIN_16X = 0x02;
    public static int GAIN_60X = 0x03;

    /**
     * Initializes Adafruit color sensors on the specified ports of the I2C
     * multiplexer.
     *
     * @param hardwareMap  hardwareMap from OpMode
     * @param muxName      Configuration name of I2CDevice for multiplexer
     * @param colorName    Configuration name of I2CDevice for color sensor
     * @param ports        Out ports on multiplexer with color sensors attached
     * @param milliSeconds Integration time in milliseconds
     * @param gain         Gain (GAIN_1X, GAIN_4X, GAIN_16X, GAIN_60X)
     */
    public MultiplexColorSensor(HardwareMap hardwareMap,//constructor
                                String muxName,
                                String colorName,
                                int[] ports,
                                double milliSeconds,
                                int gain) {
        sensorPorts = ports;

        mux = hardwareMap.i2cDevice.get(muxName);
        muxReader = new I2cDeviceSynchImpl(mux, MUX_ADDRESS, false);
        muxReader.engage();

        // Loop over the ports activating each color sensor
        for (int i = 0; i < sensorPorts.length; i++) {
            // Write to given output port on the multiplexer
            muxReader.write8(0x0, 1 << sensorPorts[i], true);

            ada = hardwareMap.i2cDevice.get(colorName);
            adaReader = new I2cDeviceSynchImpl(ada, ADA_ADDRESS, false);
            adaReader.engage();

            final int time = integrationByte(milliSeconds);
            adaReader.write8(ENABLE, 0x03, true);  // Power on and enable ADC
            adaReader.read8(ID);                   // Read device ID
            adaReader.write8(CONTROL, gain, true); // Set gain
            adaReader.write8(ATIME, time, true);   // Set integration time
        }
    }

    /**
     * Set the integration time on all the color sensors
     * @param milliSeconds Time in millseconds
     */
    public void setIntegrationTime(double milliSeconds) {
        int val = integrationByte(milliSeconds);

        for (int i = 0; i < sensorPorts.length; i++) {
            muxReader.write8(0x0, 1 << sensorPorts[i], true);
            adaReader.write8(ATIME, val, true);
        }
    }

    private int integrationByte(double milliSeconds) {
        int count = (int)(milliSeconds/2.4);
        if (count<1)    count = 1;   // Clamp the time range
        if (count>256)  count = 256;
        return (256 - count);
    }

    // Un-needed?
    public void startPolling() {
        for (int i = 0; i < sensorPorts.length; i++) {
            muxReader.write8(0x0, 1 << sensorPorts[i], true);
            adaReader.read8(STATUS);
        }
    }

    /**
     * Retrieve the color read by the given color sensor
     *
     * @param port Port on multiplexer of given color sensor
     * @return Array containing the Clear, Red, Green, and Blue color values
     */
    public int[] getCRGB(int port) {
        // Write to I2C port on the multiplexer
        muxReader.write8(0x0, 1 << port, true);

        // Read color registers
        adaCache = adaReader.read(CDATAL, 8);

        // Combine high and low bytes
        int[] crgb = new int[4];
        for (int i=0; i<4; i++) {
            crgb[i] = (adaCache[2*i] & 0xFF) + (adaCache[2*i+1] & 0xFF) * 256;
        }
        return crgb;
    }
}