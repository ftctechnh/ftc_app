package org.firstinspires.ftc.teamcode.libraries;

import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cWaitControl;

import java.io.InvalidObjectException;
import java.security.InvalidParameterException;

/**
 * Created by Noah on 10/16/2017.
 * I2C device wrapper for the APDS9960 Gesture sensor
 * Which in this case we will be using as a cheap distance sensor
 *
 * This sensor is an incredibly powerful gestre sensor, but this library is only written to exploit the distance parts
 * TODO: Gesture support for shits and giggles
 */

public class APDS9960 {
    //unsigned...?
    public static final byte ADDR = 0x39;
    //auto gain thresholds
    private static final int RAISE_GAIN_THRESH = 100;
    private static final int LOWER_GAIN_THRESH = 200;

    //instance of config
    private final Config config;
    private final I2cDeviceSynch sensor;
    private final boolean autoGain;
    //constructor
    public APDS9960(Config config, I2cDeviceSynch sensor, boolean autoGain) {
        this.config = config;
        this.sensor = sensor;
        this.autoGain = autoGain;
    }

    public APDS9960(Config config, I2cDeviceSynch sensor) {
        this(config, sensor, false);
    }

    //initialize using config
    public byte initDevice() {
        sensor.disengage();
        sensor.engage();
        //kill I2c if it was doing anythig
        //sensor.setReadWindow(new I2cDeviceSynch.ReadWindow(0, 0, I2cDeviceSynch.ReadMode.ONLY_ONCE));
        //setup address
        sensor.setI2cAddress(I2cAddr.create7bit(ADDR));
        sensor.enableWriteCoalescing(true);
        //read id
        //bad code!
        byte thing = sensor.read8(Regs.ID.REG);
        if((thing & 0xFF) != 0xAB) throw new InvalidParameterException("That's no sensor! " + thing);
        //disable device
        sensor.write8(Regs.ENABLE.REG, 0);
        //configure!
        sensor.write8(Regs.PILT.REG, config.PILT);
        sensor.write8(Regs.PIHT.REG, config.PIHT);
        sensor.write8(Regs.PERS.REG, config.PERS);
        sensor.write8(Regs.PPULSE.REG, config.PPULSE);
        sensor.write8(Regs.CONTROL.REG, config.CONTROL);
        sensor.write8(Regs.CONFIG2.REG, config.CONFIG2);
        //wait for it
        sensor.waitForWriteCompletions(I2cWaitControl.WRITTEN);
        return thing;
    }

    public void updateGain() {
        sensor.write8(Regs.CONTROL.REG, config.CONTROL);
        sensor.waitForWriteCompletions(I2cWaitControl.WRITTEN);
    }

    public void startDevice() {
        //enable
        sensor.write8(Regs.ENABLE.REG, this.config.ENABLE);
        //start I2c background polling
        //read from PDATA in background if no interrupt
        //else read from STATUS-PDATA to check interrupt
        //if(!config.interruptEnabled) sensor.setReadWindow(new I2cDeviceSynch.ReadWindow(Regs.PDATA.REG, 1, I2cDeviceSynch.ReadMode.REPEAT));
        //else sensor.setReadWindow(new I2cDeviceSynch.ReadWindow(Regs.STATUS.REG, 10, I2cDeviceSynch.ReadMode.REPEAT));
        sensor.waitForWriteCompletions(I2cWaitControl.WRITTEN);
    }

    public int getDist() {
        int dist = sensor.read8(Regs.PDATA.REG) & 0xff;
        if(autoGain) {
            if(config.gain != APDS9960.Config.DistGain.GAIN_1X && dist >= LOWER_GAIN_THRESH) setSensorGain(APDS9960.Config.DistGain.values()[this.config.gain.ordinal() - 1]);
            else if(config.gain != APDS9960.Config.DistGain.GAIN_8X && dist <= RAISE_GAIN_THRESH) setSensorGain(APDS9960.Config.DistGain.values()[this.config.gain.ordinal() + 1]);
        }
        return dist;
    }

    private void setSensorGain(APDS9960.Config.DistGain gain) {
        config.setPulse(config.len, config.count, config.strength, config.boost, gain);
        updateGain();
    }

    public boolean checkInterrupt() {
        //check for interrupt flag
        if(!config.interruptEnabled) throw new InvalidParameterException("Interrupts are not enabled on the sensor!");
        //read status for interrupt flag
        if((sensor.read8(Regs.STATUS.REG) & (1 << 5)) > 0){
            //clear interrupt
            sensor.write8(Regs.PICLEAR.REG, 0);
            //return true
            return true;
        }
        else return false;
    }

