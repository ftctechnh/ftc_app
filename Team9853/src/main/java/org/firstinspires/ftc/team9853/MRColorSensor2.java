package org.firstinspires.ftc.team9853;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Utils for moder robotics color sensors
 */
public class MRColorSensor2 extends ModernRoboticsI2cColorSensor implements ColorSensor {
    // Stateful
    private I2cDeviceSynch sensorReader;

    /*
     * Color Sensor setup
     * @param {I2cDevice} colorSensor   the sensor
     * @param {I2cAddr|int} [address]   the sensors i2c address
     */
    public MRColorSensor2(I2cDevice colorSensor) {
        super(colorSensor.getI2cController(), colorSensor.getPort());

        if(colorSensor.getManufacturer() != Manufacturer.ModernRobotics) {
            throw new IllegalArgumentException("colorSensor needs to be a Modern Robotics Color Sensor");
        }

        this.sensorReader = new I2cDeviceSynchImpl(colorSensor, this.getI2cAddress(), false);

        defaultBehavior();
    }
    public MRColorSensor2(I2cDevice colorSensor, int address) throws IllegalArgumentException {
        this(colorSensor, I2cAddr.create8bit(address));
    }
    public MRColorSensor2(I2cDevice colorSensor, I2cAddr address) throws IllegalArgumentException {
        super(colorSensor.getI2cController(), colorSensor.getPort());

        this.setI2cAddress(address);

        if(colorSensor.getManufacturer() != Manufacturer.ModernRobotics) {
            throw new IllegalArgumentException("colorSensor needs to be a Modern Robotics Color Sensor");
        }

        this.sensorReader = new I2cDeviceSynchImpl(colorSensor, address, false);

        defaultBehavior();
    }

    /*
     * Sets the sensors frequency
     * @param {boolean} setTo60     whether to set to 60. If false sets to 50
     */
    public void setFrequency60(boolean setTo60) {
        if(setTo60) {
            sensorReader.write8(ModernRoboticsI2cColorSensor.ADDRESS_COMMAND, 0x36);
        }
        else {
            sensorReader.write8(ModernRoboticsI2cColorSensor.ADDRESS_COMMAND, 0x35);
        }
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
     * Sets the address of the sensor
     * @param {I2dAddr} newAddress
     */
    public void setI2cAddress(int newAddress) {
        setI2cAddress(I2cAddr.create8bit(newAddress));
    }
    @Override
    public void setI2cAddress(I2cAddr newAddress) {
        super.setI2cAddress(newAddress);

        this.sensorReader.setI2cAddress(newAddress);
    }

    /*
         * Sets default behavior for sensor
         */
    public void defaultBehavior() {
        enableLed(true);
        setFrequency60(true);
    }
}
