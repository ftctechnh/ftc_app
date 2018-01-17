package org.firstinspires.ftc.teamcode.libraries.hardware;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cWaitControl;

import java.security.InvalidParameterException;

/**
 * Created by Noah on 1/10/2018.
 * Sensor library for APDS9930 prox sensor
 */

public class APDS9930 {
    //Register enums
    //I love java
    private enum Regs {
        ENABLE (0x00),
        PTIME (0x02),
        WTIME (0x03),
        CONFIG (0x0D),
        PPULSE (0x0E),
        CONTROL (0x0F),
        ID (0x12),
        STATUS (0x13),
        PDATAL (0x18),
        PDATAH (0x19),
        POFFSET (0x1E);

        public final byte REG;
        Regs(int reg) {
            this.REG = (byte)(reg | (1 << 7) | (1 << 5));
        }
    }

    public enum LEDDrive {
        STREN_100MA (0),
        STREN_50MA (1),
        STREN_25MA (2),
        STREN_12_5MA (3);

        private static final byte SHIFT = 6;
        private final byte bVal;
        LEDDrive(int val) { this.bVal = (byte)val; }
        byte getVal() { return (byte)(this.bVal << SHIFT); }
    }

    public enum ProxGain {
        GAIN_1X (0),
        GAIN_2X (1),
        GAIN_4X (2),
        GAIN_8X (3);

        private static final byte SHIFT = 2;
        private final byte bVal;
        ProxGain(int val) { this.bVal = (byte)val; }
        byte getVal() { return (byte)(this.bVal << SHIFT); }
    }

    public static final byte ADDR = 0x39;
    public static final byte ID = 0x39;
    //auto gain thresholds
    private static final int RAISE_GAIN_THRESH = 200;
    private static final int LOWER_GAIN_THRESH = 900;

    private static final byte ENABLE = 1 | (1 << 2);
    private byte PTIME = (byte)0xff;
    private byte WTIME = (byte)0xff;
    private byte PPULSE = (byte)0x08;
    private LEDDrive PDRIVE = LEDDrive.STREN_100MA;
    private final byte PDIODE = 2 << 4;
    private ProxGain PGAIN = ProxGain.GAIN_4X;
    private byte CONTROL = (byte)(PDRIVE.getVal() | PDIODE | PGAIN.getVal());
    private byte POFFSET = (byte)0x00;

    private final I2cDeviceSynch sensor;
    private final boolean autoGain;
    private final ProxGain maxGain;
    private final ProxGain minGain;

    private boolean deviceOn = false;

    public APDS9930(I2cDeviceSynch sensor) {
        this(sensor, false);
    }

    public APDS9930(I2cDeviceSynch sensor, boolean autoGain) {
        this(sensor, autoGain, ProxGain.GAIN_2X, ProxGain.GAIN_8X);
    }

    public APDS9930(I2cDeviceSynch sensor, boolean autoGain, ProxGain minGain, ProxGain maxGain) {
        this.sensor = sensor;
        this.autoGain = autoGain;
        this.minGain = minGain;
        this.maxGain = maxGain;
    }

    public byte initDevice() {
        sensor.disengage();
        sensor.engage();
        //kill I2c if it was doing anythig
        sensor.setReadWindow(new I2cDeviceSynch.ReadWindow(0, 0, I2cDeviceSynch.ReadMode.ONLY_ONCE));
        //setup address
        sensor.setI2cAddress(I2cAddr.create7bit(ADDR));
        sensor.enableWriteCoalescing(true);
        //read id
        //bad code!
        byte thing = sensor.read8(Regs.ID.REG);
        if((thing & 0xFF) != ID) throw new InvalidParameterException("That's no sensor! " + (thing & 0xff));
        //disable device
        sensor.write8(Regs.ENABLE.REG, 0);
        //write all teh configs
        sensor.write8(Regs.PTIME.REG, this.PTIME);
        sensor.write8(Regs.WTIME.REG, this.WTIME);
        sensor.write8(Regs.PPULSE.REG, this.PPULSE);
        sensor.write8(Regs.CONTROL.REG, this.CONTROL);
        sensor.write8(Regs.POFFSET.REG, this.POFFSET);
        waitForI2C();
        deviceOn = true;
        return thing;
    }

