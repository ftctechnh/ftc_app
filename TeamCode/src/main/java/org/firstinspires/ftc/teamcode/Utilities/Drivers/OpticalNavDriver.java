package org.firstinspires.ftc.teamcode.Utilities.Drivers;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.configuration.I2cSensor;

@I2cSensor(name = "PAT9125EL Optical Nav", description = "Optical Navigation Chip", xmlTag = "PAT9125EL")
public class OpticalNavDriver extends I2cDeviceSynchDevice<I2cDeviceSynch> {
    private static final int i2cAddress = 0x73 << 1; //I think???

    public enum Register {
        FIRST(0x00),
        LAST(0x19),
        PRODUCT_ID1(0x00),
        PRODUCT_ID2(0x01),
        MOTION_STATUS(0x02),
        DELTA_X_LO(0x03),
        DELTA_Y_LO(0x04),
        OPERATION_MODE(0x05),
        CONFIGURATION(0x06),
        WRITE_PROTECT(0x09),
        SLEEP1(0x0A),
        SLEEP2(0x0B),
        RES_X(0x0D),
        RES_Y(0x0E),
        DELTA_XY_HI(0x12),
        SHUTTER(0x14),
        FRAME_AVG(0x17),
        ORIENTATION(0x19);

        public int byteValue;
        Register(int byteValue) {
            this.byteValue = byteValue;
        }
    }

    protected void setOptimalReadWindow()
    {
        // Sensor registers are read repeatedly and stored in a register. This method specifies the
        // registers and repeat read mode
        I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(
                Register.FIRST.byteValue,
                Register.LAST.byteValue - Register.FIRST.byteValue + 1,
                I2cDeviceSynch.ReadMode.REPEAT);
        this.deviceClient.setReadWindow(readWindow);
    }




    public OpticalNavDriver(I2cDeviceSynch deviceClient) {
        super(deviceClient, true);

        setOptimalReadWindow();
        this.deviceClient.setI2cAddress(I2cAddr.create7bit(i2cAddress)); //perhaps it is an 8 bit though

        registerArmingStateCallback(false);
        this.deviceClient.engage();
    }

    @Override
    protected boolean doInitialize() {
        return false;
    }

    @Override
    public Manufacturer getManufacturer() {
        return Manufacturer.Other;
    }

    @Override
    public String getDeviceName() {
        return null;
    }

    public byte[] read(Register r, int l) {
        return deviceClient.read(r.byteValue, l);
    }
    public void write(Register r, byte[] b) {
        deviceClient.write(r.byteValue, b);
    }


    public short[] getDeltaXY() {
        byte xlo = read(Register.DELTA_X_LO, 1)[0];
        byte ylo = read(Register.DELTA_Y_LO, 1)[0];
        byte xyhi = read(Register.DELTA_XY_HI, 1)[0];


        byte ymask = (byte) 0b00001111;
        byte xmask = (byte) 0b11110000;

        short xbits = (short) (xlo | ((xyhi & xmask) << 4));
        short ybits = (short) (ylo | ((xyhi & ymask) << 8));


        return new short[]{xbits, ybits};
    }
}
