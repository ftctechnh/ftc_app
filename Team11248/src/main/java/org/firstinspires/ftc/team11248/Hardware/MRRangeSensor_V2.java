package org.firstinspires.ftc.team11248.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by Tony_Air on 11/26/17.
 */

public class MRRangeSensor_V2 {

    private I2cDeviceSynch rangeSynch;


    /**
     * Creates an MRRangeSensor_V2 object (connects to a Modern Robotics Range Sensor as an I2C device)
     * rangeSynch holds and updates all values of the sensor through its I2C registry
     * @param rangeSensor - a range sensor declared from the hardwareMap as an I2C device
     * @param SENSOR_ADDR - the I2C address of the color sensor
     */

    /*
     * 0x00   Firmware Version
     * 0x01   Manufacturer Code
     * 0x02   Sensor ID
     * 0x04   Ultrasonic Distance Reading (cm)
     * 0x05   Optical Distance Reading
     */

    public MRRangeSensor_V2 (I2cDevice rangeSensor, byte SENSOR_ADDR){
        this.rangeSynch = new I2cDeviceSynchImpl(rangeSensor, I2cAddr.create8bit(SENSOR_ADDR), false);
        this.rangeSynch.engage();
    }

    /**
     * @return and int representing distance in cm (5 - 255 cm)
     */
    public int ultrasonicValue(){
        byte[] val = rangeSynch.read(0x04, 1);
        return (val[0] & 0XFF);
    }

    /**
     * @return and int representing distance when closer than 5 cm
     */
    public int ODSValue(){
        byte[] val = rangeSynch.read(0x05, 1);
        return (val[0] & 0XFF);
    }

}