    public void startDevice() {
        sensor.write8(Regs.ENABLE.REG, ENABLE);
        //sensor.setReadWindow(new I2cDeviceSynch.ReadWindow(Regs.PDATAL.REG, 2, I2cDeviceSynch.ReadMode.REPEAT));
        waitForI2C();
    }

    public void stopDevice() {
        sensor.write8(Regs.ENABLE.REG, 0);
        sensor.disengage();
    }

    public int getDist() {
        int dist = (sensor.read8(Regs.PDATAL.REG) & 0xff) | ((sensor.read8(Regs.PDATAH.REG) & 0xff) << 8);
        if(autoGain) {
            if(this.PGAIN.ordinal() > minGain.ordinal() && dist >= LOWER_GAIN_THRESH) {
                setPGAIN(ProxGain.values()[this.PGAIN.ordinal() - 1]);
                waitForI2C();
            }
            else if(this.PGAIN.ordinal() < maxGain.ordinal() && dist <= RAISE_GAIN_THRESH) {
                setPGAIN(ProxGain.values()[this.PGAIN.ordinal() + 1]);
                waitForI2C();
            }
        }
        return dist;
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
        COFF_1X(10329.67742, -1.223433889),
        COFF_2X(66634.6608, -1.536053096),
        COFF_4X(93507.24087, -1.423473266),
        COFF_8X(274630.7561, -1.533135152);

        GainLinearizationCoff(double a, double b) {
            this.ainv = 1.0/a;
            this.binv = 1.0/b;
        }
        private double ainv;
        private double binv;

        public double linearize(int dist) {
            if(dist >= 0) return Math.pow(dist * this.ainv, this.binv);
            else return Math.abs(Math.pow(-dist * ainv, binv));
        }
    }

    public double getLinearizedDistance(boolean redCorrect) {
        //linearize!
        int dist = (sensor.read8(Regs.PDATAL.REG) & 0xff) | ((sensor.read8(Regs.PDATAH.REG) & 0xff) << 8);
        double out = GainLinearizationCoff.values()[this.PGAIN.ordinal()].linearize(getDist());
        if(!redCorrect) out -= 15;
        if(autoGain) {
            if(this.PGAIN.ordinal() > minGain.ordinal() && dist >= LOWER_GAIN_THRESH) setPGAIN(ProxGain.values()[this.PGAIN.ordinal() - 1]);
            else if(this.PGAIN.ordinal() < maxGain.ordinal() && dist <= RAISE_GAIN_THRESH) setPGAIN(ProxGain.values()[this.PGAIN.ordinal() + 1]);
        }
        return out;
    }

    public double getLinearizedDistance(int dist, ProxGain gain, boolean redCorrect) {
        return GainLinearizationCoff.values()[gain.ordinal()].linearize(getDist()) + (redCorrect ? 15 : 0);
    }

    public void setPTIME(byte pulseTime) {
        this.PTIME = pulseTime;
        if(deviceOn) sensor.write8(Regs.PTIME.REG, pulseTime);
    }

    public byte getPTIME(){
        return this.PTIME;
    }

    public void setWTIME(byte waitTime) {
        this.WTIME = waitTime;
        if(deviceOn) sensor.write8(Regs.WTIME.REG, waitTime);
    }

    public byte getWTIME() {
        return this.WTIME;
    }

    public void setPPULSE(byte pulseCount) {
        this.PPULSE = pulseCount;
        if(deviceOn) sensor.write8(Regs.PPULSE.REG, pulseCount);
    }

    public byte getPPULSE() {
        return this.PPULSE;
    }

    public void setPDRIVE(LEDDrive drive) {
        this.PDRIVE = drive;
        this.setControl();
    }

    public LEDDrive getPDRIVE() {
        return this.PDRIVE;
    }

    public void setPGAIN(ProxGain gain) {
        this.PGAIN = gain;
        this.setControl();
    }

    public ProxGain getPGAIN() {
        return this.PGAIN;
    }

    public void setPOFFSET(byte offset) {
        this.POFFSET = offset;
        if(deviceOn) sensor.write8(Regs.POFFSET.REG, offset);
    }

    public void waitForI2C() {
        sensor.waitForWriteCompletions(I2cWaitControl.WRITTEN);
    }

    private void setControl() {
        this.CONTROL = (byte)(PDRIVE.getVal() | PDIODE | PGAIN.getVal());
        if(deviceOn) sensor.write8(Regs.CONTROL.REG, this.CONTROL);
    }
}
