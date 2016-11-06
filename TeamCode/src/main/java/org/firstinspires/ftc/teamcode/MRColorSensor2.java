package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Utils for moder robotics color sensors
 */
public class MRColorSensor2 implements ColorSensor {
    // Stateful
    private ModernRoboticsI2cColorSensor sensor;
    private I2cDeviceSynch sensorReader;

    /*
     * Whether the sensor is in active mode. This is only set after enableLED is called
     */
    public boolean isActive;


    /*
     * Color Sensor setup
     * @param {I2cDevice} colorSensor   the sensor
     * @param {I2cAddr|int} address     the sensors i2c address
     */
    public MRColorSensor2(I2cDevice colorSensor, int address) throws IllegalArgumentException{
        this(colorSensor, I2cAddr.create8bit(address));
    }
    public MRColorSensor2(I2cDevice colorSensor, I2cAddr address) throws IllegalArgumentException{
        if(sensor.getManufacturer() != Manufacturer.ModernRobotics) {
            throw new IllegalArgumentException("colorSensor needs to be a Modern Robotics Color Sensor");
        }

        this.sensor = new ModernRoboticsI2cColorSensor(colorSensor.getI2cController(), colorSensor.getPort());
        this.sensorReader = new I2cDeviceSynchImpl(colorSensor, address, false);
    }

    /*
     * Turns the sensor's led on and off (active and passive mode)
     * @param {boolean} on  should the led be on
     */
    public void enableLed(boolean on) {
        sensor.enableLed(on);
    }

    public void setFrequency60(boolean setTo60) {
        if(setTo60) {
            sensorReader.write8(ModernRoboticsI2cColorSensor.ADDRESS_COMMAND, 0x36);
        }
        else {
            sensorReader.write8(ModernRoboticsI2cColorSensor.ADDRESS_COMMAND, 0x35);
        }
    }

    /*
     * Returns the current red value
     * @return {int}
     */
    public int red() {
        return sensor.red();
    }

    /*
     * Returns the current green value
     */
    public int green() {
        return sensor.green();
    }

    /*
     * Returns the current blue value
     * @return {int}
     */
    public int blue() {
        return sensor.blue();
    }

    /*
     * Returns the current alpha value
     * @return {int}
     */
    public int alpha() {
        return sensor.alpha();
    }

    /*
     * Returns the current argb value (hue)
     * @return {int}
     */
    public int argb() {
        return sensor.alpha();
    }

    /*
     * Returns the current color number of the sensor
     * @see <a href="http://www.modernroboticsinc.com/Content/Images/uploaded/ColorNumber.png">Color number chart</a>
     * @return {int} colorNumber
     */
    public int getColorNumber() {
        return sensorReader.read(ModernRoboticsI2cColorSensor.ADDRESS_COLOR_NUMBER, 1)[1] & 0xFF;
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
     * Returns the sensor's connection information
     * @return {String} connectionInfo
     */
    public String getConnectionInfo() {
        return sensor.getConnectionInfo();
    }

    /*
     * Returns the sensor's manufacturer
     * @return {Manufacturer} manufacturer
     */
    public Manufacturer getManufacturer() {
        return sensor.getManufacturer();
    }

    /*
     * Returns the sensor's current I2c address
     */
    public I2cAddr getI2cAddress() {
        return sensor.getI2cAddress();
    }

    /*
     * Returns the sensor's name
     */
    public String getDeviceName() {
        return sensor.getDeviceName();
    }

    /*
     * Returns the sensor's version
     */
    public int getVersion() {
        return sensor.getVersion();
    }

    /*
     * Sets the sensors address
     * @param {I2cAddr} address    the new address
     */
    public void setI2cAddress(I2cAddr address) {
        sensor.setI2cAddress(address);
    }

    /**
     * Resets the device's configuration to that which is expected at the beginning of an OpMode.
     */
    public void resetDeviceConfigurationForOpMode() {
        defaultBehavior();
        sensor.resetDeviceConfigurationForOpMode();
    }

    /*
     * Sets default behavior for sensor
     */
    public void defaultBehavior() {
        enableLed(true);
        setFrequency60(true);
    }

    /**
     * Closes this device
     */
    public void close() {
        sensor.close();
    }
}