    /**
     * Returns a calibrated, linear sense of distance as read by the infrared proximity
     * part of the sensor. Distance is measured to the plastic housing at the front of the
     * sensor.
     *
     * Natively, the raw optical signal follows an inverse square law. Here, parameters have
     * been fitted to turn that into a <em>linear</em> measure of distance. The function fitted
     * was of the form:
     *
     *      APDS = a(mm)^b
     *
     * We can solve this for mm:
     *
     *      mm = (APDS/a)^(1/b)
     *
     * This fitted linearity is fairly accurate over a wide range of target surfaces, but is ultimately
     * affected by the infrared reflectivity of the surface. However, even on surfaces where there is
     * significantly different reflectivity, the linearity calculated here tends to be preserved,
     * so distance accuracy can often be refined with a simple further multiplicative scaling.
     *
     * Note that readings are most accurate when perpendicular to the surface. For non-perpendicularity,
     * a cosine correction factor is usually appropriate.
     *
     * I have taken the time to get coefficients for each level of gain on the sensor, and as such the
     * setting will automatically be taken into account
     *
     * @return the estimated MM from the sensor
     */

    //the coefficents
    private enum GainLinearizationCoff {
        COFF_1X(138451, -1.74),
        COFF_2X(332944, -1.78),
        COFF_4X(774727, -1.79),
        COFF_8X(2760000, -1.9);

        GainLinearizationCoff(double a, double b) {
            this.ainv = 1.0/a;
            this.binv = 1.0/b;
        }
        private double ainv;
        private double binv;

        public double linearize(int dist) {
            return Math.pow(dist * this.ainv, this.binv);
        }
    }

    public double getLinearizedDistance() {
        //linearize!
        return GainLinearizationCoff.values()[this.config.gain.ordinal()].linearize(getDist());
    }

    public double getLinearizedDistance(int dist, Config.DistGain gain) {
        return GainLinearizationCoff.values()[gain.ordinal()].linearize(dist);
    }

    //Register enums
    //I love java
    private enum Regs {
        ENABLE (0x80),
        PILT (0x89), //lower bits for thresh for prox int
        PIHT (0x8B), //higher bits
        PERS (0x8C), //int persistence filters
        PPULSE (0x8E), //pulse control
        CONTROL (0x8F), //LED and gain
        CONFIG2 (0x90), //LED boost
        ID (0x92),
        STATUS (0x93),
        PDATA (0x9C),
        PICLEAR (0xE5); //clear interrupt

        public final byte REG;
        Regs(int reg) {
            this.REG = (byte)reg;
        }
    }
    //configuration class, cuz that's how qualcomm does it
    public static class Config {
        public PulseLength len;
        public byte count;
        public LEDStrength strength;
        public LEDBoost boost;
        public DistGain gain;
        //enums for mode
        public enum PulseLength {
            PULSE_4US (0),
            PULSE_8US (1),
            PULSE_16US (2),
            PULSE_32US (3);

            private final byte bVal;
            PulseLength(int val) { this.bVal = (byte)val; }
        }

        public enum LEDStrength {
            STREN_100MA (0),
            STREN_50MA (1),
            STREN_25MA (2),
            STREN_12P5MA (3);

            private final byte bVal;
            LEDStrength(int val) { this.bVal = (byte)val; }
        }

        public enum DistGain {
            GAIN_1X (0),
            GAIN_2X (1),
            GAIN_4X (2),
            GAIN_8X (3);

            private final byte bVal;
            DistGain(int val) { this.bVal = (byte)val; }
        }

        public enum LEDBoost {
            BOOST_1X (0),
            BOOST_1P5X (1),
            BOOST_2X (2),
            BOOST_3X (3);

            private final byte bVal;
            LEDBoost(int val) { this.bVal = (byte)val; }
        }

        //members
        public byte ENABLE;
        public byte PILT;
        public byte PIHT;
        public byte PERS;
        public byte PPULSE;
        public byte CONTROL;
        public byte CONFIG2;

        public boolean interruptEnabled;
        //constructor w/ defaults
        //don't trust these
        public Config() {
            disableInterrupt();
            setPulse(PulseLength.PULSE_8US, (byte)8, LEDStrength.STREN_100MA, LEDBoost.BOOST_1X, DistGain.GAIN_1X);
        }

        //register specific functions
        public void disableInterrupt() {
            this.ENABLE = (byte)(0b00000101);
            this.PILT = 0;
            this.PIHT = 0;
            this.PERS = 0;
            this.interruptEnabled = false;
        }
        //might as well
        public void enableInterrupt(byte lowThresh, byte highThresh, byte overCount) {
            //argcheck
            if(overCount > 15 || overCount < 0) throw new InvalidParameterException("Interrupt persistence outside possible values");
            //enable power, proximity sensing, and interrupt if desired
            this.ENABLE = (byte)(0b00100101);//(byte)(1 | (1 << 2) | (1 << 5));
            this.PILT = lowThresh;
            this.PIHT = highThresh;
            this.PERS = (byte)(overCount << 4);
            this.interruptEnabled = true;
        }

        public void setPulse(PulseLength len, byte count, LEDStrength strength, LEDBoost boost, DistGain gain) {
            //argcheck
            if(count > 63 || count < 0) throw new InvalidParameterException("Pulse count outside possible values");
            //set pulse
            this.PPULSE = (byte)(count | (len.bVal << 6));
            //set drive and gain values
            this.CONTROL = (byte)((gain.bVal << 2) | (strength.bVal << 6));
            this.CONFIG2 = (byte)(boost.bVal << 4);
            this.gain = gain;
            this.len = len;
            this.count = count;
            this.strength = strength;
            this.boost = boost;
        }
    }

}
