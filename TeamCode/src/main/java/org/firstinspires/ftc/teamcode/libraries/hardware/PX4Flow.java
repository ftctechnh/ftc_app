package org.firstinspires.ftc.teamcode.libraries.hardware;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import java.security.InvalidParameterException;

/**
 * Created by Noah on 2/1/2018.
 */

public class PX4Flow {
    //"A" prefix stands for accumulated, meaning integrated since the sensor was last read
    //"T" prefix stands for total, meaning since the sensor was last booted
    //unprefixed mean instantaneous (probably raw)
    private enum Regs {
        //regular accumulated frame
        T_FRAMECOUNT(2),
        FLOWX(2), //pixels
        FLOWY(2),
        VELX(2), //m/sec * 1000
        VELY(2),
        QUAL(2), //0-255
        GYROX(2), //gyro x radians/sec
        GYROY(2),
        GYROZ(2),
        GYRORANGE(1), //dunno
        SONARTIME(1),
        GDIST(2),
        //"intergral" frame, accumulate it yourself
        A_FRAMECOUNT(2),
        A_FLOWX(2), //rad * 10000 where rad = pix / focal len (in px)
        A_FLOWY(2),
        A_GYROX(2), //rad rotation change
        A_GYROY(2),
        A_GYROZ(2),
        A_INTTIME(4), //time since last read in micros
        A_SONARTIME(4),
        A_GDIST(2),
        GYROTEMP(2), //celdius * 100
        A_QUAL(1); //0-255, 255 best


        public byte REG;
        public final byte size;
        Regs(int size) {
            this.size = (byte)size;
        }

        public static void initRegs() {
            byte addr = 0;
            for(Regs reg : values()) {
                reg.REG = addr;
                addr += reg.size;
            }
        }
    }

    private static final byte ADDR = 0x49;
    private final I2cDeviceSynch sensor;

    private double flowX = 0;
    private double flowY = 0;


    public PX4Flow(I2cDeviceSynch px) {
        this.sensor = px;
    }

    public void initDevice() {
        //calculate register enums
        Regs.initRegs();
        //start i2c
        sensor.disengage();
        sensor.engage();
        //kill I2c if it was doing anythig
        sensor.setReadWindow(new I2cDeviceSynch.ReadWindow(0, 0, I2cDeviceSynch.ReadMode.ONLY_ONCE));
        //setup address
        sensor.setI2cAddress(I2cAddr.create7bit(ADDR));
        //read id
        //bad code!
        byte thing = sensor.read8(Regs.A_FRAMECOUNT.REG);
        if((thing & 0xFF) == 0) throw new InvalidParameterException("That's no sensor! " + (thing & 0xff));
    }

    public void startDevice() {
        sensor.setReadWindow(new I2cDeviceSynch.ReadWindow(Regs.A_FLOWX.REG, 4, I2cDeviceSynch.ReadMode.REPEAT));
    }

    public void stopDevice() {
        sensor.disengage();
        sensor.close();
    }
    
    public double[] getFlow() {
        //TODO: The focal to distance conversion
        byte[] read = sensor.read(Regs.A_FLOWX.REG, 4);
        return new double[] {(read[0] | read[1])/10000., (read[1] | read[2])/10000. };
    }
}
