package org.firstinspires.ftc.teamcode.libraries.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cWaitControl;
import com.qualcomm.robotcore.hardware.TimestampedData;
import com.qualcomm.robotcore.hardware.TimestampedI2cData;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * Created by Noah on 1/31/2018.
 *
 * I2C Driver for the Matbotix ultrasonic sensor
 */

public class MatbotixUltra {
    private enum Regs {
        RANGE_TAKE(0b01010001),
        RANGE_READ(0b00000000),
        ADDR_UNLOCK(170);


        public final byte REG;
        Regs(int reg) {
            this.REG = (byte)reg;
        }
    }

    private static final byte ADDR = 112;

    private final I2cDeviceSynch sensor;
    private final int waitMillis;

    private long lastTime;

    public MatbotixUltra(I2cDeviceSynch sensor, int waitMillis) {
        this.sensor = sensor;
        this.waitMillis = waitMillis;
    }

    public MatbotixUltra(I2cDeviceSynch sensor) {
        this(sensor, 90);
    }

    public void initDevice() {
        sensor.disengage();
        sensor.engage();
        //setup address
        sensor.setI2cAddress(I2cAddr.create7bit(ADDR));
        sensor.waitForWriteCompletions(I2cWaitControl.WRITTEN);
    }

    public void startDevice() {
        //the below line will generate a NACK
        //I'm hoping all this will mean is more logs
        sensor.write8(Regs.RANGE_TAKE.REG, 0);
    }

    //WARNING: THIS CALL WILL DELAY THE MAIN LOOP
    public int getReading() {
        sensor.write8(Regs.RANGE_TAKE.REG, 0);
        if(waitMillis > 0) {
            try{
                TimeUnit.MILLISECONDS.sleep(waitMillis);
            }
            catch (Exception e) { /* HMMMMM */ }
        }
        //get the reading (will generate NACK)
        byte[] data = sensor.read(Regs.ADDR_UNLOCK.REG, 2);
        //cache time and return
        return (data[0] & 0xFF) << 8 | (data[1] & 0xFF);
    }

    //this call will return 0 if a reading is not ready, and -1 if the reading is invalid
    public int getReadingNoDelay() {
        if(lastTime == 0) {
            sensor.write8(Regs.RANGE_TAKE.REG, 0);
            lastTime = System.currentTimeMillis();
        }
        else if(System.currentTimeMillis() - lastTime > waitMillis) {
            lastTime = 0;
            //get the reading (will generate NACK)
            byte[] data = sensor.read(Regs.ADDR_UNLOCK.REG, 2);
            //return
            int ret = (data[0] & 0xFF) << 8 | (data[1] & 0xFF);
            if(ret <= 0) return -1;
            else return ret;
        }
        return 0;
    }

    public void stopDevice() {
        sensor.disengage();
        //sensor.close();
    }
}
