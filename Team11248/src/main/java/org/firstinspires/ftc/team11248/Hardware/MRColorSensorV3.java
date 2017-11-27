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
    private byte[] colorCache;

    private int colorNumber;


        /**
         * Creates an MRColorSensorV3 object (connects to a Modern Robotics Color Sensor as an I2C device)
         * colorSynch holds and updates all values of the sensor through its I2C registry
         * @param color_sensor - a color sensor declared from the hardwareMap as an I2C device
         * @param COLOR_SENSOR_ADDR - the I2C address of the color sensor
         */

    public MRColorSensorV3(I2cDevice color_sensor, byte COLOR_SENSOR_ADDR){

        this.colorSynch = new I2cDeviceSynchImpl(color_sensor, I2cAddr.create8bit(COLOR_SENSOR_ADDR), false);
        this.colorSynch.engage();

    }

    public void update(){
        colorCache = colorSynch.read(0x04, 1);
        colorNumber = (colorCache[0] & 0XFF);
    }

    public void writeData(int ireg, int bVal){
        colorSynch.write8(ireg, bVal);

          /*
            Address	Function
            0x03	Command
            0x04	Color Number
            0x05	Red Value
            0x06	Green Value
            0x07	Blue Value
            0x08	White Value
         */
    }

        /*
         * Activates colorSensor (Active mode senses color of static non light emmiting object)
         * aka. turns colorSensor led on
         */

    public void setActiveMode(){ //Senses color
        colorSynch.write8(3,0);
    }


        /*
         * Turns the colorSensor passive (Passive mode senses color of light emmiting from objects i.e. beacons)
         * aka. turns colorSensor led off
         */

    public void setPassiveMode(){ //Senses light
        colorSynch.write8(3,1);
    }

        /**
         * @return and int corresponding to the color being seen by the colorSensor
         * http://modernroboticsinc.com/color-sensor
         */
    public int getColorNumber(){
        this.update();
        return colorNumber;
    }

    public double red(){
        byte[] color = colorSynch.read(0x05, 1);
        return (color[0] & 0XFF);
    }

    public double green(){
        byte[] color = colorSynch.read(0x06, 1);
        return (color[0] & 0XFF);
    }

    public double blue(){
        byte[] color = colorSynch.read(0x07, 1);
        return (color[0] & 0XFF);
    }
}
