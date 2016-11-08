package org.chathamrobotics.ftcutils;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * adds more functionality to the modern robotics color sensor
 */
public class MRColorSensorV2 extends ModernRoboticsI2cColorSensor implements ColorSensor{
    // Stateful
    private I2cDeviceSynch sensorSynch;
    private volatile int frequency;
    private volatile boolean active;
    private volatile int lastCommand;

    /*
     * Creates new MRColorSensorV2
     * @param {I2cDevice} sensor        The I2cDevice instance of the sensor
     * @param {I2cAddr|int} [address]   The sensors address
     */
    public MRColorSensorV2(I2cDevice sensor) {
        // Create a MR color sensor
        super(sensor.getI2cController(), sensor.getPort());

        if(! isMRColorSensor(this)) {
            throw new IllegalArgumentException("MRColorSensorV2 only accepts Modern Robotics color sensors.");
        }

        // Create I2c synchronizer
        this.sensorSynch = new I2cDeviceSynchImpl(sensor, this.getI2cAddress(), false);

        defaultBehavior();
    }
    public MRColorSensorV2(I2cDevice sensor, int address) {
        this(sensor, I2cAddr.create8bit(address));
    }
    public MRColorSensorV2(I2cDevice sensor, I2cAddr address) {
        // Create a MR color sensor
        super(sensor.getI2cController(), sensor.getPort());

        if(! isMRColorSensor(this)) {
            throw new IllegalArgumentException("MRColorSensorV2 only accepts Modern Robotics color sensors.");
        }

        // Make sure the sensor is using the right address
        this.setI2cAddress(address);

        // Create I2c synchronizer
        this.sensorSynch = new I2cDeviceSynchImpl(sensor, address, false);

        defaultBehavior();
    }

    /*
     * Determine if the color sensor is a modern robotics color sensor
     * @param {ColorSensor} sensor  the sensor to test
     * @return {boolean} isMrColorSensor
     */
    public boolean isMRColorSensor(ColorSensor sensor) {
        return sensor.getManufacturer() == Manufacturer.ModernRobotics;
    }

    /*
     * Returns the current color number of the sensor
     * @see <a href="http://www.modernroboticsinc.com/Content/Images/uploaded/ColorNumber.png">Color number chart</a>
     * @return {int} colorNumber
     */
    public int getColorNumber() {
        return this.sensorSynch.read(ADDRESS_COMMAND, 1)[1] & 0xFF;
    }

    /*
     * Returns true if the color is black
     * @return {boolean}
     */
    public boolean isBlack() {return getColorNumber() == 0;}

    /*
     * Returns true if the color is white
     * @return {boolean}
     */
    public boolean isWhite() {return getColorNumber() == 16;}

    /*
     * Returns true if the color is red
     * @return {boolean}
     */
    public boolean isRed() {return getColorNumber() == 10;}

    /*
     * Returns true if the color is green
     * @return {boolean}
     */
    public boolean isGreen() {return getColorNumber() == 5;}

    /*
     * Returns true if the color is blue
     * @return {boolean}
     */
    public boolean isBlue() {return getColorNumber() == 3;}

    /*
     * Sets the address of the sensor
     * @param {I2dAddr|int} newAddress
     */
    public void setI2cAddress(int newAddress) {
        setI2cAddress(I2cAddr.create8bit(newAddress));
    }
    @Override
    public void setI2cAddress(I2cAddr newAddress) {
        super.setI2cAddress(newAddress);

        this.sensorSynch.setI2cAddress(newAddress);
    }

    @Override
    public synchronized void enableLed(boolean enable) {
        active = ! active;

        super.enableLed(enable);
    }

    /*
     * Sets the sensors frequency
     * @param {int} freq     the frequency to set the senor to. Only 60 and 50 are supported
     */
    public void setFrequency(int freq) {
        if(freq != 60 && freq != 50) {
            throw new IllegalArgumentException("Sorry as of current only 60hz and 50hz are supported frequencies.");
        }

        int command = freq == 60 ? 0x36 : 0x35;

        // Why do this again if I just did it
        if(command != lastCommand) {
            lastCommand = command;
            frequency = freq;
            sensorSynch.write8(ADDRESS_COMMAND, command);
        }
    }

    /*
     * Gets the current frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /*
     * Whether the sensor is active or not
     */
    public boolean isActive() {
        return active;
    }
    /*
     * Sets sensor to default behavior
     */
    public void defaultBehavior() {
        enableLed(true);
        setFrequency(60);
    }
}
