package org.firstinspires.ftc.teamcode.libraries.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cWaitControl;
import com.qualcomm.robotcore.hardware.TimestampedData;
import com.qualcomm.robotcore.hardware.TimestampedI2cData;

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
        RANGE_READ(0b00000000);


        public final byte REG;
        Regs(int reg) {
            this.REG = (byte)reg;
        }
    }

    private static final byte ADDR = 112;

    private final I2cDeviceSynch sensor;
    private final long waitNanos;
    private final OpMode mode;

    private long lastTime = 0;

    public MatbotixUltra(I2cDeviceSynch sensor, int waitMillis, OpMode mode) {
        this.sensor = sensor;
        this.waitNanos = TimeUnit.MILLISECONDS.toNanos(waitMillis);
        this.mode = mode;
    }

    MatbotixUltra(I2cDeviceSynch sensor) {
        this(sensor, 0, null);
    }

    public void initDevice() {
        sensor.disengage();
        sensor.engage();
        //setup address
        sensor.setI2cAddress(I2cAddr.create7bit(ADDR));
    }

    public void startDevice() {
        sensor.write8(Regs.RANGE_TAKE.REG, 0);
        lastTime = System.nanoTime();
    }

    public int getReading() {
        if(waitNanos > 0) {
            long run = System.nanoTime();
            if(run - lastTime < waitNanos) {
                try { wait(TimeUnit.NANOSECONDS.toMillis(waitNanos - (run - lastTime))); }
                catch (InterruptedException e) { /* hmmmm */ }
            }
        }
        TimestampedData data = sensor.readTimeStamped(Regs.RANGE_TAKE.REG, 2);
        if(waitNanos > 0) lastTime = data.nanoTime;
        return data.data[0] << 8 | data.data[1];
    }

    public void stopDevice() {
        sensor.disengage();
        sensor.close();
    }
}
