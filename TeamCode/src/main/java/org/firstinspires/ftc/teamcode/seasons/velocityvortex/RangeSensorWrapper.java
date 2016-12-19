package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by ftc6347 on 12/11/16.
 */

public class RangeSensorWrapper {
    private I2cDeviceSynch reader;

    private static final int START_REG = 0x04;

    public RangeSensorWrapper(I2cDevice rangeSensor, I2cAddr sensorAddr) {
        this.reader = new I2cDeviceSynchImpl(rangeSensor, sensorAddr, false);
        this.reader.engage();
    }

    public int readUltrasonic() {
        return reader.read(START_REG, 2)[0] & 0xFF;
    }

    public int readOds() {
        return reader.read(START_REG, 2)[1] & 0xFF;
    }
}
